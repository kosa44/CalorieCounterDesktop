package org.kosa.caloriecounterdesktop;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface FoodDatabaseInterface {

    public void initialize() throws SQLException;

    public Foodstuff getFoodstuff(int i);

    public void addToDatabase(Foodstuff food) throws ClassNotFoundException, SQLException;

    public void removeFromDatabase(String s) throws ClassNotFoundException, SQLException;

    public FoodDatabaseInterface getDatabase();

    public ResultSet executeQuery(String sql);

}

