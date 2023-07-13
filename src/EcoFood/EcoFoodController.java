package EcoFood;

// TODO: diversified & balancer , threads, save base x
// BUG: import,add 30 best, generate 6, refine best 6 -> skips meals

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.regex.MatchResult;

public class EcoFoodController implements Initializable {

    public TableView<FoodRecipe> recipeTable = null;
    public TableColumn<FoodRecipe, Integer> colCount = null;
    public TableColumn<FoodRecipe, String> colName = null;
    public TableColumn<FoodRecipe, Integer> colCalories = null;
//    public TableColumn<FoodRecipe, Integer> colCarbs = null;
    public TableColumn<FoodRecipe, String> colCarbs = null;
    public TableColumn<FoodRecipe, String> colProteins = null;
    public TableColumn<FoodRecipe, String> colFats = null;
    public TableColumn<FoodRecipe, String> colVitamins = null;
    public TableColumn<FoodRecipe, String> colNutrients = null;
    public TableColumn<FoodRecipe, String> colSkill = null;
    public TableColumn<FoodRecipe, Integer> colLvl = null;
    public TableColumn<FoodRecipe, String> colCraftStation = null;
    public TableColumn<FoodRecipe, Integer> colTastiness = null;

    public TableView<FoodRecipe> mealTable = null;
    public TableColumn<FoodRecipe, Integer> colCountMeal = null;
    public TableColumn<FoodRecipe, String> colNameMeal = null;
    public TableColumn<FoodRecipe, Integer> colCaloriesMeal = null;
    public TableColumn<FoodRecipe, Integer> colCarbsMeal = null;
    public TableColumn<FoodRecipe, Integer> colProteinsMeal = null;
    public TableColumn<FoodRecipe, Integer> colFatsMeal = null;
    public TableColumn<FoodRecipe, Integer> colVitaminsMeal = null;
    public TableColumn<FoodRecipe, Integer> colNutrientsMeal = null;

    public TableView<Meal> mealListTable = null;
    public TableColumn<Meal, String> colCountMealList = null;
    public TableColumn<Meal, String> colNameMealList = null;
    public TableColumn<Meal, Double> colSkillPointsMealList = null;
    public TableColumn<Meal, Double> colBalanceMultiplierMealList = null;
    public TableColumn<Meal, String> colSurvivalSkillLvlMealList = null;
    public TableColumn<Meal, String> colSkillsMealList = null;
    public TableColumn<Meal, String> colIngredientsMealList = null;
    public TableColumn<Meal, String> colToolsMealList = null;

    public Label labelCalories = null;
    public Label labelCarbs = null;
    public Label labelProteins = null;
    public Label labelFats = null;
    public Label labelVitamins = null;
    public Label labelNutrients = null;
    public Label labelBalanceMultiplier = null;
    public Label labelTastiness = null;
    public Label labelVariety = null;
    public Label labelSkillPoints = null;
    public Label labelSurvivalSkillLvl = null;

    public TabPane tabsFood = null;

    public TextField textBaseGain = null;
    public TextField textSkillMultiplier = null;

    public Button buttonCreateEdit = null;
    public Button buttonResetDelete = null;
    public Button buttonSearch = null;

    public Spinner<Integer> spCampfireCooking = null;
    public Spinner<Integer> spMilling = null;
    public Spinner<Integer> spCooking = null;
    public Spinner<Integer> spAdvCooking = null;
    public Spinner<Integer> spBaking = null;
    public Spinner<Integer> spAdvBaking = null;

    public CheckBox cbCampfireCooking = null;
    public CheckBox cbMilling = null;
    public CheckBox cbCooking = null;
    public CheckBox cbAdvCooking = null;
    public CheckBox cbBaking = null;
    public CheckBox cbAdvBaking = null;
    public CheckBox cbRaw = null;
    public CheckBox cbCharred = null;
    public CheckBox cbHidden = null;

    public RadioButton rbTastedYes = null;
    public RadioButton rbTastedNo = null;
    public RadioButton rbTastedBoth = null;
    public ToggleGroup rbtgTasted = null;

    public MenuItem miAddBest = null;

    public PieChart pieNutrients = null;

    private ObservableList<FoodRecipe> recipeList = FXCollections.observableArrayList(recipe -> new Observable[]{recipe.countProperty()});
    private ObservableList<FoodRecipe> emulatedMealListFilteredList = FXCollections.observableArrayList(recipe -> new Observable[]{recipe.countProperty()});

