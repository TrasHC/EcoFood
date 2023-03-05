package EcoFood;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;

import java.util.Comparator;

public class GeneratorTask extends Task<Void> {
    private ObservableList<FoodRecipe> recipes = FXCollections.observableArrayList();
    public ObservableList<Meal> meals = FXCollections.observableArrayList();
    private SortedList<Meal> mealsSortedList = null;
    public ObservableList<Meal> balancedMeals = FXCollections.observableArrayList();
    private final double baseGain;
    private final double skillMultiplier;
    private final long numCalculations;
    private final int numRecipes;
    private final int numItemsPerMeal;
    private long numProcessed;


    public GeneratorTask(ObservableList<FoodRecipe> recipes, double baseGain, double skillMultiplier, long numCalculations, int numRecipes, int numItemsPerMeal) {
        this.baseGain = baseGain;
        this.skillMultiplier = skillMultiplier;
        this.numCalculations = numCalculations;
        this.numRecipes = numRecipes;
        this.numItemsPerMeal = numItemsPerMeal;
        this.numProcessed = 0;
        for(FoodRecipe recipe: recipes) this.recipes.add(new FoodRecipe(recipe));
        mealsSortedList = new SortedList<>(meals.filtered(p -> true));
        mealsSortedList.setComparator(new Comparator<Meal>() {
            @Override
            public int compare(Meal o1, Meal o2) {
                if(o1.getSkillPoints() - o2.getSkillPoints() < 0)
                    return -1;
                else if(o1.getSkillPoints() - o2.getSkillPoints() > 0)
                    return 1;
                else
                    return 0;
            }
        });
        mealsSortedList.setComparator(mealsSortedList.getComparator().reversed());
        //colSkillPointsMealList.setSortType(TableColumn.SortType.DESCENDING);
    }

    @Override
    protected Void call() throws Exception {
        int[] chosen = new int[numRecipes];
        for (int i = 0; i<numRecipes; i++)
            chosen[i] = 0;

        combinationRepetition(chosen,0,numItemsPerMeal,0,numRecipes-1);

        for (int i = 0; i < balancedMeals.size(); i++) {
            int index = meals.indexOf(balancedMeals.get(i));
            if(index < 0) {
                balancedMeals.get(i).setName("Balanced"+ (i+1));
                meals.add(balancedMeals.get(i));
            }
            else
                meals.get(index).setName("Balanced"+ (i+1));
        }
        return null;
    }

    @Override protected void succeeded() {
        super.succeeded();
        updateMessage("Done!");
    }

    @Override protected void cancelled() {
        super.cancelled();
        updateMessage("Cancelled!");
    }

    @Override protected void failed() {
        super.failed();
        updateMessage("Failed!");
    }

    private void combinationRepetition(int[] chosen, int index, int r, int start, int end) {
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
            // TODO user input
            int maxMeals = 10;
            Meal meal = new Meal();
            meal.setName(name.toString());
            meal.setRecipes(recipes);
            meal.updateSkillPoints((int) baseGain,skillMultiplier,0,0);
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
            //else System.out.println("discard: " + meal.getSkillPoints());

            numProcessed++;
            if (numProcessed%100000 == 0)
                this.updateProgress(numProcessed, numCalculations);
            return;
        }
        for (int i = start; i <= end; i++) {
            if (isCancelled())
                break;
            chosen[i]++;
            combinationRepetition(chosen, index + 1, r, i, end);
            chosen[i]--;
        }
    }


}
