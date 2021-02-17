package org.kosa.caloriecounterdesktop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.text.DecimalFormat;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

public class CalorieCounterView {
    private JFrame viewFrame;
    private JPanel viewPanel;
    private JPanel buttonsPanel;

    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenu menuProducts;

    private JMenuItem saveSession;
    private JMenuItem loadSession;
    private JMenuItem removeSession;
    private JMenuItem addProduct;
    private JMenuItem removeProduct;

    private JLabel productLabel;
    private JLabel gramLabel;

    private JScrollPane chooseProductScroll;
    private DefaultListModel<String> list1;
    private JList<String> list;
    private JTextField howManyGrams;
    private DefaultTableModel sessionTableModel;
    private JTable sessionTable;
    private DefaultTableModel sumTableModel;
    private JTable sumTable;

    private JButton addButton;
    private JButton removeButton;
    private JButton clearButton;

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public CalorieCounterView() {

        viewFrame = new JFrame("Schabowy 4 Ever :)");
//        URL url = CalorieCounterMain.class.getResource("/resources/steak-rare.png");
//        ImageIcon img = new ImageIcon(url);
//        viewFrame.setIconImage(img.getImage());

        viewPanel = new JPanel(new GridBagLayout());

        createMenuBar();

        GridBagConstraints c = new GridBagConstraints();

        productLabel = new JLabel("Produkt");
        c.weighty = 0.0;
        c.weightx = 2.0;
        c.gridx = 0;
        c.gridy = 0;
        c.insets.bottom = 10;
        viewPanel.add(productLabel, c);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(4, 1));
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0.0;
        c.weightx = 1.0;
        c.gridx = 2;
        c.gridy = 1;
        c.insets.right = 20;
        viewPanel.add(buttonsPanel, c);

        gramLabel = new JLabel("Gram");
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.gridx = 2;
        c.gridy = 0;
        c.insets.left = 135;
        c.insets.bottom = 10;
        viewPanel.add(gramLabel, c);

