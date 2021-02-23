package org.kosa.caloriecounterdesktop.model;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class ArrayListFoodData {
    private static ArrayList<Foodstuff> foodData;

    public void serializeProductsToJson(Foodstuff[] food) {
        try (Writer writer = new FileWriter("C:/Users/lenovo/IdeaProjects/CalorieCounterDesktop/resources/initialProducts.json")) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(food, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Foodstuff[] deserializeProductsFromJson() {
        try {
            Gson gson = new GsonBuilder().create();
            Reader reader = new FileReader("C:/Users/lenovo/IdeaProjects/CalorieCounterDesktop/resources/initialProducts.json");
            Type foodstuffArray = new TypeToken<Foodstuff[]>() {
            }.getType();
            return gson.fromJson(reader, foodstuffArray);
        } catch (JsonParseException | FileNotFoundException e) {
            System.out.println("Couldn't deserialize from json file");
            e.printStackTrace();
        }
        return null;
    }

    public void createDatabase() {
        Foodstuff[] array = deserializeProductsFromJson();
        foodData = new ArrayList<Foodstuff>();
        foodData.addAll(Arrays.asList(array));
    }

    public List<Foodstuff> getData() {
        return foodData;
    }

    public Foodstuff getFoodstuff(int i) {
        return foodData.get(i);
    }

    public void addToData(Foodstuff food) {
        foodData.add(food);
    }

    public void removeFromData(int i) {
        foodData.remove(i);
    }

    public void sortDatabaseByName(Foodstuff[] data) {
        for (int i = 0; i < data.length; i++) {
            int smallest = findSmallest(data, i, data.length);
            swapFoodstuffs(data, i, smallest);
        }
    }

    public void swapFoodstuffs(Foodstuff[] data, int first, int second) {
        Foodstuff temp = data[first];
        data[first] = data[second];
        data[second] = temp;
    }

    public int findSmallest(Foodstuff[] data, int lowIndex, int highIndex) {
        int smallest = lowIndex;
        for (int i = smallest; i < highIndex; i++) {
            if (data[smallest].getName().compareTo(data[i].getName()) >= 1) {
                smallest = i;
            }
        }
        return smallest;
    }
}
