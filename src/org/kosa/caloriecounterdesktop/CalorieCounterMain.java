package org.kosa.caloriecounterdesktop;

import javax.swing.SwingUtilities;

public class CalorieCounterMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run () {
                FoodDatabaseInterface foodDatabase = new FoodDatabaseSQLite();

                CalorieCounterModel model = new CalorieCounterModel(foodDatabase);
                CalorieCounterView view = new CalorieCounterView();
                CalorieCounterController controller = new CalorieCounterController(model, view);
            }
        });
    }
}