        howManyGrams = new JTextField("100");
        howManyGrams.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                int max = 4;
                if (howManyGrams.getText().length() == max) {
                    e.consume();
                } else if (howManyGrams.getText().length() > max) {
                    howManyGrams.setText(String.valueOf(e.getKeyChar()));
                }
            }

            @Override
            public void keyPressed(KeyEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                // TODO Auto-generated method stub

            }
        });
        c.weightx = 0.1;
        c.weighty = 0.0;

        c.gridx = 1;
        c.gridy = 1;
        c.gridheight = 3;
        buttonsPanel.add(howManyGrams, c);

        addButton = new JButton("Dodaj");

        buttonsPanel.add(addButton);

        removeButton = new JButton("Usuń");

        buttonsPanel.add(removeButton);

        clearButton = new JButton("Wyczyść");
        clearButton.setSize(10, 5);

        buttonsPanel.add(clearButton);

        chooseProductScroll = new JScrollPane();
        list1 = new DefaultListModel<>();

        list = new JList<>(list1);
        list.setVisibleRowCount(8);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chooseProductScroll = new JScrollPane(list);
        c.weightx = 0.1;
        c.weighty = 0.0;
        c.gridheight = 1;
        c.insets.right = 15;
        c.insets.left = 15;
        c.gridx = 0;
        c.gridy = 1;
        viewPanel.add(chooseProductScroll, c);

        sessionTable = createSessionTable();
        JScrollPane sessionScroll = new JScrollPane(sessionTable);
        sessionScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        sessionScroll.getViewport().setBackground(Color.WHITE);
        sessionScroll.setPreferredSize(
                new Dimension(sessionScroll.getPreferredSize().width, sessionTable.getRowHeight() * 10));
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets.right = 15;
        c.insets.left = 15;
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 4;
        viewPanel.add(sessionScroll, c);

        String[] columns = { "", "Białko", "Tłuszcz", "Węglowodany", "Kalorie", "Gram" };
        Object[] row = new Object[6];
        row[0] = "Suma";

        sumTableModel = new DefaultTableModel(columns, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        sumTable = new JTable(sumTableModel);
        sumTable.setShowHorizontalLines(false);
        sumTableModel.addRow(row);
        JScrollPane sumScroll = new JScrollPane(sumTable);
        sumScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        sumScroll.getViewport().setBackground(Color.WHITE);
        sumScroll.setPreferredSize(
                new Dimension(sumScroll.getPreferredSize().width, sumTable.getPreferredSize().height * 3));

        c.weightx = 0.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets.right = 15;
        c.insets.left = 15;
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 4;
        viewPanel.add(sumScroll, c);

        viewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewFrame.setSize(809, 500);
        viewFrame.setMinimumSize(new Dimension(700, 480));
        viewFrame.getContentPane().add(viewPanel, BorderLayout.CENTER);
        viewFrame.setJMenuBar(menuBar);
        viewFrame.setResizable(false);
        viewFrame.setLocationRelativeTo(null);
        viewFrame.setVisible(true);
    }

    public JMenuBar createMenuBar() {

        menuBar = new JMenuBar();

        menuFile = new JMenu("Plik");
        menuBar.add(menuFile);

        saveSession = new JMenuItem("Zapisz");
        menuFile.add(saveSession);

        loadSession = new JMenuItem("Wczytaj");
        menuFile.add(loadSession);

        removeSession = new JMenuItem("Usuń");
        menuFile.add(removeSession);

        menuProducts = new JMenu("Produkty");
        menuBar.add(menuProducts);

        addProduct = new JMenuItem("Dodaj produkt");
        menuProducts.add(addProduct);

        removeProduct = new JMenuItem("Usuń produkt");
        menuProducts.add(removeProduct);

        return menuBar;
    }

    public JTable createSessionTable() {
        String[] columns = { "Produkt", "Białko", "Tłuszcz", "Węglowodany", "Kalorie", "Gram" };
        sessionTableModel = new DefaultTableModel(columns, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(sessionTableModel);
        table.setShowHorizontalLines(false);
        return table;
    }

    public void addRow(String name, double protein, double fat, double carbohydrates, double calories, double grams) {
        Object[] row = new Object[6];
        row[0] = name;
        row[1] = protein;
        row[2] = fat;
        row[3] = carbohydrates;
        row[4] = calories;
        row[5] = grams;
        sessionTableModel.addRow(row);
    }

    public void removeRow(int i) {
        sessionTableModel.removeRow(i);
    }

    public void clearSession() {
        int rows = sessionTable.getRowCount();
        for (int i = rows; i > 0; i--) {
            sessionTableModel.removeRow(i - 1);
        }
    }

    public void updateSum() {
        // remove all values from the sumTable
        for (int columns = 1; columns < sumTable.getColumnCount(); columns++) {
            sumTable.setValueAt(0.0, 0, columns);
        }
        // sum new values present in the sessionTable
        for (int columns = 1; columns < sessionTable.getColumnCount(); columns++) {
            double sum = 0;
            for (int rows = 0; rows < sessionTable.getRowCount(); rows++) {
                double temp = (Double) sessionTable.getValueAt(rows, columns);
                sum = sum + temp;
                sumTable.setValueAt(df2.format(sum).replace(',', '.'), 0, columns);
            }
        }
    }

    public DefaultListModel<String> getListModel() {
        return list1;
    }

    public int getGrams() {
        return Integer.valueOf(howManyGrams.getText());
    }

    public String getSelectedItem() {
        return list.getSelectedValue();
    }

    public DefaultTableModel getSessionTableModel() {
        return sessionTableModel;
    }

    public int getSelectedRow() {
        return sessionTable.getSelectedRow();
    }

    public void addAddButtonListener(ActionListener aal) {
        addButton.addActionListener(aal);
    }

    public void addRemoveButtonListener(ActionListener ral) {
        removeButton.addActionListener(ral);
    }

    public void addClearButtonListener(ActionListener cal) {
        clearButton.addActionListener(cal);
    }

    public void addSaveSessionButtonListener(ActionListener ssal) {
        saveSession.addActionListener(ssal);
    }

    public void addLoadSessionButtonListener(ActionListener lsal) {
        loadSession.addActionListener(lsal);
    }

    public void addRemoveSessionButtonListener(ActionListener rsal) {
        removeSession.addActionListener(rsal);
    }

    public void addAddProductButtonListener(ActionListener apal) {
        addProduct.addActionListener(apal);
    }

    public void addRemoveProductButtonListener(ActionListener rpal) {
        removeProduct.addActionListener(rpal);
    }
}
