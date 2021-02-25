package org.kosa.caloriecounterdesktop.model;

import java.io.File;
import java.sql.*;

public class CalorieCounterModel implements CalorieCounterModelInterface {
    private FoodDatabaseInterface foodstuffsData;
    private final String sessionNamesURL = "jdbc:sqlite:SessionNames.db";
    private static boolean hasData = false;

    public CalorieCounterModel(FoodDatabaseInterface foodstuffsData) {
        this.foodstuffsData = foodstuffsData;
        try {
            foodstuffsData.initialize();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        try {
            Connection conn = DriverManager.getConnection(sessionNamesURL);
            // check for database table
            if (!hasData) {
                hasData = true;
                Statement state = conn.createStatement();
                ResultSet res = state
                        .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='SessionNames'");
                if (!res.next()) {
                    // need to build the table
                    System.out.println("Building the SessionNames table with pre-populated values.");
                    Statement state2 = conn.createStatement();
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
        try (Connection conn = DriverManager.getConnection(sessionNamesURL)) {
            boolean formerAutoCommitMode = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO SessionNames(name) values(?)")) {
                pstmt.setString(1, name);
                pstmt.execute();
                conn.commit();
            } catch (SQLException e) {
                System.out.println("Failed to add new session, transaction is being rolled back");
                conn.rollback();
                e.printStackTrace();
            }
            conn.setAutoCommit(formerAutoCommitMode);
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public ResultSet getSessionNames() {
        try {
            Connection conn = DriverManager.getConnection(sessionNamesURL);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM SessionNames");
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void removeSessionName(String name) {
        try (Connection conn = DriverManager.getConnection(sessionNamesURL)) {
            boolean formerAutoCommitMode = conn.getAutoCommit();
            conn.setAutoCommit(false);
            File file = new File("/"+name+".db");
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM SessionNames WHERE name = ?")) {
                if (file.delete()) {
                    pstmt.setString(1,name);
                    pstmt.execute();
                    conn.commit();
                } else {
                    System.out.println("Failed to delete file "+name);
                }
            } catch (SQLException e) {
                System.out.println("Failed to remove session, transaction is being rolled back");
                conn.rollback();
                e.printStackTrace();
            }
            conn.setAutoCommit(formerAutoCommitMode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

