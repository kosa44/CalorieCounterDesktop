package org.kosa.caloriecounterdesktop.model;

import java.io.File;
import java.sql.*;

public class CalorieCounterModel implements CalorieCounterModelInterface {
    private FoodDatabaseInterface foodstuffsData;
    private final String sessionNamesURL = "jdbc:sqlite:SessionNames.db";

    public CalorieCounterModel(FoodDatabaseInterface foodstuffsData) {
        this.foodstuffsData = foodstuffsData;
        try {
            foodstuffsData.initialize();
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        if (hasData()) {
            return;
        }
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='SessionNames'";
        try (Connection conn = DriverManager.getConnection(sessionNamesURL); Statement stmt = conn.createStatement();
             ResultSet res = stmt
                     .executeQuery(sql);) {
            boolean formerAutoCommit = conn.getAutoCommit();
            // check for database table
            if (!res.next()) {
                // need to build the table
                try (Statement stmt2 = conn.createStatement()) {
                    conn.setAutoCommit(false);
                    stmt2.executeUpdate(
                            "CREATE TABLE SessionNames(id integer," + "name varchar(60)," + "primary key (id));");
                    System.out.println("Building the SessionNames table with pre-populated values.");
                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback();
                    conn.setAutoCommit(formerAutoCommit);
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            File file = new File("/" + name + ".db");
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM SessionNames WHERE name = ?")) {
                if (file.delete()) {
                    pstmt.setString(1, name);
                    pstmt.execute();
                    conn.commit();
                } else {
                    System.out.println("Failed to delete file " + name);
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

    private boolean hasData() {
        try (Connection conn = DriverManager.getConnection(sessionNamesURL); Statement stmt = conn.createStatement()) {
            ResultSet res = stmt.executeQuery(
                    "SELECT name FROM SessionNames");
            if (!res.next()) {
                res.close();
                return false;
            }
            if (res.next()) {
                res.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