    private ObservableList<Meal> mealList = FXCollections.observableArrayList(recipe -> new Observable[]{recipe.countProperty()});
    private FilteredList<FoodRecipe> recipeFilteredList = null;
    private FilteredList<FoodRecipe> mealFilteredList = null;
    //private FilteredList<Meal> mealListFilteredList = null;
    private SortedList<FoodRecipe> recipeSortedList = null;
    private SortedList<Meal> mealListSortedList = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCalories.setCellValueFactory(new PropertyValueFactory<>("calories"));
//        colCarbs.setCellValueFactory(new PropertyValueFactory<>("carbs"));
        colCarbs.setCellValueFactory(param -> Bindings.createObjectBinding(()-> {
            double carbs = param.getValue().getCarbs();
            int calories = param.getValue().getCalories();
            if (calories == 0) return String.valueOf(carbs);
            double totalMeanCarbs = Double.parseDouble(labelCarbs.getText().replace(',','.'));
            int totalCalories = Integer.parseInt(labelCalories.getText());
            double score = (carbs - totalMeanCarbs) /(1+((double)totalCalories/(double)calories));
            return carbs + "\n(" + (score < 0 ? "" : "+") + String.format("%.1f", score) + ")";
            } ,param.getValue().carbsProperty(), param.getValue().caloriesProperty(),labelCarbs.textProperty(),labelCalories.textProperty()));
        colProteins.setCellValueFactory(param -> Bindings.createObjectBinding(()-> {
            double proteins = param.getValue().getProteins();
            int calories = param.getValue().getCalories();
            if (calories == 0) return String.valueOf(proteins);
            double totalMeanProteins = Double.parseDouble(labelProteins.getText().replace(',','.'));
            int totalCalories = Integer.parseInt(labelCalories.getText());
            double score = (proteins - totalMeanProteins) /(1+((double)totalCalories/(double)calories));
            return proteins + "\n(" + (score < 0 ? "" : "+") + String.format("%.1f", score) + ")";
        } ,param.getValue().proteinsProperty(), param.getValue().caloriesProperty(),labelProteins.textProperty(),labelCalories.textProperty()));
        colFats.setCellValueFactory(param -> Bindings.createObjectBinding(()-> {
            double fats = param.getValue().getFats();
            int calories = param.getValue().getCalories();
            if (calories == 0) return String.valueOf(fats);
            double totalMeanFats = Double.parseDouble(labelFats.getText().replace(',','.'));
            int totalCalories = Integer.parseInt(labelCalories.getText());
            double score = (fats - totalMeanFats) /(1+((double)totalCalories/(double)calories));
            return fats + "\n(" + (score < 0 ? "" : "+") + String.format("%.1f", score) + ")";
        } ,param.getValue().fatsProperty(), param.getValue().caloriesProperty(),labelFats.textProperty(),labelCalories.textProperty()));
        colVitamins.setCellValueFactory(param -> Bindings.createObjectBinding(()-> {
            double vitamins = param.getValue().getVitamins();
            int calories = param.getValue().getCalories();
            if (calories == 0) return String.valueOf(vitamins);
            double totalMeanVitamins = Double.parseDouble(labelVitamins.getText().replace(',','.'));
            int totalCalories = Integer.parseInt(labelCalories.getText());
            double score = (vitamins - totalMeanVitamins) /(1+((double)totalCalories/(double)calories));
            return vitamins + "\n(" + (score < 0 ? "" : "+") + String.format("%.1f", score) + ")";
        } ,param.getValue().vitaminsProperty(), param.getValue().caloriesProperty(),labelVitamins.textProperty(),labelCalories.textProperty()));
        colNutrients.setCellValueFactory(param -> Bindings.createObjectBinding(()-> {
            double carbs  = param.getValue().getCarbs();
            double fats  = param.getValue().getFats();
            double proteins  = param.getValue().getProteins();
            double vitamins = param.getValue().getVitamins();
            int calories = param.getValue().getCalories();
            if (calories == 0) return String.valueOf(carbs+fats+proteins+vitamins);
            /*double totalMeanCarbs = Double.parseDouble(labelCarbs.getText().replace(',','.'));
            double totalMeanFats = Double.parseDouble(labelFats.getText().replace(',','.'));
            double totalMeanProteins = Double.parseDouble(labelProteins.getText().replace(',','.'));
            double totalMeanVitamins = Double.parseDouble(labelVitamins.getText().replace(',','.'));
            int totalCalories = Integer.parseInt(labelCalories.getText());
            double newCarbs = (carbs*calories + totalMeanCarbs*totalCalories) / (double)(calories+totalCalories);
            double newFats = (fats*calories + totalMeanFats*totalCalories) / (double)(calories+totalCalories);
            double newProteins = (proteins*calories + totalMeanProteins*totalCalories) / (double)(calories+totalCalories);
            double newVitamins = (vitamins*calories + totalMeanVitamins*totalCalories) / (double)(calories+totalCalories);
            double maxNutrient = Math.max(newCarbs,Math.max(newFats, Math.max(newProteins, newVitamins)));
            double minNutrient = Math.min(newCarbs,Math.min(newFats, Math.min(newProteins, newVitamins)));
            double score = 100*(-0.5f + (minNutrient/maxNutrient)); */
            //System.out.println("Factored: "+ param.getValue().getName() + param.getValue().getTastiness()+String.format("%02.2f",(carbs + fats + proteins + vitamins)*(1+param.getValue().getTastiness()/100.0f)))
            DecimalFormat df = new DecimalFormat("00.00");
            return df.format((carbs + fats + proteins + vitamins)*(1+param.getValue().getTastiness()/100.0f)); // + "\n(" + (score>=100? "MAX":String.format("%.1f",score)) + ")";
        } ,param.getValue().carbsProperty(), param.getValue().fatsProperty(), param.getValue().proteinsProperty(), param.getValue().vitaminsProperty(),
                param.getValue().caloriesProperty(),param.getValue().tastinessProperty(),labelCarbs.textProperty(),labelFats.textProperty(),labelProteins.textProperty(),labelVitamins.textProperty(),labelCalories.textProperty()));
//        colNutrients.setCellValueFactory(new PropertyValueFactory<>("nutrients"));
        colSkill.setCellValueFactory(new PropertyValueFactory<>("skill"));
        colLvl.setCellValueFactory(new PropertyValueFactory<>("skillLvl"));
        colCraftStation.setCellValueFactory(new PropertyValueFactory<>("craftStation"));
        colTastiness.setCellValueFactory(new PropertyValueFactory<>("tastiness"));
        colTastiness.setCellFactory(ComboBoxTableCell.forTableColumn(-30, -20, -10, -1, 0, 10, 20, 30));

