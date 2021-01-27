package org.kosa.caloriecounterdesktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class CalorieCounterController implements CalorieCounterControllerInterface {
    CalorieCounterModel model;
    CalorieCounterView view;

    public CalorieCounterController(CalorieCounterModel model, CalorieCounterView view) {
        this.model = model;
        this.view = view;

        view.addAddButtonListener(new AddButtonListener());
        view.addRemoveButtonListener(new RemoveButtonListener());
        view.addClearButtonListener(new ClearButtonListener());
        view.addSaveSessionButtonListener(new SaveSessionButtonListener());
        view.addLoadSessionButtonListener(new LoadSessionButtonListener());
        view.addRemoveSessionButtonListener(new RemoveSessionButtonListener());
        view.addAddProductButtonListener(new AddProductButtonListener());
        view.addRemoveProductButtonListener(new RemoveProductButtonListener());

        populateSelectionList(view.getListModel());
    }

    class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int grams = Integer.valueOf(view.getGrams());
            String name = view.getSelectedItem();
            addToSession(name, grams);
            view.updateSum();
        }
    }

    class RemoveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int row = view.getSelectedRow();
            removeFromSession(row);
            view.updateSum();
        }
    }

    class ClearButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            clearSession();
            view.updateSum();
        }
    }

    class SaveSessionButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            saveSession();
        }
    }

    class LoadSessionButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            loadSession();
            view.updateSum();
        }
    }

    class RemoveSessionButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            removeSession();
        }
    }

    class AddProductButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            addFoodstuff();
            clearSelectionList(view.getListModel());
            populateSelectionList(view.getListModel());
        }
    }

    class RemoveProductButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            removeFoodstuff();
            clearSelectionList(view.getListModel());
            populateSelectionList(view.getListModel());
        }
    }

    public void fillListModel(DefaultListModel<String> list, String s) {
        list.addElement(s);
    }

    public void populateSelectionList(DefaultListModel<String> list) {
        String sql = "SELECT name FROM CalorieCounterProducts ORDER BY name";

        ResultSet rs = model.getData().executeQuery(sql);

        try {
            while (rs.next()) {
                fillListModel(list, (rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearSelectionList(DefaultListModel<String> list) {
        list.clear();
    }

    @Override
    public void addToSession(String foodName, double grams) {
        String name = foodName;
        String sql = "SELECT * FROM CalorieCounterProducts WHERE name = ";

        try {

            ResultSet rs = model.getData().executeQuery(sql + "'" + name + "'");
            while (rs.next()) {
                view.addRow(rs.getString("name"), rs.getDouble("protein") * (grams / rs.getInt("grams")),
                        rs.getDouble("fats") * (grams / rs.getInt("grams")),
                        rs.getDouble("carbohydrates") * (grams / rs.getInt("grams")),
                        rs.getDouble("calories") * (grams / rs.getDouble("grams")), grams);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void removeFromSession(int i) {
        view.removeRow(i);

    }

    @Override
    public void clearSession() {
        view.clearSession();
    }

    @Override
    public void addFoodstuff() {
        JFrame frame = new JFrame();
        JTextField nameField = new JTextField(5);
        JTextField proteinField = new JTextField(5);
        JTextField fatsField = new JTextField(5);
        JTextField carbohydratesField = new JTextField(5);
        JTextField caloriesField = new JTextField(5);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Nazwa:"));
        myPanel.add(nameField);
        myPanel.add(Box.createHorizontalStrut(15));
        myPanel.add(new JLabel("Białko:"));
        myPanel.add(proteinField);
        myPanel.add(Box.createHorizontalStrut(15));
        myPanel.add(new JLabel("Tłuszcze:"));
        myPanel.add(fatsField);
        myPanel.add(Box.createHorizontalStrut(15));
        myPanel.add(new JLabel("Węglowodany:"));
        myPanel.add(carbohydratesField);
        myPanel.add(Box.createHorizontalStrut(15));
        myPanel.add(new JLabel("Kalorie:"));
        myPanel.add(caloriesField);

        int result = JOptionPane.showConfirmDialog(frame, myPanel, "Podaj wartości w przeliczeniu na 100 gram",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Foodstuff food = new Foodstuff(nameField.getText().replace(',', '.'),
                    Double.parseDouble(proteinField.getText().replace(',', '.')),
                    Double.parseDouble(fatsField.getText().replace(',', '.')),
                    Double.parseDouble(carbohydratesField.getText().replace(',', '.')),
                    Double.parseDouble(caloriesField.getText().replace(',', '.')), 100);

            model.addProduct(food);
        }
    }

    @Override
    public void removeFoodstuff() {
        DefaultListModel<String> list1 = new DefaultListModel<>();

        JList<String> list = new JList<>(list1);
        list.setVisibleRowCount(8);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        populateSelectionList(list1);
        String[] products = new String[list1.capacity()];
        for (int i = 0; i < list1.getSize(); i++) {
            products[i] = list1.getElementAt(i);
        }

        String result = (String) JOptionPane.showInputDialog(null, "Wybierz produkt, kt�ry chcesz usun��",
                "Usuwanie produktu", JOptionPane.QUESTION_MESSAGE, null, products, products[0]);
        model.removeProduct(result);
    }

    @Override
    public void saveSession() {
        String message = "Zapisz";
        String dbName = null;
        String url = null;
        do {
            dbName = JOptionPane.showInputDialog(message);
            message = "<html><b style='color:red'>Wpisz nazwę:</b><br>"
                    + "Wpisz nazwę bez spacji i znaków specjalnych, nazwa CalorieCounterProducts jest już zajęta.";

        } while ((dbName != null && dbName.equals("CalorieCounterProducts") || ((dbName.equals(""))
                || (dbName.equals("")) || (((dbName.charAt(0) >= '0') && (dbName.charAt(0) <= '9'))))));
        url = "jdbc:sqlite:" + dbName + ".db";
        {
            try (Connection conn = DriverManager.getConnection(url);) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt
                        .executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + dbName + "'");
                if (!rs.next()) {
                    System.out.println("Building the " + dbName + " table.");
                    // need to build the table
                    Statement stmt3 = conn.createStatement();
                    stmt3.executeUpdate("create table " + dbName + "(id integer," + "name varchar(60),"
                            + "protein real," + "fats real," + "carbohydrates real," + "calories real," + "grams real,"
                            + "primary key (id));");
                    System.out.println("table built");
                }

                Statement stmt2 = conn.createStatement();
                ResultSet rs2 = stmt2.executeQuery("SELECT * FROM " + dbName + " WHERE id >= 0");
                if (rs2.next()) {
                    Statement stmt3 = conn.createStatement();
                    stmt3.execute("DELETE FROM " + dbName);
                }

                DefaultTableModel model = view.getSessionTableModel();
                for (int i = 0; i < model.getRowCount(); i++) {
                    PreparedStatement prep = conn.prepareStatement("INSERT INTO " + dbName
                            + "(name, protein, fats, carbohydrates, calories, grams) values(?,?,?,?,?,?);");
                    prep.setString(1, (String) model.getValueAt(i, 0));
                    prep.setDouble(2, (double) model.getValueAt(i, 1));
                    prep.setDouble(3, (double) model.getValueAt(i, 2));
                    prep.setDouble(4, (double) model.getValueAt(i, 3));
                    prep.setDouble(5, (double) model.getValueAt(i, 4));
                    prep.setDouble(6, (double) model.getValueAt(i, 5));
                    prep.execute();
                }
            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }
            model.addSessionName(dbName);
        }
    }

    @Override
    public void loadSession() {
        ResultSet sessionNames = model.getSessionNames();
        String[] nameArray = null;
        ArrayList<String> list = new ArrayList<>();
        try {
            while (sessionNames.next()) {
                list.add(sessionNames.getString("Name"));
            }
            nameArray = new String[list.size()];
            for (int i = 0; i < nameArray.length; i++) {
                nameArray[i] = list.get(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (nameArray.length > 0) {

            String dbName = (String) JOptionPane.showInputDialog(null, "Wczytaj:", "Wczytywanie",
                    JOptionPane.QUESTION_MESSAGE, null, nameArray, nameArray[0]);

            if (dbName != null) {

                String url = "jdbc:sqlite:" + dbName + ".db";

                try {
                    Connection conn = DriverManager.getConnection(url);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM " + dbName + "");

                    while (rs.next()) {
                        view.addRow(rs.getString("Name"), rs.getDouble("Protein"), rs.getDouble("Fats"),
                                rs.getDouble("Carbohydrates"), rs.getDouble("Calories"), rs.getDouble("Grams"));
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Lista jest pusta");
        }
    }

    @Override
    public void removeSession() {
        ResultSet sessionNames = model.getSessionNames();
        String[] nameArray = null;
        ArrayList<String> list = new ArrayList<>();
        try {
            while (sessionNames.next()) {
                list.add(sessionNames.getString("Name"));
            }
            nameArray = new String[list.size()];
            for (int i = 0; i < nameArray.length; i++) {
                nameArray[i] = list.get(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (nameArray.length > 0) {
            String dbName = (String) JOptionPane.showInputDialog(null, "Usuń:", "Usuwanie",
                    JOptionPane.QUESTION_MESSAGE, null, nameArray, nameArray[0]);

            if (dbName != null) {
                model.removeSessionName(dbName);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Lista jest pusta");
        }
    }

    @Override
    public void setLanguage() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setView() {
        // TODO Auto-generated method stub

    }
}

