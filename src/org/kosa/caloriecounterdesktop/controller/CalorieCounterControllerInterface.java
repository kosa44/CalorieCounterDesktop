package org.kosa.caloriecounterdesktop;

public interface CalorieCounterControllerInterface {
    void addToSession(String foodName, double gram);
    void removeFromSession(int i);
    void clearSession();
    void addFoodstuff();
    void removeFoodstuff();
    void saveSession();
    void loadSession();
    void setLanguage();
    void setView();
    void removeSession();
}
