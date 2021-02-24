package org.kosa.caloriecounterdesktop.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CalorieCounterModel implements CalorieCounterModelInterface {
    private FoodDatabaseInterface foodstuffsData;
    private Connection sessionNames;
    private static boolean hasData = false;

    public CalorieCounterModel(FoodDatabaseInterface foodstuffsData) {
        this.foodstuffsData = foodstuffsData;
        try {
            foodstuffsData.initialize();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        try {
            String sessionNamesURL = "jdbc:sqlite:SessionNames.db";
            sessionNames = DriverManager.getConnection(sessionNamesURL);
            // check for database table
            if (!hasData) {
                hasData = true;
                Statement state = sessionNames.createStatement();
                ResultSet res = state
                        .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='SessionNames'");
                if (!res.next()) {
                    // need to build the table
                    System.out.println("Building the SessionNames table with pre-populated values.");
                    Statement state2 = sessionNames.createStatement();
                    state2.executeUpdate(
                            "CREATE TABLE SessionNames(id integer," + "name varchar(60)," + "primary key (id));");
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
    public void addSessionName(String name) {
        try {
            PreparedStatement pstmt = sessionNames.prepareStatement("INSERT INTO SessionNames(name) values(?);");
            pstmt.setString(1, name);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultSet getSessionNames() {
        try {
            PreparedStatement stmt = sessionNames.prepareStatement("SELECT * FROM SessionNames");
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void removeSessionName(String name) {
        try {
            PreparedStatement pstmt = sessionNames.prepareStatement("DELETE FROM SessionNames WHERE name = ?");
            pstmt.setString(1, name);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

