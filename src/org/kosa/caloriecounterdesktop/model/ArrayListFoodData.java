package org.kosa.caloriecounterdesktop;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class ArrayListFoodData {
    private static ArrayList<Foodstuff> foodData;

    public void serializeProductsToJson(Foodstuff[] food){
        try (Writer writer = new FileWriter("C:/Users/lenovo/IdeaProjects/CalorieCounterDesktop/resources/initialProducts.json")) {
//            Type foodstuffArrayList = new TypeToken<ArrayList<Foodstuff>>(){}.getType();
//            Gson gson = new GsonBuilder().create();
//            gson.toJson(foodData, writer);
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
            Type foodstuffArray = new TypeToken<Foodstuff[]>(){}.getType();
            return gson.fromJson(reader, foodstuffArray);
        } catch (JsonParseException | FileNotFoundException e) {
            System.out.println("Couldn't deserialize from json file");
            e.printStackTrace();
        }
        return null;
    }

    public void createDatabase() {
    Foodstuff[] array = deserializeProductsFromJson();
    System.out.println("Array contains: "+array[0].toString());
//        foodData = new ArrayList<Foodstuff>();
//
        Foodstuff apple = new Foodstuff("Jabłko", 0.40, 0.40, 12.10, 46, 100);
//        Foodstuff cheeseGouda = new Foodstuff("Ser Gouda tłusty", 27.9, 22.9, 0.1, 316, 100);
//        Foodstuff cheeseMozarella = new Foodstuff("Ser Mozarella tłusty", 20.0, 16.0, 0.0, 224, 100);
//        Foodstuff chickenBreast = new Foodstuff("Kurczak, pierś bez skóry", 21.5, 13.0, 0.0, 100, 100);
//        Foodstuff cottageCheese = new Foodstuff("Serek wiejski", 12.3, 4.3, 3.3, 101, 100);
//        Foodstuff fromageFrais = new Foodstuff("Ser twarogowy chudy", 19.8, 0.5, 3.5, 99, 100);
//        Foodstuff lentils = new Foodstuff("Soczewica czerwona", 25.4, 3.0, 57.5, 327, 100);
//        Foodstuff milk = new Foodstuff("Mleko", 3.3, 3.2, 4.8, 61, 100);
//        Foodstuff oatmeal = new Foodstuff("Płatki owsiane", 11.9, 7.2, 69.3, 366, 100);
//        Foodstuff porkChop = new Foodstuff("Wieprzowina, schab", 21.0, 10.0, 0.0, 174, 100);
//        Foodstuff porkNeck = new Foodstuff("Wieprzowina, karkówka", 16.1, 22.8, 0.0, 270, 100);
//        Foodstuff potatoes = new Foodstuff("Ziemniaki", 1.7, 0.1, 19.9, 87, 100);
//        Foodstuff riceWhite = new Foodstuff("Ryż biały", 6.7, 7.0, 78.9, 344, 100);
//        Foodstuff tunaInOil = new Foodstuff("Tuńczyk w oleju", 27.1, 9.0, 0.0, 190, 100);
//        Foodstuff wholeEgg = new Foodstuff("Jajka", 12.5, 10.7, 1.0, 150, 100);
//        Foodstuff[] array = { apple, cheeseGouda, cheeseMozarella, chickenBreast, cottageCheese, fromageFrais, lentils, milk,
//                oatmeal, porkChop, porkNeck, potatoes, riceWhite, tunaInOil, wholeEgg };
//        sortDatabaseByName(array);
//            foodData.addAll(Arrays.asList(array));
//        Foodstuff[] array = new Foodstuff[]{apple};
//        serializeProductsToJson(array);
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
