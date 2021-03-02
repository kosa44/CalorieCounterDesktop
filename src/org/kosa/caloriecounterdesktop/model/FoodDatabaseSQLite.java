package org.kosa.caloriecounterdesktop.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FoodDatabaseSQLite implements FoodDatabaseInterface {
    private String dbUrl = "jdbc:sqlite:CalorieCounterProducts.db";

    public ResultSet getName(String name) throws SQLException {
        Connection conn = DriverManager.getConnection(dbUrl);
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT name FROM CalorieCounterProducts WHERE name = " + "'" + name + "'");
    }

    public ResultSet getProtein(String name) throws SQLException {
        Connection conn = DriverManager.getConnection(dbUrl);
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT protein FROM CalorieCounterProducts WHERE name = " + "'" + name + "'");
    }

    public ResultSet displayProducts() throws SQLException {
        Connection conn = DriverManager.getConnection(dbUrl);
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT name, protein, fats, carbohydrates, calories, grams FROM CalorieCounterProducts");
    }

    @Override
    public Foodstuff getFoodstuff(int i) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addToDatabase(Foodstuff food) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:CalorieCounterProducts.db")) {
            System.out.println("Conn is " + conn.toString());
            boolean formerAutoCommitMode = conn.getAutoCommit();
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO CalorieCounterProducts(name, protein, fats, carbohydrates, calories, grams) VALUES (?,?,?,?,?,?);")) {
                conn.setAutoCommit(false);
                pstmt.setString(1, food.getName());
                pstmt.setDouble(2, food.getProtein());
                pstmt.setDouble(3, food.getFat());
                pstmt.setDouble(4, food.getCarbohydrates());
                pstmt.setDouble(5, food.getCalories());
                pstmt.setDouble(6, food.getGrams());
                pstmt.execute();
                System.out.println(pstmt.toString());
                conn.commit();
            } catch (SQLException e) {
                System.out.println("Failed to add new product to the database, transaction is being rolled back");
                conn.rollback();
                e.printStackTrace();
            }
            conn.setAutoCommit(formerAutoCommitMode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeFromDatabase(String name) {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            boolean formerAutoCommitMode = conn.getAutoCommit();
            try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM CalorieCounterProducts WHERE name = ?")) {
                conn.setAutoCommit(false);
                pstmt.setString(1, name);
                pstmt.execute();
                conn.commit();
            } catch (SQLException e) {
                System.out.println("Failed to remove product " + name + " from the database, transaction is being rolled back");
                conn.rollback();
                e.printStackTrace();
            }
            conn.setAutoCommit(formerAutoCommitMode);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initialize() throws SQLException {
        if (hasData()) {
            return;
        }
        //check for database table
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            boolean formerAutoCommitMode = conn.getAutoCommit();
            conn.setAutoCommit(false);
            String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='CalorieCounterProducts'";
            try (Statement stmt = conn.createStatement(); ResultSet res = stmt.executeQuery(sql)) {
                if (!res.next()) {
                    //need to build the table
                    System.out.println("Building the CalorieCounterProducts table with pre-populated values.");
                    Statement stmt2 = conn.createStatement();
                    stmt2.executeUpdate("CREATE TABLE CalorieCounterProducts(id integer," + "name varchar(60),"
                            + "protein real," + "fats real," + "carbohydrates real," + "calories real," + "grams real,"
                            + "primary key (id));");
                    stmt2.close();
                    conn.commit();
                }
            } catch (SQLException e) {
                System.out.println("Failed to create products table, transaction is being rolled back");
                conn.rollback();
                e.printStackTrace();
            }
            conn.setAutoCommit(formerAutoCommitMode);
        }
        if (hasData() == false) {
        insertInitialData();
        }
    }

    public void insertInitialData() throws SQLException {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            ArrayListFoodData foodArray = new ArrayListFoodData();
            foodArray.createDatabase();
            boolean formerAutoCommitMode = conn.getAutoCommit();
            String sql = "INSERT INTO CalorieCounterProducts(name, protein, fats, carbohydrates, calories, grams) VALUES (?,?,?,?,?,?);";
//            for (int i = 0; i < foodArray.getData().size(); i++) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    conn.setAutoCommit(false);
                for (int i = 0; i < foodArray.getData().size(); i++) {
                    pstmt.setString(1, foodArray.getFoodstuff(i).getName());
                    pstmt.setDouble(2, foodArray.getFoodstuff(i).getProtein());
                    pstmt.setDouble(3, foodArray.getFoodstuff(i).getFat());
                    pstmt.setDouble(4, foodArray.getFoodstuff(i).getCarbohydrates());
                    pstmt.setDouble(5, foodArray.getFoodstuff(i).getCalories());
                    pstmt.setDouble(6, foodArray.getFoodstuff(i).getGrams());
                    pstmt.execute();
                }
                    conn.commit();
                } catch (SQLException e) {
                    System.out.println("Failed to insert initial data, transaction is being rolled back");
                    conn.rollback();
                    e.printStackTrace();
                }
//            }
            conn.setAutoCommit(formerAutoCommitMode);
        }
    }

    @Override
    public FoodDatabaseInterface getDatabase() {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public ResultSet executeQuery(String sql) {
        try {
            Connection conn = DriverManager.getConnection(dbUrl);
            Statement stmt = conn.createStatement();

            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean hasData() {
        try (Connection conn = DriverManager.getConnection(dbUrl); Statement stmt = conn.createStatement()) {
            ResultSet res = stmt.executeQuery(
                    "SELECT name FROM CalorieCounterProducts");
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
