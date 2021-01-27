package org.kosa.caloriecounterdesktop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FoodDatabaseSQLite implements FoodDatabaseInterface {
    private static boolean hasData = false;
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
    public void addToDatabase(Foodstuff food) throws ClassNotFoundException, SQLException {
        PreparedStatement pstmt = conn.prepareStatement(
                "insert into CalorieCounterProducts(name, protein, fats, carbohydrates, calories, grams) values(?,?,?,?,?,?);");
        pstmt.setString(1, food.getName());
        pstmt.setDouble(2, food.getProtein());
        pstmt.setDouble(3, food.getFat());
        pstmt.setDouble(4, food.getCarbohydrates());
        pstmt.setDouble(5, food.getCalories());
        pstmt.setDouble(6, food.getGrams());
        pstmt.execute();
    }

    @Override
    public void removeFromDatabase(String s) throws ClassNotFoundException, SQLException {
        String sql = "DELETE FROM CalorieCounterProducts WHERE name = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, s);
        pstmt.execute();
    }

    public void initialize() throws SQLException {
        if (!hasData) {
            hasData = true;
            // check for database table
            Statement state = conn.createStatement();
            ResultSet res = state.executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='CalorieCounterProducts'");
            if (!res.next()) {
                System.out.println("Building the CalorieCounterProducts table with pre-populated values.");
                // need to build the table
                Statement state2 = conn.createStatement();
                state2.executeUpdate("create table CalorieCounterProducts(id integer," + "name varchar(60),"
                        + "protein real," + "fats real," + "carbohydrates real," + "calories real," + "grams real,"
                        + "primary key (id));");

                // inserting data
                ArrayListFoodData foodArray = new ArrayListFoodData();
                foodArray.createDatabase();
                for (int i = 0; i < foodArray.getData().size(); i++) {
                    PreparedStatement prep = conn.prepareStatement(
                            "insert into CalorieCounterProducts(name, protein, fats, carbohydrates, calories, grams) values(?,?,?,?,?,?);");
                    prep.setString(1, foodArray.getFoodstuff(i).getName());
                    prep.setDouble(2, foodArray.getFoodstuff(i).getProtein());
                    prep.setDouble(3, foodArray.getFoodstuff(i).getFat());
                    prep.setDouble(4, foodArray.getFoodstuff(i).getCarbohydrates());
                    prep.setDouble(5, foodArray.getFoodstuff(i).getCalories());
                    prep.setDouble(6, foodArray.getFoodstuff(i).getGrams());
                    prep.execute();
                }
            }
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
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
