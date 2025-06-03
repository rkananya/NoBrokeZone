package com.mongoapp.view;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RefineryUtilities;


public class Main extends JFrame {
    private static Expense expenseTracker = new Expense();
    private static DefaultTableModel tableModel;
    private static JLabel balanceLabel;
    private static JLabel percentSavedLabel;
    private static JTextField incomeField;
    private static JTextField expenseField;
    private static JTextField descriptionField;
    private static JTextField targetSavingsField;
    private static JTextField userField;
    private static JComboBox<String> categoryComboBox;
    private static JPanel cardPanel;
    private static JPanel incomePanel;
    private static JPanel expensePanel;
    private static JPanel tablePanel;
    private static JPanel chartPanel;
    private static JPanel statusPanel;
    private static JPanel userPanel;
    private static String user = "Ananya";


    public Main(String title) {
        super(title);
        initUI();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main frame = new Main("No broke zone -" + user);
            frame.setSize(600, 700); 
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.setBackground(new Color(240, 221, 245));
            RefineryUtilities.centerFrameOnScreen(frame);
        });
    }


    private void initUI() {
        JPanel maiPanel = new JPanel(new BorderLayout());
         statusPanel = new JPanel();
        statusPanel.setBackground(new Color(85,105,103));
        balanceLabel = new JLabel("Balance: $0.0");
        balanceLabel.setForeground(Color.WHITE);
        percentSavedLabel = new JLabel("Percent Saved: 0%");
        percentSavedLabel.setForeground(Color.WHITE);
        statusPanel.add(balanceLabel);
        statusPanel.add(percentSavedLabel);

        maiPanel.add(statusPanel,BorderLayout.NORTH);
      
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(new Color(91,129,117));
        JButton addIncomeBtn = createStyledButton("Add Income");
        JButton addExpenseBtn = createStyledButton("Add Expense");
        JButton showExpensesBtn = createStyledButton("Show Expenses");
        JButton showChartBtn = createStyledButton("Show Chart");

        buttonPanel.add(addIncomeBtn);
        buttonPanel.add(addExpenseBtn);
        buttonPanel.add(showExpensesBtn);
        buttonPanel.add(showChartBtn);

        maiPanel.add(buttonPanel,BorderLayout.CENTER);
       
        incomePanel = new JPanel(new GridLayout(3, 2));
        incomePanel.setBackground(new Color(220,255,243));
        incomeField = new JTextField();
        incomeField.setPreferredSize(new Dimension(150,20));
        targetSavingsField = new JTextField();
        JButton confirmIncomeBtn = createStyledButton("Confirm Income");
        incomePanel.add(new JLabel("Enter Income:",SwingConstants.CENTER));
        incomePanel.add(incomeField);
        incomePanel.add(new JLabel("Target Savings:",SwingConstants.CENTER));
        incomePanel.add(targetSavingsField);
        incomePanel.add(confirmIncomeBtn); 
        confirmIncomeBtn.addActionListener(e -> addIncome());

        expensePanel = new JPanel(new GridLayout(4, 2));
        expensePanel.setBackground(new Color(220,255,243));
        expenseField = new JTextField();
        descriptionField = new JTextField();
        String[] categories = {"Food", "Miscellaneous", "Health", "Social Life", "Clothing"};
        categoryComboBox = new JComboBox<>(categories);
        JButton confirmExpenseBtn = createStyledButton("Confirm Expense");
        expensePanel.add(new JLabel("Expense Description:",SwingConstants.CENTER));
        expensePanel.add(descriptionField);
        expensePanel.add(new JLabel("Amount Spent:",SwingConstants.CENTER));
        expensePanel.add(expenseField);
        expensePanel.add(new JLabel("Category:",SwingConstants.CENTER));
        expensePanel.add(categoryComboBox);
        expensePanel.add(confirmExpenseBtn);
        confirmExpenseBtn.addActionListener(e -> addExpense());

        tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(220,255,243));
        String[] columns = {"Date", "Category", "Description", "Amount"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable expenseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(expenseTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
   
        chartPanel = new JPanel(new BorderLayout());
        updateChart(); 

        cardPanel = new JPanel(new CardLayout());
        cardPanel.add(incomePanel, "Income");
        cardPanel.add(expensePanel, "Expense");
        cardPanel.add(tablePanel, "Table");
        cardPanel.add(chartPanel, "Chart");
       
        cardPanel.setBackground(new Color(92,151,130));
        cardPanel.setPreferredSize(new Dimension(700, 500));
        maiPanel.add(cardPanel,BorderLayout.SOUTH);
        add(maiPanel);

        addIncomeBtn.addActionListener(e -> showIncomeFields());
        addExpenseBtn.addActionListener(e -> showExpenseFields());
        showExpensesBtn.addActionListener(e -> showExpenseTable());
        showChartBtn.addActionListener(e -> showChart());
        
        fetchValuesFromDb(user);
    }
    private static void fetchValuesFromDb(String user){
      if(Db.getUser(user)==null){
        Db.createUserIfNotExists(user);
      }
      else{
       double balance = Db.getBalance(user);
       balanceLabel.setText("Balance: $" + String.format("%.2f", balance)); 
       double percentSaved = Db.getPercentSaved(user);
       percentSavedLabel.setText("Percent Saved: " + String.format("%.2f", percentSaved) + "%");
       Expense.setIncome(balance);
      }
    }
    private static void loadExpensesFromDb(String username) {
    tableModel.setRowCount(0); // Clear previous data

    java.util.List<String[]> pastExpenses = Db.getAllExpenses(username);
    if (pastExpenses != null) {
        for (String[] row : pastExpenses) {
            // row = {date, category, description, amount}
            tableModel.addRow(new Object[]{row[0], row[1], row[2], "$" + row[3]});
        }
    }
    System.out.println(pastExpenses);
}

  
    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(205,252,237));
        button.setForeground(Color.BLACK);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setPreferredSize(new Dimension(150, 30));
        return button;
    }

    private static void showIncomeFields() {
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, "Income");
    }
  
    private static void showExpenseFields() {
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, "Expense");
    }


    private static void addIncome() {
        try {
            double income = Double.parseDouble(incomeField.getText());
            double targetSavings = Double.parseDouble(targetSavingsField.getText());
            Db.addIncome(user, income);
            Expense.setIncome(income);
            expenseTracker.setTarget(targetSavings);
            updateBalanceAndPercent();
            JOptionPane.showMessageDialog(null, "Income Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            incomeField.setText("");
            targetSavingsField.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter valid amounts.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void addExpense() {
        String description = descriptionField.getText();
        double amountSpent;
        
        try {
            amountSpent = Double.parseDouble(expenseField.getText());
            if (Expense.getIncome(user) <= 0) {
                JOptionPane.showMessageDialog(null, "Please add income first.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            expenseTracker.addExpense(user,description, categoryComboBox.getSelectedItem().toString(), amountSpent);

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            tableModel.addRow(new Object[]{sdf.format(date), categoryComboBox.getSelectedItem(), description, "$" + amountSpent});
 
            updateBalanceAndPercent();
    
            descriptionField.setText("");
            expenseField.setText("");

            if (expenseTracker.isNearSpendingLimit(user)) {
                JOptionPane.showMessageDialog(null, "Warning: You have reached 90% of your target savings!", "Warning", JOptionPane.WARNING_MESSAGE);
                statusPanel.setBackground(Color.ORANGE);
            }
            else{
                statusPanel.setBackground(new Color(128, 64, 255, 128));
            }
    
            updateChart();
    
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid expense amount.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void updateBalanceAndPercent() {
        double balance = Expense.getIncome(user) - expenseTracker.getTotalExpenses(user);
        double percentSaved = expenseTracker.calculatePercentSaved(user);
        balanceLabel.setText("Balance: $" + String.format("%.2f", balance)); 
        percentSavedLabel.setText("Percent Saved: " + String.format("%.2f", percentSaved) + "%");
        Db.setBalance(user, balance);
        Db.setPercentSaved(user, percentSaved);
        if(percentSaved<=10.00){
            statusPanel.setBackground(Color.RED);
        }
        else{
            statusPanel.setBackground(new Color(128, 64, 255, 128));
        }
    }

    private static void updateChart() {
        chartPanel.removeAll();
        JPanel newChartPanel = createDemoPanel();
        chartPanel.add(newChartPanel, BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }
    private static void showChart() {
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, "Chart");
    }

    private static void showExpenseTable() {
        loadExpensesFromDb(user);
        CardLayout cl = (CardLayout) cardPanel.getLayout();
        cl.show(cardPanel, "Table");
    }

    private static DefaultPieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        Map<String, Double> expenseData = Db.getExpenseData(user);
        for (Map.Entry<String, Double> entry : expenseData.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        return dataset;
    }

     private static JFreeChart createChart(DefaultPieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
            "Expenses by Category", dataset, true, true, false
        );

       
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.decode("#01291C"));
        plot.setOutlineVisible(false);
        plot.setLabelBackgroundPaint(Color.decode("#333333"));
        plot.setLabelPaint(Color.WHITE);
        plot.setLabelOutlinePaint(null);
        plot.setLabelShadowPaint(null);
        plot.setShadowPaint(null); 

     
        plot.setSectionPaint("Food", new Color(255, 99, 132));        
        plot.setSectionPaint("Health", new Color(54, 162, 235)); 
        plot.setSectionPaint("Clothing", new Color(255, 206, 86));       
        plot.setSectionPaint("Miscellaneous", new Color(75, 192, 192));  
        plot.setSectionPaint("Social Life", new Color(153, 102, 255));     

        return chart;
    }


    private static JPanel createDemoPanel() {
        JFreeChart chart = createChart(createDataset());
        return new ChartPanel(chart);
    }
}
