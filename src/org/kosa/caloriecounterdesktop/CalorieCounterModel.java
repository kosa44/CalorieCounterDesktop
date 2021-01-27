package org.kosa.caloriecounterdesktop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CalorieCounterModel implements CalorieCounterModelInterface {
    private FoodDatabaseInterface foodstuffsData;
    private Connection sessionNames;
    private String sessionNamesURL = "jdbc:sqlite:SessionNames.db";
    private ArrayList observers = new ArrayList();
    private static boolean hasData = false;

    public CalorieCounterModel(FoodDatabaseInterface foodstuffsData) {
        this.foodstuffsData = foodstuffsData;
        try {
            foodstuffsData.initialize();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        try {
            sessionNames = DriverManager.getConnection(sessionNamesURL);
            if (!hasData) {
                hasData = true;
                // check for database table
                Statement state = sessionNames.createStatement();
                ResultSet res = state
                        .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='SessionNames'");
                if (!res.next()) {
                    System.out.println("Building the SessionNames table with pre-populated values.");
                    // need to build the table
                    Statement state2 = sessionNames.createStatement();
                    state2.executeUpdate(
                            "create table SessionNames(id integer," + "name varchar(60)," + "primary key (id));");
                }
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public FoodDatabaseInterface getData() {
        return foodstuffsData;
    }

    public Foodstuff getFoodstuff(int i) {
        return foodstuffsData.getFoodstuff(i);
    }

    public void registerObserver(CalorieCounterObserver o) {

    }

    public void removeObserver(CalorieCounterObserver o) {

    }

    public void notifyObservers() {

    }

    @Override
    public void addProduct(Foodstuff food) {
        try {
            foodstuffsData.addToDatabase(food);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeProduct(String s) {
        try {
            foodstuffsData.removeFromDatabase(s);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addSessionName(String s) {
        try {
            PreparedStatement pstmt = sessionNames.prepareStatement("INSERT into SessionNames(name) values(?);");
            pstmt.setString(1, s);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultSet getSessionNames() {
        try {
            PreparedStatement stmt = sessionNames.prepareStatement("SELECT * FROM SessionNames");
            ResultSet rs = stmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void removeSessionName(String s) {
        try {
            PreparedStatement pstmt = sessionNames.prepareStatement("DELETE FROM SessionNames where name = ?");
            pstmt.setString(1, s);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

