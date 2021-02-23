package org.kosa.caloriecounterdesktop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FoodDatabaseSQLite implements FoodDatabaseInterface {
    private static Connection conn = null;

    public FoodDatabaseSQLite() {
        String url = "jdbc:sqlite:CalorieCounterProducts.db";
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet getName(String name) throws ClassNotFoundException, SQLException {
        Statement state = conn.createStatement();
        ResultSet res = state.executeQuery("SELECT name FROM CalorieCounterProducts WHERE name = " + "'" + name + "'");
        return res;
    }

    public ResultSet getProtein(String name) throws ClassNotFoundException, SQLException {
        Statement state = conn.createStatement();
        ResultSet res = state
                .executeQuery("SELECT protein FROM CalorieCounterProducts WHERE name = " + "'" + name + "'");
        return res;
    }

    public ResultSet displayProducts() throws SQLException, ClassNotFoundException {
        Statement state = conn.createStatement();
        ResultSet res = state
                .executeQuery("SELECT name, protein, fats, carbohydrates, calories, grams FROM CalorieCounterProducts");
        return res;
    }

    @Override
    public Foodstuff getFoodstuff(int i) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addToDatabase(Foodstuff food) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO CalorieCounterProducts(name, protein, fats, carbohydrates, calories, grams) VALUES (?,?,?,?,?,?);")) {
            conn.setAutoCommit(false);
            pstmt.setString(1, food.getName());
            pstmt.setDouble(2, food.getProtein());
            pstmt.setDouble(3, food.getFat());
            pstmt.setDouble(4, food.getCarbohydrates());
            pstmt.setDouble(5, food.getCalories());
            pstmt.setDouble(6, food.getGrams());
            pstmt.execute();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("Transaction is being rolled back");
            e.printStackTrace();
        }
    }

    @Override
    public void removeFromDatabase(String name) throws SQLException {
        String sql = "DELETE FROM CalorieCounterProducts WHERE name = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            pstmt.setString(1, name);
            pstmt.execute();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("Transaction is being rolled back");
            e.printStackTrace();
        }
    }

    public void initialize() throws SQLException {
//        if (hasData()) {
//            return;
//        }
        // check for database table
        ArrayListFoodData foodArray = new ArrayListFoodData();
        foodArray.createDatabase();
        Statement state = conn.createStatement();
        ResultSet res = state.executeQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='CalorieCounterProducts'");
        System.out.println("ResultSet: "+res.toString());
        if (!res.next()) {
            System.out.println("Building the CalorieCounterProducts table with pre-populated values.");
            // need to build the table
            conn.setAutoCommit(false);
            try (Statement state2 = conn.createStatement()) {
                state2.executeUpdate("CREATE TABLE CalorieCounterProducts(id integer," + "name varchar(60),"
                        + "protein real," + "fats real," + "carbohydrates real," + "calories real," + "grams real,"
                        + "primary key (id));");
                conn.commit();
                if (hasData() == false) {
                    insertInitialData();
                }
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Transaction is being rolled back");
                e.printStackTrace();
            }
        }
        if (hasData() == false) {
            insertInitialData();
        }
    }

    public void insertInitialData() throws SQLException {
        // inserting data
        ArrayListFoodData foodArray = new ArrayListFoodData();
        foodArray.createDatabase();
        return;
//        for (int i = 0; i < foodArray.getData().size(); i++) {
//            try (PreparedStatement prep = conn.prepareStatement(
//                    "INSERT INTO CalorieCounterProducts(name, protein, fats, carbohydrates, calories, grams) VALUES (?,?,?,?,?,?);")) {
//                conn.setAutoCommit(false);
//                prep.setString(1, foodArray.getFoodstuff(i).getName());
//                prep.setDouble(2, foodArray.getFoodstuff(i).getProtein());
//                prep.setDouble(3, foodArray.getFoodstuff(i).getFat());
//                prep.setDouble(4, foodArray.getFoodstuff(i).getCarbohydrates());
//                prep.setDouble(5, foodArray.getFoodstuff(i).getCalories());
//                prep.setDouble(6, foodArray.getFoodstuff(i).getGrams());
//                prep.execute();
//                conn.commit();
//            } catch (SQLException e) {
//                conn.rollback();
//                System.out.println("Transaction is being rolled back");
//                e.printStackTrace();
//            }

//        }
    }

    @Override
    public FoodDatabaseInterface getDatabase() {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public ResultSet executeQuery(String sql) {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean hasData() {
        try {
            Statement state = conn.createStatement();
            ResultSet res = state.executeQuery(
                    "SELECT * FROM CalorieCounterProducts");
            if (!res.next()) {
                System.out.print("db empty");
                return false;
            }
            if (res.next()) {
                System.out.println(res.getRow());
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Couldn't connect to the database");
            e.printStackTrace();
        }
        return false;
    }
}