        colCountMeal.setCellValueFactory(new PropertyValueFactory<>("count"));
        colNameMeal.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCaloriesMeal.setCellValueFactory(new PropertyValueFactory<>("calories"));
        colCarbsMeal.setCellValueFactory(new PropertyValueFactory<>("carbs"));
        colProteinsMeal.setCellValueFactory(new PropertyValueFactory<>("proteins"));
        colFatsMeal.setCellValueFactory(new PropertyValueFactory<>("fats"));
        colVitaminsMeal.setCellValueFactory(new PropertyValueFactory<>("vitamins"));
        colNutrientsMeal.setCellValueFactory(new PropertyValueFactory<>("nutrients"));

        colCountMealList.setCellValueFactory(new PropertyValueFactory<>("count"));
        colNameMealList.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSkillPointsMealList.setCellValueFactory(new PropertyValueFactory<>("skillPoints"));
        colBalanceMultiplierMealList.setCellValueFactory(new PropertyValueFactory<>("balanceMultiplier"));
        colSurvivalSkillLvlMealList.setCellValueFactory(new PropertyValueFactory<>("survivalSkill"));
        colSkillsMealList.setCellValueFactory(new PropertyValueFactory<>("skills"));
        colIngredientsMealList.setCellValueFactory(new PropertyValueFactory<>("ingredients"));
        colToolsMealList.setCellValueFactory(new PropertyValueFactory<>("craftStations"));

