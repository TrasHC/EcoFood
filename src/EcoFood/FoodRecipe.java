package EcoFood;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class FoodRecipe implements Serializable {

    public enum Tastiness
    {
        DISGUSTING(-30),
        HORRIBLE(-20),
        BAD(-10),
        OK(0),
        GOOD(10),
        DELICIOUS(20),
        FAVOURITE(30);
        public final int modifier;
        Tastiness(int modifier) {this.modifier = modifier;}
    }
    private transient SimpleIntegerProperty count;
    private transient SimpleStringProperty name;
    private transient SimpleIntegerProperty calories;
    private transient SimpleDoubleProperty carbs;
    private transient SimpleDoubleProperty proteins;
    private transient SimpleDoubleProperty fats;
    private transient SimpleDoubleProperty vitamins;
    private transient SimpleDoubleProperty nutrients;
    private transient SimpleStringProperty skill;
    private transient SimpleIntegerProperty skillLvl;
    private transient SimpleStringProperty craftStation;
    private transient SimpleDoubleProperty tastiness;
    private Meal emulationSource;

    public FoodRecipe(int count, String name, int calories, double carbs, double proteins, double fats, double vitamins, double nutrients, String skill, int skillLvl, String craftStation, double tastiness) {
        this.count = new SimpleIntegerProperty(count);
        this.name = new SimpleStringProperty(name);
        this.calories = new SimpleIntegerProperty(calories);
        this.carbs = new SimpleDoubleProperty(carbs);
        this.proteins = new SimpleDoubleProperty(proteins);
        this.fats = new SimpleDoubleProperty(fats);
        this.vitamins = new SimpleDoubleProperty(vitamins);
        this.nutrients = new SimpleDoubleProperty(nutrients);
        this.skill = new SimpleStringProperty(skill);
        this.skillLvl = new SimpleIntegerProperty(skillLvl);
        this.craftStation = new SimpleStringProperty(craftStation);
        this.tastiness = new SimpleDoubleProperty(tastiness);
        this.emulationSource = null;
    }

    public FoodRecipe(FoodRecipe recipe) {
        this.count = new SimpleIntegerProperty(recipe.getCount());
        this.name = new SimpleStringProperty(recipe.getName());
        this.calories = new SimpleIntegerProperty(recipe.getCalories());
        this.carbs = new SimpleDoubleProperty(recipe.getCarbs());
        this.proteins = new SimpleDoubleProperty(recipe.getProteins());
        this.fats = new SimpleDoubleProperty(recipe.getFats());
        this.vitamins = new SimpleDoubleProperty(recipe.getVitamins());
        this.nutrients = new SimpleDoubleProperty(recipe.getNutrients());
        this.skill = new SimpleStringProperty(recipe.getSkill());
        this.skillLvl = new SimpleIntegerProperty(recipe.getSkillLvl());
        this.craftStation = new SimpleStringProperty(recipe.getCraftStation());
        this.tastiness = new SimpleDoubleProperty(recipe.getTastiness());
        this.emulationSource = recipe.getEmulationSource();
    }

    public FoodRecipe() {
        this.count = new SimpleIntegerProperty(-1);
        this.name = new SimpleStringProperty("");
        this.calories = new SimpleIntegerProperty(-1);
        this.carbs = new SimpleDoubleProperty(-1);
        this.proteins = new SimpleDoubleProperty(-1);
        this.fats = new SimpleDoubleProperty(-1);
        this.vitamins = new SimpleDoubleProperty(-1);
        this.nutrients = new SimpleDoubleProperty(-1);
        this.skill = new SimpleStringProperty("");
        this.skillLvl = new SimpleIntegerProperty(-1);
        this.craftStation = new SimpleStringProperty("");
        this.tastiness = new SimpleDoubleProperty(-1);
        this.emulationSource = null;
    }

    public Meal getEmulationSource() {
        return emulationSource;
    }

    public void setEmulationSource(Meal emulationSource) {
        this.emulationSource = emulationSource;
    }

    public double getTastiness() {
        return tastiness.get();
    }

    public SimpleDoubleProperty tastinessProperty() {
        return tastiness;
    }

    public void setTastiness(double tastiness) {
        this.tastiness.set(tastiness);
    }

    public SimpleIntegerProperty countProperty() {
        return count;
    }

    public int getCount() { return count.get(); }

    public void setCount(int count) {
        this.count.set(count);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getCalories() {
        return calories.get();
    }

    public void setCalories(int calories) {
        this.calories.set(calories);
    }

    public IntegerProperty caloriesProperty() {
        return this.calories;
    }

    public double getCarbs() {
        return carbs.get();
    }

    public void setCarbs(double carbs) {
        this.carbs.set(carbs);
    }

    public SimpleDoubleProperty carbsProperty() {
        return this.carbs;
    }

    public double getProteins() {
        return proteins.get();
    }

    public void setProteins(double proteins) {
        this.proteins.set(proteins);
    }

    public SimpleDoubleProperty proteinsProperty() {
        return this.proteins;
    }

    public double getFats() {
        return fats.get();
    }

    public void setFats(double fats) {
        this.fats.set(fats);
    }

    public SimpleDoubleProperty fatsProperty() {
        return this.fats;
    }

    public double getVitamins() {
        return vitamins.get();
    }

    public void setVitamins(double vitamins) {
        this.vitamins.set(vitamins);
    }

    public SimpleDoubleProperty vitaminsProperty() {
        return this.vitamins;
    }

    public double getNutrients() {
        return nutrients.get();
    }

    public void setNutrients(double nutrients) {
        this.nutrients.set(nutrients);
    }

    public SimpleDoubleProperty nutrientsProperty() {
        return nutrients;
    }

    public void calcNutrients() {
        this.nutrients.set(carbs.get() + fats.get() + proteins.get() + vitamins.get());
    }

    public String getSkill() {
        return skill.get();
    }

    public void setSkill(String skill) {
        this.skill.set(skill);
    }

    public int getSkillLvl() {
        return skillLvl.get();
    }

    public void setSkillLvl(int skillLvl) {
        this.skillLvl.set(skillLvl);
    }

    public String getCraftStation() {
        return craftStation.get();
    }

    public void setCraftStation(String craftStation) {
        this.craftStation.set(craftStation);
    }

    @Override
    public String toString() {
        return "FoodRecipe{" +
                "amount=" + count.get() +
                ", name='" + name.get() + '\'' +
                ", calories=" + calories.get() +
                ", carbs=" + carbs.get() +
                ", proteins=" + proteins.get() +
                ", fats=" + fats.get() +
                ", vitamins=" + vitamins.get() +
                ", nutrients=" + nutrients.get() +
                ", tastiness=" + tastiness.get() +
                ", skill='" + skill.get() + '\'' +
                ", skillLvl=" + skillLvl.get() +
                ", craftStation='" + craftStation.get() + '\'' +
                '}';
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(getCount());
        s.writeObject(getName());
        s.writeInt(getCalories());
        s.writeDouble(getCarbs());
        s.writeDouble(getProteins());
        s.writeDouble(getFats());
        s.writeDouble(getVitamins());
        s.writeDouble(getNutrients());
        s.writeObject(getSkill());
        s.writeInt(getSkillLvl());
        s.writeObject(getCraftStation());
        s.writeDouble(getTastiness());
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        count = new SimpleIntegerProperty(s.readInt());
        name = new SimpleStringProperty((String)s.readObject());
        calories = new SimpleIntegerProperty(s.readInt());
        carbs = new SimpleDoubleProperty(s.readDouble());
        proteins = new SimpleDoubleProperty(s.readDouble());
        fats = new SimpleDoubleProperty(s.readDouble());
        vitamins = new SimpleDoubleProperty(s.readDouble());
        nutrients = new SimpleDoubleProperty(s.readDouble());
        skill = new SimpleStringProperty((String) s.readObject());
        skillLvl = new SimpleIntegerProperty(s.readInt());
        craftStation = new SimpleStringProperty((String) s.readObject());
        tastiness = new SimpleDoubleProperty(s.readDouble());
        this.emulationSource = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodRecipe temp = (FoodRecipe) o;
        // don't check emulation source (?) and count (!)
        boolean bEqual = getName().equals(temp.getName());
        bEqual = bEqual && getTastiness() == temp.getTastiness();
        bEqual = bEqual && getCalories() == temp.getCalories();
        bEqual = bEqual && getCarbs() == temp.getCarbs();
        bEqual = bEqual && getFats() == temp.getFats();
        bEqual = bEqual && getProteins() == temp.getProteins();
        bEqual = bEqual && getNutrients() == temp.getNutrients();
        bEqual = bEqual && getCraftStation().equals(temp.getCraftStation());
        bEqual = bEqual && getSkill().equals(temp.getSkill());
        bEqual = bEqual && getSkillLvl() == temp.getSkillLvl();
        return bEqual;
    }
}
