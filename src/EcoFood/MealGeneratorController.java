package EcoFood;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class MealGeneratorController implements Initializable {

    public TableView<Meal> tableMealList;
    public TableColumn<Meal, SimpleStringProperty> colNameMealList = null;
    public TableColumn<Meal, SimpleDoubleProperty> colSkillPointsMealList = null;
    public TableColumn<Meal, SimpleDoubleProperty> colBalanceMultiplierMealList = null;
    public TableColumn<Meal, SimpleDoubleProperty> colTastinessMealList = null;
    public TableColumn<Meal, SimpleIntegerProperty> colSurvivalSkillLvlMealList = null;
    public TableColumn<Meal, SimpleStringProperty> colSkillsMealList = null;
    public TableColumn<Meal, SimpleListProperty<FoodRecipe>> colIngredientsMealList = null;
    public TableColumn<Meal, SimpleListProperty<String>> colToolsMealList = null;

    public TextField textNumRecipes;
    public TextField textNumItemsPerMeal;
    public TextField textNumCalculations;

    public Button buttonSave;
    public Button buttonGenerate;
    public Button buttonRefine;
    public Button buttonClose;

    public ProgressBar progressGenerate;
    private int progressCounter;

    private ObservableList<FoodRecipe> recipes = FXCollections.observableArrayList();
    private ObservableList<Meal> meals = FXCollections.observableArrayList();
    private SortedList<Meal> mealsSortedList = null;
    private ObservableList<Meal> balancedMeals = FXCollections.observableArrayList();


    private EcoFoodController parent = null;
    private GeneratorTask task = null;

    public void setParent(EcoFoodController parent) {
        this.parent = parent;
    }



    public static long binomialCoefficient(long n, long k) {
        final long m = Math.max(k, n - k);
        double temp = 1;
        for (long i = n, j = 1; i > m; i--, j++) {
            temp = temp * i / j;
        }
        return (long) temp;
    }

    public void setRecipes(ObservableList<FoodRecipe> recipes) {
        this.recipes = recipes;
        int numRecipes = recipes.size();
        int numItemsPerMeal = Integer.parseInt(textNumItemsPerMeal.getText());
        textNumRecipes.setText(String.format("%d",numRecipes));
        textNumCalculations.setText(String.format("%d",binomialCoefficient(numRecipes+numItemsPerMeal-1,numItemsPerMeal)));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colNameMealList.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSkillPointsMealList.setCellValueFactory(new PropertyValueFactory<>("skillPoints"));
        colBalanceMultiplierMealList.setCellValueFactory(new PropertyValueFactory<>("balanceMultiplier"));
        colTastinessMealList.setCellValueFactory(new PropertyValueFactory<>("tastinessMultiplier"));
        colSurvivalSkillLvlMealList.setCellValueFactory(new PropertyValueFactory<>("survivalSkill"));
        colSkillsMealList.setCellValueFactory(new PropertyValueFactory<>("skills"));
        colIngredientsMealList.setCellValueFactory(new PropertyValueFactory<>("ingredients"));
        colToolsMealList.setCellValueFactory(new PropertyValueFactory<>("craftStations"));

        mealsSortedList = new SortedList<>(meals.filtered(p -> true));
        mealsSortedList.comparatorProperty().bind(tableMealList.comparatorProperty());
        tableMealList.setItems(mealsSortedList);
        tableMealList.setPlaceholder(new Label("Generate meals from the previously selected food items\n(all items with count>0)"));
        colSkillPointsMealList.setSortType(TableColumn.SortType.DESCENDING);
        tableMealList.getSortOrder().addAll(colSkillPointsMealList);

        tableMealList.setRowFactory(tv -> new TableRow<Meal>() {
            @Override
            public void updateItem(Meal item, boolean empty) {
                super.updateItem(item, empty) ;
                if (item == null) {
                    setStyle("");
                } else if (item.getCount() != -1) {
                    setStyle("-fx-background-color: green;");
                } else {
                    setStyle("");
                }
            }
        });

        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (change.getText().isBlank()) return change;
            if (change.getText().matches("[0-9]*") && Integer.parseInt(change.getControlNewText()) <= 20) {
                //System.out.println(change.getControlNewText());
                return change;
            }
            return null;
        };
        textNumItemsPerMeal.setTextFormatter(new TextFormatter<String>(filter));
        textNumItemsPerMeal.textProperty().addListener((obs, oldValue, newValue) -> {
            if(!newValue.isBlank()) {
                int numRecipes = recipes.size();
                int numItemsPerMeal = Integer.parseInt(newValue);
                textNumCalculations.setText(String.format("%d", binomialCoefficient(numRecipes + numItemsPerMeal - 1, numItemsPerMeal)));
            }
            else
                textNumCalculations.setText("1");
        });
        textNumItemsPerMeal.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if(!newValue && textNumItemsPerMeal.getText().isBlank()) {
                textNumItemsPerMeal.setText("0");
                textNumCalculations.setText("1");
            }
        });
        textNumItemsPerMeal.setText("4");

    }

    public void onButtonClose() {
        if (buttonClose.getText().equals("Close")) {
            Stage stage = (Stage) buttonClose.getScene().getWindow();
            stage.close();
        } else if (buttonClose.getText().equals("Cancel")) {
            cancelTask();
        }
    }
    public void cancelTask() {
        if (task!=null && task.isRunning())
            task.cancel();
        System.out.println("Cancelling task");
    }

    public void onButtonGenerate() {
        int numRecipes = recipes.size();
        int numItemsPerMeal = Integer.parseInt(textNumItemsPerMeal.getText());
        long numCalculations = Long.parseLong(textNumCalculations.getText());
        meals.clear();
        balancedMeals.clear();
//        colSkillPointsMealList.setSortType(TableColumn.SortType.DESCENDING);
//        tableMealList.getSortOrder().clear();
//        tableMealList.getSortOrder().addAll(colSkillPointsMealList);

        //new
        buttonGenerate.setDisable(true);
        buttonClose.setText("Cancel");
        buttonSave.setDisable(true);
        buttonRefine.setDisable(true);
        if (task != null && task.isRunning())
            task.cancel();

        task = new GeneratorTask(recipes, parent.getBaseGain(), parent.getSkillMultiplier(),numCalculations,numRecipes,numItemsPerMeal );
        progressGenerate.progressProperty().bind(task.progressProperty());
        progressGenerate.setVisible(true);
        task.messageProperty().addListener((obs,oldValue,newValue) -> {
            if (newValue.equals("Done!")) {
                System.out.println("Generation task done!");
            } else if (newValue.equals("Cancelled!")) {
                System.out.println("Generation task cancelled!");
            } else if (newValue.equals("Failed!")) {
                System.out.println("Generation task failed!");
            }
            meals.addAll(task.meals);
            //balancedMeals.addAll(task.balancedMeals);

            progressGenerate.setVisible(false);
            buttonGenerate.setDisable(false);
            buttonClose.setText("Close");
        });

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();


//        progressCounter = 0;
//
//        int[] chosen = new int[numRecipes];
//        for (int i = 0; i<numRecipes; i++)
//            chosen[i] = 0;
//
//        //Task task = new Task<Void>(){
//
//        combinationRepetition(chosen,0,numItemsPerMeal,0,numRecipes-1);
//
//        for (int i = 0; i < balancedMeals.size(); i++) {
//            int index = meals.indexOf(balancedMeals.get(i));
//            if(index < 0) {
//                balancedMeals.get(i).setName("Balanced"+ (i+1));
//                meals.add(balancedMeals.get(i));
//            }
//            else
//                meals.get(index).setName("Balanced"+ (i+1));
//        }
//        progressGenerate.setVisible(false);
    }

    public void onButtonRefine() {
        Meal mealToRefine = tableMealList.getSelectionModel().getSelectedItem();
        if (mealToRefine == null) return;

        FoodRecipe recipeMeal = new FoodRecipe();
        recipeMeal.setCount(mealToRefine.getCount());
        recipeMeal.setName("RefinedMeal");
        recipeMeal.setCalories(mealToRefine.getTotalCalories());
        recipeMeal.setTastiness(mealToRefine.getTastinessMultiplier());
        recipeMeal.setCarbs(mealToRefine.getTotalCarbs());
        recipeMeal.setFats(mealToRefine.getTotalFats());
        recipeMeal.setProteins(mealToRefine.getTotalProteins());
        recipeMeal.setVitamins(mealToRefine.getTotalVitamins());
        recipeMeal.calcNutrients();
        recipeMeal.setEmulationSource(mealToRefine);
        System.out.println(recipes.size());
        recipes.add(recipeMeal);
        System.out.println(recipes.size());

        onButtonGenerate();

        //if(!recipes.remove(recipeMeal)) System.out.println("Failed to remove emulated meal");
        //System.out.println(recipes.size());
    }

    /*    private void combinationRepetition(int[] chosen, int index, int r, int start, int end)
        {
            if (index == r) {
                ObservableList<FoodRecipe> recipes = FXCollections.observableArrayList();
                StringBuilder name = new StringBuilder("Meal");
                for (int i = 0; i <= end; i++) {
                    name.append(chosen[i]);
                    if (chosen[i]>0) {
                    FoodRecipe recipe = new FoodRecipe(this.recipes.get(i));
                    recipe.setCount(chosen[i]);
                    recipes.add(recipe);
                    }
                }
                //System.out.println(name);
                int maxMeals = 10;
                Meal meal = new Meal();
                meal.setName(name.toString());
                meal.setRecipes(recipes);
                meal.updateSkillPoints((int) parent.getBaseGain(),parent.getSkillMultiplier(),0,0);
                if(meal.getBalanceMultiplier() > 0.49f) {
                    //System.out.println(name.toString());
                    balancedMeals.add(meal);
                }
                if (mealsSortedList.size()<maxMeals) {
                    meals.add(meal);
                    //System.out.println("add: " + meal.getSkillPoints());
                }
                else if(meal.getSkillPoints() > mealsSortedList.get(maxMeals-1).getSkillPoints()){
                    //System.out.println(mealsSortedList.get(maxMeals-1).getSkillPoints() + " -> " + meal.getSkillPoints());
                    meals.removeAll(mealsSortedList.get(maxMeals-1));
                    meals.add(meal);
                }

                progressCounter++;
                if (progressCounter%10000 == 0) {
                    int numCalculations = Integer.parseInt(textNumCalculations.getText());
                    progressGenerate.setProgress((double)progressCounter / (double)numCalculations);
                }
                //else System.out.println("discard: " + meal.getSkillPoints());
                return;
            }
            for (int i = start; i <= end; i++) {
                chosen[i]++;
                combinationRepetition(chosen, index + 1, r, i, end);
                chosen[i]--;
            }
        }
    */
    public void onButtonSave() {
        Meal meal= tableMealList.getSelectionModel().getSelectedItem();
        if (meal != null) {
            String mealName;
            TextInputDialog dialog = new TextInputDialog(meal.getName());
            dialog.setTitle("Enter Meal Name");
            dialog.setHeaderText(null);
            dialog.setContentText("Name:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                mealName = result.get();
            } else {
                return;
            }
            meal.setName(mealName);
            meal.setCount(1);
            tableMealList.refresh();
            parent.addMeal(meal);
        }
    }

    public void onPressMealList() {
        if (task != null && task.isRunning()) return;
        Meal meal = tableMealList.getSelectionModel().getSelectedItem();
        if (meal != null) {
            buttonRefine.setDisable(false);
            buttonSave.setDisable(false);
        } else {
            buttonRefine.setDisable(true);
            buttonSave.setDisable(true);
        }

    }


}

