package EcoFood;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Meal implements Serializable {

    private transient SimpleStringProperty name;
    private transient SimpleIntegerProperty count;
    private transient SimpleDoubleProperty skillPoints;
    private transient SimpleDoubleProperty balanceMultiplier;
    private transient SimpleDoubleProperty tastinessMultiplier;
    private transient SimpleDoubleProperty varietyMultiplier;
    private transient SimpleStringProperty skills;
    private transient SimpleStringProperty ingredients;
    private transient SimpleStringProperty craftStations;
    private transient SimpleStringProperty survivalSkill;
    private transient SimpleIntegerProperty totalCalories;
    private transient SimpleDoubleProperty totalCarbs;
    private transient SimpleDoubleProperty totalFats;
    private transient SimpleDoubleProperty totalProteins;
    private transient SimpleDoubleProperty totalVitamins;
    private transient ObservableList<FoodRecipe> recipes;

    public int getCount() {
        return count.get();
    }

    public SimpleIntegerProperty countProperty() {
        return count;
    }

    public void setCount(int count) {
        this.count.set(count);
    }

    public double getTastinessMultiplier() {
        return tastinessMultiplier.get();
    }

    public SimpleDoubleProperty tastinessMultiplierProperty() {
        return tastinessMultiplier;
    }

    public void setTastinessMultiplier(double tastinessMultiplier) {
        this.tastinessMultiplier.set(tastinessMultiplier);
    }

    public double getVarietyMultiplier() {
        return varietyMultiplier.get();
    }

    public SimpleDoubleProperty varietyMultiplierProperty() {
        return varietyMultiplier;
    }

    public void setVarietyMultiplier(double varietyMultiplier) {
        this.varietyMultiplier.set(varietyMultiplier);
    }

    public int getTotalCalories() {
        return totalCalories.get();
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories.set(totalCalories);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getSkillPoints() {
        return skillPoints.get();
    }

    public void setSkillPoints(double skillPoints) {
        this.skillPoints.set(skillPoints);
    }

    public double getBalanceMultiplier() {
        return balanceMultiplier.get();
    }

    public void setBalanceMultiplier(double balanceMultiplier) { this.balanceMultiplier.set(balanceMultiplier); }

    public String getSkills() {
        return skills.get();
    }

    public void setSkills(String skills) {
        this.skills.set(skills);
    }

    public String getIngredients() {
        return ingredients.get();
    }

    public void setIngredients(String ingredients) {
        this.ingredients.set(ingredients);
    }

    public String getCraftStations() {
        return craftStations.get();
    }

    public void setCraftStations(String craftStations) {
        this.craftStations.set(craftStations);
    }

    public String getSurvivalSkill() {
        return survivalSkill.get();
    }

    public void setSurvivalSkill(String survivalSkill) {
        this.survivalSkill.set(survivalSkill);
    }

    public double getTotalCarbs() {
        return totalCarbs.get();
    }

    public void setTotalCarbs(double totalCarbs) {
        this.totalCarbs.set(totalCarbs);
    }

    public double getTotalFats() {
        return totalFats.get();
    }

    public void setTotalFats(double totalFats) {
        this.totalFats.set(totalFats);
    }

    public double getTotalProteins() {
        return totalProteins.get();
    }

    public void setTotalProteins(double totalProteins) {
        this.totalProteins.set(totalProteins);
    }

    public double getTotalVitamins() {
        return totalVitamins.get();
    }

    public void setTotalVitamins(double totalVitamins) {
        this.totalVitamins.set(totalVitamins);
    }

    public ObservableList<FoodRecipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ObservableList<FoodRecipe> recipes) {
        this.recipes = recipes;
    }

    public Meal(String name, int count, double skillPoints, double balanceMultiplier, double tastinessMultiplier, double varietyMultiplier, String skills, String ingredients, String craftStations, String survivalSkill, int totalCalories, double totalCarbs, double totalFats, double totalProteins, double totalVitamins, ObservableList<FoodRecipe> recipes) {
        this.name = new SimpleStringProperty(name);
        this.count = new SimpleIntegerProperty(count);
        this.skillPoints = new SimpleDoubleProperty(skillPoints);
        this.balanceMultiplier = new SimpleDoubleProperty(balanceMultiplier);
        this.tastinessMultiplier = new SimpleDoubleProperty(tastinessMultiplier);
        this.varietyMultiplier = new SimpleDoubleProperty(varietyMultiplier);
        this.skills = new SimpleStringProperty(skills);
        this.ingredients = new SimpleStringProperty(ingredients);
        this.craftStations = new SimpleStringProperty(craftStations);
        this.survivalSkill = new SimpleStringProperty(survivalSkill);
        this.totalCalories = new SimpleIntegerProperty(totalCalories);
        this.totalCarbs = new SimpleDoubleProperty(totalCarbs);
        this.totalFats = new SimpleDoubleProperty(totalFats);
        this.totalProteins = new SimpleDoubleProperty(totalProteins);
        this.totalVitamins = new SimpleDoubleProperty(totalVitamins);
        this.recipes = recipes;
    }

    public Meal(Meal meal) {
        this.name = new SimpleStringProperty(meal.getName());
        this.count = new SimpleIntegerProperty(meal.getCount());
        this.skillPoints = new SimpleDoubleProperty(meal.getSkillPoints());
        this.balanceMultiplier = new SimpleDoubleProperty(meal.getBalanceMultiplier());
        this.tastinessMultiplier = new SimpleDoubleProperty(meal.getTastinessMultiplier());
        this.varietyMultiplier = new SimpleDoubleProperty(meal.getVarietyMultiplier());
        this.skills = new SimpleStringProperty(meal.getSkills());
        this.ingredients = new SimpleStringProperty(meal.getIngredients());
        this.craftStations = new SimpleStringProperty(meal.getCraftStations());
        this.survivalSkill = new SimpleStringProperty(meal.getSurvivalSkill());
        this.totalCalories = new SimpleIntegerProperty(meal.getTotalCalories());
        this.totalCarbs = new SimpleDoubleProperty(meal.getTotalCarbs());
        this.totalFats = new SimpleDoubleProperty(meal.getTotalFats());
        this.totalProteins = new SimpleDoubleProperty(meal.getTotalProteins());
        this.totalVitamins = new SimpleDoubleProperty(meal.getTotalVitamins());
        ObservableList<FoodRecipe> recipes = FXCollections.observableArrayList();
        for (FoodRecipe recipe : meal.getRecipes()) recipes.add(new FoodRecipe(recipe));
        this.recipes = recipes;
    }

    public Meal() {
        this.name = new SimpleStringProperty("");
        this.count = new SimpleIntegerProperty(-1);
        this.skillPoints = new SimpleDoubleProperty(-1);
        this.balanceMultiplier = new SimpleDoubleProperty(-1);
        this.tastinessMultiplier = new SimpleDoubleProperty(-1);
        this.varietyMultiplier = new SimpleDoubleProperty(-1);
        this.skills = new SimpleStringProperty("");
        this.ingredients = new SimpleStringProperty("");
        this.craftStations = new SimpleStringProperty("");
        this.survivalSkill = new SimpleStringProperty("");
        this.totalCalories = new SimpleIntegerProperty(-1);
        this.totalCarbs = new SimpleDoubleProperty(-1);
        this.totalFats = new SimpleDoubleProperty(-1);
        this.totalProteins = new SimpleDoubleProperty(-1);
        this.totalVitamins = new SimpleDoubleProperty(-1);
        this.recipes = FXCollections.observableArrayList();
    }

    public void updateSkillPoints(int baseGain, double skillMultiplier, double varietyMultiplier, double cravingMultiplier) {

        int totalCalories=0;
        double meanCarbs=0, meanFats=0, meanProteins=0, meanVitamins=0, tastiness=0;
        String craftStations = "", skills = "", ingredients = "";

        for (FoodRecipe recipe : recipes){
            totalCalories += recipe.getCount() * recipe.getCalories();

            meanCarbs += recipe.getCount() * recipe.getCarbs() * recipe.getCalories();
            meanFats += recipe.getCount() * recipe.getFats() * recipe.getCalories();
            meanProteins += recipe.getCount() * recipe.getProteins() * recipe.getCalories();
            meanVitamins += recipe.getCount() * recipe.getVitamins() * recipe.getCalories();
            if (recipe.getEmulationSource() == null)
                tastiness += recipe.getCount() * recipe.getTastiness() * recipe.getCalories() / 100.0f;
            else
                tastiness += recipe.getCount() * recipe.getTastiness() * recipe.getCalories();

            if (!recipe.getSkill().matches("\\s*")) {
                Pattern pattern = Pattern.compile(recipe.getSkill() + "\\s(\\d+)");
                Matcher matcher = pattern.matcher(skills);
                if (matcher.find()) {
                    if (Integer.parseInt(matcher.group(1)) < recipe.getSkillLvl())
                        skills = skills.substring(0, matcher.start(1)) + recipe.getSkillLvl() + skills.substring(matcher.end(1));
                } else
                    skills += recipe.getSkill() + " " + recipe.getSkillLvl() + "\n";
            }

            ingredients += recipe.getCount() + "x " + recipe.getName() + "\n";
        }

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

        double skillPoints = ((totalMeanNutrients * (1.0f+balanceMultiplier) * (1.0f+tastiness)*(1+varietyMultiplier)*(1+cravingMultiplier)) + baseGain) * skillMultiplier;

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

        this.balanceMultiplier.set(balanceMultiplier);
        this.craftStations.set(craftStations);
        this.ingredients.set(ingredients);
        this.skillPoints.set(skillPoints);
        this.skills.set(skills);
        this.survivalSkill.set(survivalSkillLvl);
        this.tastinessMultiplier.set(tastiness);
        this.totalCalories.set(totalCalories);
        this.totalCarbs.set(meanCarbs);
        this.totalFats.set(meanFats);
        this.totalProteins.set(meanProteins);
        this.totalVitamins.set(meanVitamins);
        this.varietyMultiplier.set(varietyMultiplier);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeObject(getName());
        s.writeInt(getCount());
        s.writeDouble(getSkillPoints());
        s.writeDouble(getBalanceMultiplier());
        s.writeDouble(getTastinessMultiplier());
        s.writeDouble(getVarietyMultiplier());
        s.writeObject(getSkills());
        s.writeObject(getIngredients());
        s.writeObject(getCraftStations());
        s.writeObject(getSurvivalSkill());
        s.writeInt(getTotalCalories());
        s.writeDouble(getTotalCarbs());
        s.writeDouble(getTotalFats());
        s.writeDouble(getTotalProteins());
        s.writeDouble(getTotalVitamins());
        s.writeObject(new ArrayList<>(recipes));
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        name = new SimpleStringProperty((String)s.readObject());
        count = new SimpleIntegerProperty(s.readInt());
        skillPoints = new SimpleDoubleProperty(s.readDouble());
        balanceMultiplier = new SimpleDoubleProperty(s.readDouble());
        tastinessMultiplier = new SimpleDoubleProperty(s.readDouble());
        varietyMultiplier = new SimpleDoubleProperty(s.readDouble());
        skills = new SimpleStringProperty((String)s.readObject());
        ingredients = new SimpleStringProperty((String)s.readObject());
        craftStations = new SimpleStringProperty((String)s.readObject());
        survivalSkill = new SimpleStringProperty((String)s.readObject());
        totalCalories = new SimpleIntegerProperty(s.readInt());
        totalCarbs = new SimpleDoubleProperty(s.readDouble());
        totalFats = new SimpleDoubleProperty(s.readDouble());
        totalProteins = new SimpleDoubleProperty(s.readDouble());
        totalVitamins = new SimpleDoubleProperty(s.readDouble());
        List<FoodRecipe> recipeList = (List<FoodRecipe>) s.readObject();
        recipes = FXCollections.observableArrayList(recipeList);
    }
}
