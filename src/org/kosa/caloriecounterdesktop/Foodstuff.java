package org.kosa.caloriecounterdesktop;

import java.io.Serializable;

public class Foodstuff implements Comparable<Foodstuff>, Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private double protein;
    private double fat;
    private double carbohydrates;
    private double calories;
    private double grams;

    Foodstuff(String name, double protein, double fat, double carbohydrates, double calories, double grams) {
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
        for (int i = 0; i < this.getName().length(); i++) {
            if (this.getName().charAt(i) > ((Foodstuff) food).getName().charAt(i)) {
                return 1;
            } else if (this.getName().charAt(i) < ((Foodstuff) food).getName().charAt(i)) {
                return -1;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Foodstuff{" +
                "name='" + name + '\'' +
                ", protein=" + protein +
                ", fat=" + fat +
                ", carbohydrates=" + carbohydrates +
                ", calories=" + calories +
                '}';
    }
}