        spCampfireCooking.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 7, 7));
        spMilling.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 7, 7));
        spCooking.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 7, 7));
        spAdvCooking.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 7, 7));
        spBaking.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 7, 7));
        spAdvBaking.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 7, 7));

        recipeFilteredList = new FilteredList<>(recipeList, p -> true);
        recipeSortedList = new SortedList<>(recipeFilteredList);
        recipeSortedList.comparatorProperty().bind(recipeTable.comparatorProperty());
        recipeTable.setItems(recipeSortedList);

        Button buttonImport = new Button("Import Data");
        buttonImport.setOnAction(e -> onImportFromECO());
        //recipeTable.setPlaceholder(buttonImport);
        Button buttonLoad = new Button("Load Data");
        buttonLoad.setOnAction(e -> onLoad());
        HBox hBoxPH = new HBox(10);
        hBoxPH.setAlignment(Pos.CENTER);
        hBoxPH.getChildren().addAll(buttonImport, buttonLoad);
        Label labelPH = new Label("No food items to show (none loaded or none matching the selected filters).");
        VBox vBoxPH = new VBox(10);
        vBoxPH.setAlignment(Pos.CENTER);
        vBoxPH.getChildren().addAll(hBoxPH, labelPH);
        recipeTable.setPlaceholder(vBoxPH);

        colNutrients.setSortType(TableColumn.SortType.DESCENDING);
        recipeTable.getSortOrder().addAll(colNutrients);

        mealFilteredList = recipeList.filtered(recipe -> recipe.getCount() > 0);
        SortedList<FoodRecipe> mealSortedList = new SortedList<>(mealFilteredList);
        mealSortedList.comparatorProperty().bind(mealTable.comparatorProperty());
        mealTable.setItems(mealSortedList);
        mealTable.setPlaceholder(new Label("Click on table above to add items,\nclick on this table to remove them.\n Click three times on Tastiness Column to set tastiness."));

        //mealListFilteredList = mealList.filtered(meal -> meal.getCount() > 0);
        //SortedList<Meal> mealListSortedList = new SortedList<>(mealListFilteredList);
        FilteredList<Meal> mealListFilteredList = mealList.filtered(meal -> meal.getCount() > 0);
        mealListSortedList = new SortedList<>(mealListFilteredList);
        mealListSortedList.comparatorProperty().bind(mealListTable.comparatorProperty());
        mealListTable.setItems(mealList);

        spCampfireCooking.valueProperty().addListener((obs, oldValue, newValue) ->
                onFilterChange());
        spAdvBaking.valueProperty().addListener((obs, oldValue, newValue) ->
                onFilterChange());
        spBaking.valueProperty().addListener((obs, oldValue, newValue) ->
                onFilterChange());
        spAdvCooking.valueProperty().addListener((obs, oldValue, newValue) ->
                onFilterChange());
        spCooking.valueProperty().addListener((obs, oldValue, newValue) ->
                onFilterChange());
        spMilling.valueProperty().addListener((obs, oldValue, newValue) ->
                onFilterChange());

        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (change.getText().isBlank()) return change;
            if (change.getText().matches("\\d*")) return change;
            return null;
        };
        textSkillMultiplier.setTextFormatter(new TextFormatter<String>(filter));
        textSkillMultiplier.textProperty().addListener((obs, oldValue, newValue) -> {
            if(!newValue.isBlank()) {
                updateMeal();
            }
        });
        textSkillMultiplier.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if(!newValue && textSkillMultiplier.getText().isBlank()) {
                textSkillMultiplier.setText("0");
                updateMeal();
            }
        });
        textBaseGain.setTextFormatter(new TextFormatter<String>(filter));
        textBaseGain.textProperty().addListener((obs, oldValue, newValue) -> {
            if(!newValue.isBlank())
                updateMeal();
        });
        textBaseGain.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if(!newValue && textBaseGain.getText().isBlank()) {
                textBaseGain.setText("0");
                updateMeal();
            }
        });

        tabsFood.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if (newTab.getText().equals("Meal Creator")){
                buttonCreateEdit.setText("Create");
                buttonResetDelete.setText("Reset");
                miAddBest.setDisable(false);
                mealTable.setItems(mealFilteredList);
                labelVariety.setVisible(false);
                updateMeal();
            }
            else if (newTab.getText().equals("Meals")){
                buttonCreateEdit.setText("Edit");
                buttonResetDelete.setText("Delete");
                labelVariety.setVisible(true);
                miAddBest.setDisable(true);
                updateDiet();
                mealTable.setItems(emulatedMealListFilteredList);
            }
        });

        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Carbs", 0),
                        new PieChart.Data("Fats", 0),
                        new PieChart.Data("Proteins", 0),
                        new PieChart.Data("Vitamins", 0));
        pieNutrients.setData(pieChartData);

        updateMeal();
        onFilterChange();
    }

    public void onImportFromECO() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select ECO root folder");
        File defaultDirectory = new File("C:/Program Files (x86)/Steam/steamapps/common/Eco");
        if (defaultDirectory.exists())
            chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(recipeTable.getScene().getWindow());
        if (selectedDirectory == null) return;
        String ecoPath = selectedDirectory.getPath();

        File folder = new File(ecoPath + "/Eco_Data/Server/Mods/__core__/AutoGen/Food");
        File[] foodFiles = folder.listFiles();
        // add seeds
        folder = new File(ecoPath + "/Eco_Data/Server/Mods/__core__/AutoGen/Seed");
        File[] seedFiles = folder.listFiles();
        folder = new File(ecoPath + "/Eco_Data/Server/Mods/__core__/AutoGen/Recipe");
        File[] recipeFiles = folder.listFiles();
        if (seedFiles == null || foodFiles == null) {
            System.out.println("Couldn't find Eco files");
            return;
        }
        File[] listOfFiles = Arrays.copyOf(foodFiles, foodFiles.length + seedFiles.length);
        System.arraycopy(seedFiles, 0, listOfFiles, foodFiles.length, seedFiles.length);
        for (File file : listOfFiles) {
            if (file.isFile()) {
                FoodRecipe recipe = new FoodRecipe();
                try {
                    Scanner in = new Scanner(new FileReader(file.getPath()));
//                        read name
                    in.findWithinHorizon("\\[\\s*LocDisplayName\\s*\\(\\s*\"(.+)\"\\s*\\)\\s*]", 0);
                    MatchResult result = in.match();
                    recipe.setName(result.group(1));

//                        check for raw or hidden food
                    boolean isRaw = (in.findWithinHorizon("\\[\\s*Tag\\s*\\(\\s*\"Crop\"", 0) != null);
                    boolean isHidden = (in.findWithinHorizon("\\[\\s*Category\\s*\\(\\s*\"Hidden\"[\\s\\S]+partial\\s+class.+:\\s*(?:FoodItem|SeedItem)", 0) != null);

//                        read calories
                    if (in.findWithinHorizon("float\\s+Calories\\s*=>\\s*(\\d+)\\s*;", 0) == null) {
                        System.out.println("Failed to parse calories: " + file.getName());
                        continue;
                    }
                    result = in.match();
                    recipe.setCalories(Integer.parseInt(result.group(1)));

//                        read nutrients
                    if (in.findWithinHorizon("new\\s+Nutrients\\s*\\(\\)\\s*\\{\\s*Carbs\\s*=\\s*(\\d+)\\s*,\\s*Fat\\s*=\\s*(\\d+)\\s*,\\s*Protein\\s*=\\s*(\\d+)\\s*,\\s*Vitamins\\s*=\\s*(\\d+)\\s*}\\s*;", 0) == null) {
                        System.out.println("Failed to parse nutrients: " + file.getName());
                        continue;
                    }
                    result = in.match();
                    recipe.setCarbs(Integer.parseInt(result.group(1)));
                    recipe.setFats(Integer.parseInt(result.group(2)));
                    recipe.setProteins(Integer.parseInt(result.group(3)));
                    recipe.setVitamins(Integer.parseInt(result.group(4)));
                    recipe.calcNutrients();
                    if (recipe.getNutrients() == 0) {
                        continue;
                    }

//                        read skill and crafting station
                    if (isRaw) {
                        recipe.setSkill("Gathering");
                        recipe.setSkillLvl(0);
                        recipe.setCraftStation("Raw Food");
                    } else if (isHidden) {
                        recipe.setSkill("Hidden");
                        recipe.setSkillLvl(0);
                        recipe.setCraftStation("None");
                    } else try {
                        in.findWithinHorizon("\\[\\s*RequiresSkill\\s*\\(\\s*typeof\\s*\\(\\s*(\\w+)\\s*\\)\\s*,\\s*(\\d+)\\s*\\)\\s*]", 0);
                        result = in.match();
                        recipe.setSkill(result.group(1).replaceAll("Skill", ""));
                        recipe.setSkillLvl(Integer.parseInt(result.group(2)));

                        in.findWithinHorizon("CraftingComponent\\.AddRecipe\\s*\\(\\s*tableType\\s*:\\s*typeof\\s*\\(\\s*(\\w+)\\s*\\)", 0);
                        result = in.match();
                        recipe.setCraftStation(result.group(1).replaceAll("Object", ""));

                    } catch (Exception e) {
                        if (recipeFiles != null) {
                            for (File recipeFile : recipeFiles) {
                                if (recipeFile.isFile()) {
                                    try {
                                        Scanner inRecipe = new Scanner(new FileReader(recipeFile.getPath()));
                                        // new CraftingElement<VegetableMedleyItem>(1)
                                        inRecipe.findWithinHorizon("new\\s*CraftingElement\\s*<\\s*(.+)Item\\s*>\\(", 0);
                                        MatchResult resultRecipe = inRecipe.match();
                                        String resultName = resultRecipe.group(1);
                                        if (recipe.getName().replaceAll("\\s", "").equals(resultName)) {
                                            System.out.println(recipe.getName() + " found");
                                            inRecipe = new Scanner(new FileReader(recipeFile.getPath()));
                                            try {
                                                inRecipe.findWithinHorizon("\\[\\s*RequiresSkill\\s*\\(\\s*typeof\\s*\\(\\s*(\\w+)\\s*\\)\\s*,\\s*(\\d+)\\s*\\)\\s*]", 0);
                                                result = inRecipe.match();
                                                recipe.setSkill(result.group(1).replaceAll("Skill", ""));
                                                recipe.setSkillLvl(Integer.parseInt(result.group(2)));
                                            } catch (Exception e3) {
                                                recipe.setSkill("None");
                                                recipe.setSkillLvl(0);
                                            }

                                            inRecipe.findWithinHorizon("CraftingComponent\\.AddRecipe\\s*\\(\\s*tableType\\s*:\\s*typeof\\s*\\(\\s*(\\w+)\\s*\\)", 0);
                                            result = inRecipe.match();
                                            recipe.setCraftStation(result.group(1).replaceAll("Object", ""));
                                            break;
                                        }
                                    } catch (Exception e2) {
                                        System.out.println("Failed to parse crafting details: " + file.getName());
                                        recipe.setSkill("");
                                        recipe.setSkillLvl(0);
                                        recipe.setCraftStation("");
                                    }
                                }
                            }
                        }
                    }

                    // System.out.println(recipe.toString());
                    recipe.setCount(0);
                    recipe.setTastiness(-1);
                    recipeList.add(recipe);

                    in.close();
                } catch (Exception e) {
                    System.out.println("Failed to parse " + file.getName());
                }
            }
        }


    }

    public void onFilterChange() {
        // TODO: can be in initialization?
        /*recipeFilteredList.setPredicate(recipe -> switch (recipe.getSkill()) {
            case "CampfireCooking" -> (recipe.getSkillLvl() == 0 && cbCharred.isSelected()) || (recipe.getSkillLvl() != 0 && cbCampfireCooking.isSelected() && spCampfireCooking.getValue() >= recipe.getSkillLvl());
            case "Baking" -> recipe.getSkillLvl() == 0 || (cbBaking.isSelected() && spBaking.getValue() >= recipe.getSkillLvl());
            case "AdvancedBaking" -> recipe.getSkillLvl() == 0 || (cbAdvBaking.isSelected() && spAdvBaking.getValue() >= recipe.getSkillLvl());
            case "Cooking" -> recipe.getSkillLvl() == 0 || (cbCooking.isSelected() && spCooking.getValue() >= recipe.getSkillLvl());
            case "AdvancedCooking" -> recipe.getSkillLvl() == 0 || (cbAdvCooking.isSelected() && spAdvCooking.getValue() >= recipe.getSkillLvl());
            case "Milling" -> recipe.getSkillLvl() == 0 || (cbMilling.isSelected() && spMilling.getValue() >= recipe.getSkillLvl());
            case "Gathering", "Butchery" -> cbRaw.isSelected();
            case "Hidden" -> cbHidden.isSelected();
            default -> true;
        });*/
        recipeFilteredList.setPredicate(recipe -> {
            if (recipe.getTastiness() == -1 && rbTastedYes.isSelected())
                return false;
            if (recipe.getTastiness() != -1 && rbTastedNo.isSelected())
                return false;
            return switch (recipe.getSkill()) {
                case "CampfireCooking" ->
                        (recipe.getSkillLvl() == 0 && cbCharred.isSelected()) || (recipe.getSkillLvl() != 0 && cbCampfireCooking.isSelected() && spCampfireCooking.getValue() >= recipe.getSkillLvl());
                case "Baking" -> cbBaking.isSelected() && spBaking.getValue() >= recipe.getSkillLvl();
                case "AdvancedBaking" -> cbAdvBaking.isSelected() && spAdvBaking.getValue() >= recipe.getSkillLvl();
                case "Cooking" -> cbCooking.isSelected() && spCooking.getValue() >= recipe.getSkillLvl();
                case "AdvancedCooking" -> cbAdvCooking.isSelected() && spAdvCooking.getValue() >= recipe.getSkillLvl();
                case "Milling" -> cbMilling.isSelected() && spMilling.getValue() >= recipe.getSkillLvl();
                case "Gathering", "Butchery", "None" -> cbRaw.isSelected();
                case "Hidden", "" -> cbHidden.isSelected();
                default -> true;
            };
        });
    }

    public Meal updateMeal() {

        ObservableList<FoodRecipe> recipes = FXCollections.observableArrayList();
/*
        int totalCalories=0;
        double meanCarbs=0, meanFats=0, meanProteins=0, meanVitamins=0, tastiness=0, variety=0;
        String tools = "", skills = "", ingredients = "";

        for (FoodRecipe recipe : mealFilteredList){
            totalCalories += recipe.getCount() * recipe.getCalories();

            meanCarbs += recipe.getCount() * recipe.getCarbs() * recipe.getCalories();
            meanFats += recipe.getCount() * recipe.getFats() * recipe.getCalories();
            meanProteins += recipe.getCount() * recipe.getProteins() * recipe.getCalories();
            meanVitamins += recipe.getCount() * recipe.getVitamins() * recipe.getCalories();
            tastiness += recipe.getCount() * recipe.getTastiness() * recipe.getCalories() / 100.0f;

            if (!recipe.getSkill().isBlank()) {
                Pattern pattern = Pattern.compile(recipe.getSkill() + "\\s(\\d+)");
                Matcher matcher = pattern.matcher(skills);
                if (matcher.find()) {
                    if (Integer.parseInt(matcher.group(1)) < recipe.getSkillLvl())
                        skills = skills.substring(0, matcher.start(1)) + recipe.getSkillLvl() + skills.substring(matcher.end(1));
                } else
                    skills += recipe.getSkill() + " " + recipe.getSkillLvl() + "\n";
            }

            ingredients += recipe.getCount() + "x " + recipe.getName() + "\n";
            variety++;
            recipes.add(new FoodRecipe(recipe));
        }

        String survivalSkillLvl = "0";
        if (totalCalories>4250)
            survivalSkillLvl = "-";
        else if (totalCalories>4000)
            survivalSkillLvl = "7";
        else if (totalCalories>3750)
            survivalSkillLvl = "6";
        else if (totalCalories>3500)
            survivalSkillLvl = "5";
        else if (totalCalories>3250)
            survivalSkillLvl = "4";
        else if (totalCalories>3125)
            survivalSkillLvl = "3";
        else if (totalCalories>3000)
            survivalSkillLvl = "2";

        if(totalCalories > 0) {
            meanCarbs /= totalCalories;
            meanFats /= totalCalories;
            meanProteins /= totalCalories;
            meanVitamins /= totalCalories;
            tastiness /= totalCalories;
        }
        double maxMeanNutrient = Math.max(Math.max(Math.max(meanCarbs, meanFats), meanProteins), meanVitamins);
        double minMeanNutrient = Math.min(Math.min(Math.min(meanCarbs, meanFats), meanProteins), meanVitamins);
        double totalMeanNutrients = meanCarbs+meanFats+meanProteins+meanVitamins;
        double balanceMultiplier = -0.5f;
        if (maxMeanNutrient > 0)
            balanceMultiplier += minMeanNutrient/maxMeanNutrient;
*/
//        int baseGain = 0;
//        if (!textBaseGain.getText().isBlank())
//            baseGain = Integer.parseInt(textBaseGain.getText());
//        else textBaseGain.setText("0");
//        int skillMultiplier = 0;
//        if (!textSkillMultiplier.getText().isBlank())
//            skillMultiplier = Integer.parseInt(textSkillMultiplier.getText());
//        else textSkillMultiplier.setText("0");

        double baseGain = getBaseGain();
        double skillMultiplier = getSkillMultiplier();

        // new
        for (FoodRecipe recipe : mealFilteredList) recipes.add(new FoodRecipe(recipe));
        Meal meal = new Meal();
        meal.setName("Name");
        meal.setCount(1);
        meal.setRecipes(recipes);
        meal.updateSkillPoints((int) baseGain,skillMultiplier,0,0);

//        double skillPoints = ((totalMeanNutrients * (1.0f+balanceMultiplier) * (1+tastiness)) + baseGain) * skillMultiplier;

//        labelSurvivalSkillLvl.setText(survivalSkillLvl);
//        labelCalories.setText(Integer.toString(totalCalories));
//        labelCarbs.setText(String.format("%.1f",meanCarbs));
//        labelFats.setText(String.format("%.1f",meanFats));
//        labelProteins.setText(String.format("%.1f",meanProteins));
//        labelVitamins.setText(String.format("%.1f",meanVitamins));
//        labelNutrients.setText(String.format("%.1f",totalMeanNutrients));
//        labelBalanceMultiplier.setText(String.format("%.2f",balanceMultiplier*100)+"%");
//        labelTastiness.setText(String.format("%.2f",tastiness*100)+"%");
//        labelVariety.setText(String.format("%.2f",variety*100)+"%");
//        labelSkillPoints.setText(String.format("%.1f",skillPoints));
//
//        pieNutrients.getData().get(0).setPieValue(meanCarbs);
//        pieNutrients.getData().get(1).setPieValue(meanProteins);
//        pieNutrients.getData().get(2).setPieValue(meanFats);
//        pieNutrients.getData().get(3).setPieValue(meanVitamins);

        // altered
        labelCalories.setText(Integer.toString(meal.getTotalCalories()));
        labelCarbs.setText(String.format("%.1f",meal.getTotalCarbs()));
        labelFats.setText(String.format("%.1f",meal.getTotalFats()));
        labelProteins.setText(String.format("%.1f",meal.getTotalProteins()));
        labelVitamins.setText(String.format("%.1f",meal.getTotalVitamins()));
        labelNutrients.setText(String.format("%.1f",meal.getTotalCarbs()+meal.getTotalFats()+meal.getTotalProteins()+meal.getTotalVitamins()));
        labelBalanceMultiplier.setText(String.format("%.2f",meal.getBalanceMultiplier()*100)+"%");
        labelTastiness.setText(String.format("%.2f",meal.getTastinessMultiplier()*100)+"%");
        labelVariety.setText(String.format("%.2f",meal.getVarietyMultiplier()*100)+"%");
        labelSkillPoints.setText(String.format("%.1f",meal.getSkillPoints()));

        pieNutrients.getData().get(0).setPieValue(meal.getTotalCarbs());
        pieNutrients.getData().get(1).setPieValue(meal.getTotalProteins());
        pieNutrients.getData().get(2).setPieValue(meal.getTotalFats());
        pieNutrients.getData().get(3).setPieValue(meal.getTotalVitamins());


        recipeTable.sort();

        //return new Meal("Name", skillPoints, balanceMultiplier, tastiness, variety, skills, ingredients, tools, survivalSkillLvl, totalCalories, meanCarbs, meanFats, meanProteins, meanVitamins, recipes);
        return meal;
    }

    public void onReleaseRecipe()
    {
        if (recipeTable.getSelectionModel().getSelectedCells().isEmpty()) return;
        if(colTastiness != recipeTable.getSelectionModel().getSelectedCells().get(0).getTableColumn())
            recipeTable.getSelectionModel().clearSelection();
    }

    public void onReleaseMeal() {
        if(mealTable.getSelectionModel() == null) return;
        mealTable.getSelectionModel().clearSelection();
    }

    public void onPressMealList() {
        if (mealListTable.getSelectionModel().getSelectedItem() == null) return;
        Meal meal = mealListTable.getSelectionModel().getSelectedItem();
        meal.setCount(meal.getCount()+1);
        updateDiet();
    }

    public void onPressRecipe() {
        if (recipeTable.getSelectionModel().getSelectedItem() == null) return;
        final FoodRecipe recipe = recipeTable.getSelectionModel().getSelectedItem();
        if(colTastiness == recipeTable.getSelectionModel().getSelectedCells().get(0).getTableColumn()) return;
        //System.out.println("Tastiness: " + recipe.getTastiness());
        recipe.setCount(recipe.getCount()+1);
        recipeTable.refresh();
        mealTable.refresh();
        updateMeal();
    }

    public void onPressMeal() {
        if (tabsFood.getSelectionModel().getSelectedIndex()==0) {
            if (mealTable.getSelectionModel().getSelectedItem() == null) return;
            final FoodRecipe recipe = mealTable.getSelectionModel().getSelectedItem();
//          System.out.println("Selection changed(meal): " + recipe);
            recipe.setCount(recipe.getCount() - 1);
            recipeTable.refresh();
            mealTable.refresh();
            updateMeal();
        } else if (tabsFood.getSelectionModel().getSelectedIndex()==1) {
            if (mealTable.getSelectionModel().getSelectedItem() == null) return;
            Meal meal = mealTable.getSelectionModel().getSelectedItem().getEmulationSource();
            meal.setCount(meal.getCount() - 1);
            updateDiet();
            mealListTable.refresh();
            mealTable.refresh();
        }
    }


    public void onButtonCreateEdit() {
        // create
        if (tabsFood.getSelectionModel().getSelectedIndex() == 0) {
            Meal meal = updateMeal();
            String mealName;

            TextInputDialog dialog = new TextInputDialog("Meal");
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
            mealList.add(new Meal(meal));
            tabsFood.getSelectionModel().select(1);
        }
        // edit
        else if (tabsFood.getSelectionModel().getSelectedIndex() == 1) {
            Meal meal = mealListTable.getSelectionModel().getSelectedItem();
            if (meal == null) return;

            tabsFood.getSelectionModel().select(0);
            onButtonResetDelete();

            for (FoodRecipe recipe : meal.getRecipes()) {
                int index = recipeList.indexOf(recipe);
                if (index == -1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Can't Find Food Item");
                    alert.setHeaderText(null);
                    alert.setContentText("Couldn't find the item " + recipe.getName() + "!");
                    alert.showAndWait();
                    break;
                }
                recipeList.get(index).setCount(recipe.getCount());
            }
            recipeTable.refresh();
            mealTable.refresh();
            updateMeal();
        }
    }

    public void onButtonResetDelete() {
        if (tabsFood.getSelectionModel().getSelectedIndex()==0){
            for (FoodRecipe recipe : recipeList)
                recipe.setCount(0);
            updateMeal();
        } else if (tabsFood.getSelectionModel().getSelectedIndex()==1){
            Meal meal = mealListTable.getSelectionModel().getSelectedItem();
            if (meal == null) return;
            mealList.removeAll(meal);
            updateDiet();
        }
    }

    public void onButtonSearch() {
        if (tabsFood.getSelectionModel().getSelectedIndex() != 0) return;
        Meal meal = updateMeal();

        if (meal.getRecipes().size() > 50) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Too many food items");
            alert.setContentText("You can have a maximum of 50 different food items selected.");
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MealGeneratorWindow.fxml"));
        Parent rootGenerator;
        try {
            rootGenerator = loader.load();
        } catch (Exception e) {
            System.out.println("Failed to load MealGeneratorWindow.fxml");
            return;
        }
        MealGeneratorController controllerGenerator = loader.getController();
        controllerGenerator.setRecipes(meal.getRecipes());
        controllerGenerator.setParent(this);
        Scene sceneGenerator = new Scene(rootGenerator);

        Stage stageGenerator = new Stage();
        stageGenerator.setTitle("Meal Generator");
        stageGenerator.initModality(Modality.APPLICATION_MODAL);
        stageGenerator.setScene(sceneGenerator);
        stageGenerator.setOnCloseRequest(e -> controllerGenerator.cancelTask());
        stageGenerator.showAndWait();
    }

    public void onSave(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        File file = chooser.showSaveDialog(((MenuItem)actionEvent.getSource()).getParentPopup().getScene().getWindow());
        if (file != null) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                out.writeObject(new ArrayList<>(recipeList));
                out.writeObject(new ArrayList<>(mealList));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onLoad() {
        FileChooser chooser = new FileChooser();
//        ((MenuItem)actionEvent.getSource()).getParentPopup().getScene().getWindow()
        File file = chooser.showOpenDialog(recipeTable.getScene().getWindow());
        if (file != null) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                List<FoodRecipe> recipes = (List<FoodRecipe>) in.readObject();
                recipeList.setAll(recipes);
                List<Meal> meals = (List<Meal>) in.readObject();
                mealList.setAll(meals);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onAddBest() {
        Object[] options = {10, 15, 20, 25, 30, 35, 40, 45, 50};
        int noItems;
        try {
            noItems = (Integer) (JOptionPane.showInputDialog(null, "How many of the best food items you want to add?\n(will respect selected filters and clear the current meal)", "Add best", JOptionPane.QUESTION_MESSAGE, null, options, options[4]));
        } catch (Exception e) {
            return;
        }
        if (tabsFood.getSelectionModel().getSelectedIndex() == 0) {
            for (FoodRecipe recipe : recipeList) {
                recipe.setCount(0);
            }
            colNutrients.setSortType(TableColumn.SortType.DESCENDING);
            recipeTable.getSortOrder().clear();
            recipeTable.getSortOrder().add(colNutrients);
            if (recipeSortedList.size() < noItems) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Can't Add Best");
                alert.setHeaderText("Too few food items in the current filtered list");
                alert.setContentText("Only could add " + recipeSortedList.size() + "/" + noItems + " items! Try changing the filters.");
                alert.showAndWait();
                noItems = recipeSortedList.size();
            }
            for (int i = 0; i < noItems; i++) {
                recipeSortedList.get(i).setCount(1);
            }

            updateMeal();
        }
        System.out.println("add best: " + noItems);
    }

    public double getBaseGain() {
        int baseGain = 0;
        if (!textBaseGain.getText().isBlank())
            baseGain = Integer.parseInt(textBaseGain.getText());
        else textBaseGain.setText("0");
        return baseGain;
    }

    public double getSkillMultiplier() {
        int skillMultiplier = 0;
        if (!textSkillMultiplier.getText().isBlank())
            skillMultiplier = Integer.parseInt(textSkillMultiplier.getText());
        else textSkillMultiplier.setText("0");
        return skillMultiplier;
    }

    public void addMeal(Meal meal) {
        if (meal!=null)
            mealList.add(meal);
        else
            System.out.println("Cant add null to meallist");
    }

    public void emulateMealList() {
        emulatedMealListFilteredList.clear();
        for (Meal meal: mealListSortedList) {
            FoodRecipe recipe = new FoodRecipe();
            recipe.setCount(meal.getCount());
            recipe.setName(meal.getName());
            recipe.setCalories(meal.getTotalCalories());
            recipe.setTastiness(meal.getTastinessMultiplier());
            recipe.setCarbs(meal.getTotalCarbs());
            recipe.setFats(meal.getTotalFats());
            recipe.setProteins(meal.getTotalProteins());
            recipe.setVitamins(meal.getTotalVitamins());
            recipe.calcNutrients();
            recipe.setEmulationSource(meal);

            emulatedMealListFilteredList.add(recipe);
        }
    }

    public void updateDiet() {
        ObservableList<FoodRecipe> mealsAsRecipes = FXCollections.observableArrayList();
        double baseGain = getBaseGain();
        double skillMultiplier = getSkillMultiplier();

        emulateMealList();
        for (FoodRecipe recipe : emulatedMealListFilteredList) mealsAsRecipes.add(new FoodRecipe(recipe));
        Meal diet = new Meal();
        diet.setName("Diet");
        diet.setCount(1);
        diet.setRecipes(mealsAsRecipes);
        // TODO: add cravings
        diet.updateSkillPoints((int) baseGain, skillMultiplier, calcVarietyModifier(), 0.3f);

        labelCalories.setText(Integer.toString(diet.getTotalCalories()));
        labelCarbs.setText(String.format("%.1f", diet.getTotalCarbs()));
        labelFats.setText(String.format("%.1f", diet.getTotalFats()));
        labelProteins.setText(String.format("%.1f", diet.getTotalProteins()));
        labelVitamins.setText(String.format("%.1f", diet.getTotalVitamins()));
        labelNutrients.setText(String.format("%.1f", diet.getTotalCarbs() + diet.getTotalFats() + diet.getTotalProteins() + diet.getTotalVitamins()));
        labelBalanceMultiplier.setText(String.format("%.2f", diet.getBalanceMultiplier() * 100) + "%");
        labelTastiness.setText(String.format("%.2f", diet.getTastinessMultiplier() * 100) + "%");
        labelVariety.setText(String.format("%.2f", diet.getVarietyMultiplier() * 100) + "% (" + countNumUniqueRecipes() + ")");
        labelSkillPoints.setText(String.format("%.1f", diet.getSkillPoints()));

        pieNutrients.getData().get(0).setPieValue(diet.getTotalCarbs());
        pieNutrients.getData().get(1).setPieValue(diet.getTotalProteins());
        pieNutrients.getData().get(2).setPieValue(diet.getTotalFats());
        pieNutrients.getData().get(3).setPieValue(diet.getTotalVitamins());
    }

    public double calcVarietyModifier() {
        int numUniqueRecipes = countNumUniqueRecipes();

        double varietyMultiplier = (double)numUniqueRecipes * 25.0f/2000.0f;
        if (numUniqueRecipes > 20)
            varietyMultiplier = (25.0f + 0.12f + 0.381f * (double)(numUniqueRecipes-20))/100.0f;
        //System.out.println(uniqueRecipes.size() + " -> " + varietyMultiplier);
        return varietyMultiplier;
    }

    public int countNumUniqueRecipes () {
        ObservableList<FoodRecipe> uniqueRecipes = FXCollections.observableArrayList();
        boolean bFound = false;
        for (Meal meal : mealListSortedList) {
            for (FoodRecipe recipe : meal.getRecipes()) {
                bFound = false;
                for (FoodRecipe ur : uniqueRecipes)
                    if (recipe.getName().equals(ur.getName())) {
                        bFound = true;
                        break;
                    }
                if (!bFound)
                    uniqueRecipes.add(recipe);
            }
        }
        return uniqueRecipes.size();
    }
}
