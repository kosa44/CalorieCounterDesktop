package org.kosa.caloriecounterdesktop.model;

import java.io.Serializable;

public class Foodstuff implements Comparable<Foodstuff>, Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private double protein;
    private double fat;
    private double carbohydrates;
    private double calories;
    private double grams;

    public Foodstuff(String name, double protein, double fat, double carbohydrates, double calories, double grams) {
        this.name = name;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.calories = calories;
        this.grams = grams;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGrams() {
        return grams;
    }

    public void setGrams(double grams) {
        this.grams = grams;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    @Override
    public int compareTo(Foodstuff food) {
        return this.getName().compareTo(food.getName());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Foodstuff{").append(getName()).append('\'');
        sb.append(", name=").append(getName());
        sb.append(", protein=").append(getProtein());
        sb.append(", fat=").append(getFat());
        sb.append(", carbohydrates=").append(getCarbohydrates());
        sb.append(", calories=").append(getCalories());
        sb.append(", grams=").append(getGrams()).append('}');
        return sb.toString();
    }
}

