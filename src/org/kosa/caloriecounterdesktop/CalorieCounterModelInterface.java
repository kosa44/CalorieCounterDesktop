package org.kosa.caloriecounterdesktop;

import java.sql.ResultSet;

public interface CalorieCounterModelInterface {

    public void registerObserver(CalorieCounterObserver o);

    public void removeObserver(CalorieCounterObserver o);

    public void notifyObservers();

    public FoodDatabaseInterface getData();

    public void addProduct(Foodstuff food);

    public void removeProduct(String s);

    public void addSessionName(String s);

    public ResultSet getSessionNames();

    public void removeSessionName(String s);
}
