package il.ac.hit.costmanager;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

/**
 * Implements the IView model
 * View represent the UI of the application
 * holds reference to the viewModel
 */
public class View implements IView
{

    private IViewModel viewModel;
    private JFrame frameMain;
    private JTable tableCostItems;
    private DefaultListModel<String> jListModel;
    private JList<String> jListCategories;
    private JScrollPane scrollPaneForItemsTable, scrollPaneForCategories;
    private DefaultTableModel defaultTableCostItems;
    private JButton buttonAddCostItem, buttonDeleteCostItem, buttonLogout, buttonAddCategory, buttonFilterCostItemsByDates;
    private JPanel panelAddItem, panelButtons, panelAddItemAndButtonsPanels;
    private JTextField textFieldCostSum, textFieldDescription, textFieldCurrency;
    private JTextArea textAreaCostSum, textAreaDescription, textAreaCategory, textAreaCurrency;
    private final String[] colNamesForTable = {"Category", "Description", "Cost SUM", "Currency", "Date Added"};
    private String userName;
    private ArrayList<Category> currentCategoryList;

    /**
     * initialize is the first function that start and while the View Object is created
     * Her role is equivalent to the Ctor
     * initialize all the components
     * And made the login process of the user
     * This is the Entry Point of this Object
     */
    @Override
    public void initialize()
    {
        login();
        frameMain = new JFrame();
        frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameMain.setTitle("Cost Manager");
        buttonAddCostItem = new JButton("Add Item");
        buttonDeleteCostItem = new JButton("Delete selected item");
        buttonLogout = new JButton("Logout");
        buttonAddCategory = new JButton("Add Category");
        buttonFilterCostItemsByDates = new JButton("Filter by Dates");
        panelAddItem = new JPanel();
        panelButtons = new JPanel();
        panelButtons.setBackground(new Color(153, 204, 255));
        panelAddItemAndButtonsPanels = new JPanel();
        tableCostItems = new JTable();
        defaultTableCostItems = new DefaultTableModel(0, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        textFieldCostSum = new JTextField();
        textFieldDescription = new JTextField();
        textFieldCurrency = new JTextField();
        textAreaCostSum = new JTextArea("Cost");
        textAreaDescription = new JTextArea("Description");
        textAreaCategory = new JTextArea("Category");
        textAreaCurrency = new JTextArea("Currency");
        textAreaCategory.setEditable(false);
        textAreaDescription.setEditable(false);
        textAreaCostSum.setEditable(false);
        textAreaCurrency.setEditable(false);
        defaultTableCostItems.setColumnIdentifiers(colNamesForTable);
        tableCostItems.setModel(defaultTableCostItems);
        tableCostItems.setRowSelectionAllowed(true);
        tableCostItems.setBackground(new Color(102, 178, 255));
        tableCostItems.getTableHeader().setBackground(new Color(0, 128, 255));
        jListModel = new DefaultListModel<>();
        jListCategories = new JList<>(jListModel);
        scrollPaneForItemsTable = new JScrollPane(tableCostItems);
        scrollPaneForCategories = new JScrollPane(jListCategories);
        scrollPaneForItemsTable.getViewport().setBackground(new Color(204, 229, 255));
        jListCategories.setVisibleRowCount(0);
        textAreaCategory.setBackground(new Color(153, 204, 255));
        textAreaCostSum.setBackground(new Color(153, 204, 255));
        textAreaCurrency.setBackground(new Color(153, 204, 255));
        textAreaDescription.setBackground(new Color(153, 204, 255));
    }

    /**
     * Responsible to the show the login view to user
     * and start the login flow
     */
    @Override
    public void login()
    {
        String inputUserName = JOptionPane.showInputDialog(null, "Enter Username", "Login", JOptionPane.QUESTION_MESSAGE);
        if (inputUserName == null)
            System.exit(0);
//        LoginView loginView = new LoginView();
//        loginView.init();
//        loginView.start();
        viewModel.setUserName(inputUserName);
        this.userName = inputUserName;
    }

    /**
     * Responsible show the logout popup dialog
     * Handel the user logout request and starts the flow
     */
    @Override
    public void logout()
    {
        frameMain.setVisible(false);
        String newUsername = JOptionPane.showInputDialog(frameMain, "Enter Username", "Login", JOptionPane.QUESTION_MESSAGE);
        while (Objects.equals(newUsername, null) || Objects.equals(newUsername, ""))
        {
            JOptionPane.showMessageDialog(frameMain, "Wrong username input");
            newUsername = JOptionPane.showInputDialog(frameMain, "Enter Username", "Login", JOptionPane.QUESTION_MESSAGE);
        }
        this.userName = newUsername;
        viewModel.logOut(newUsername);
        frameMain.setVisible(true);
    }

    /**
     * start, place all the view components on the main frame
     * and make them visible and functional
     * add listeners for all the buttons'
     * init the cost table according to current user.
     */
    @Override
    public void start()
    {
        //  locate and place the add item panel.
        panelAddItem.setLayout(new GridLayout(2, 4));
        panelAddItem.add(textAreaCategory, 0);
        panelAddItem.add(textAreaDescription, 1);
        panelAddItem.add(textAreaCostSum, 2);
        panelAddItem.add(textAreaCurrency, 3);
        panelAddItem.add(scrollPaneForCategories, 4);
        panelAddItem.add(textFieldDescription, 5);
        panelAddItem.add(textFieldCostSum, 6);
        panelAddItem.add(textFieldCurrency, 7);

        // place buttons on panel that contain all the buttons belongs to this view.
        panelButtons.setLayout(new FlowLayout());
        panelButtons.add(buttonAddCostItem);
        panelButtons.add(buttonDeleteCostItem);
        panelButtons.add(buttonLogout);
        panelButtons.add(buttonAddCategory);
        panelButtons.add(buttonFilterCostItemsByDates);

        panelAddItemAndButtonsPanels.setLayout(new GridLayout(2, 1));
        panelAddItemAndButtonsPanels.add(panelAddItem, 0);
        panelAddItemAndButtonsPanels.add(panelButtons, 1);

        frameMain.setLayout(new BorderLayout());
        frameMain.add(panelAddItemAndButtonsPanels, BorderLayout.SOUTH);
        viewModel.getAllCosts(userName);
        viewModel.getAllCategories(); // display available costs in table
        jListCategories.setLayoutOrientation(JList.VERTICAL);
        frameMain.add(scrollPaneForItemsTable, BorderLayout.CENTER);
        frameMain.setSize(700, 700);
        frameMain.setLocationRelativeTo(null);
        frameMain.setVisible(true);

        // set listeners to buttons
        buttonAddCategory.addActionListener(e -> addCategory());
        buttonLogout.addActionListener(e -> logout());
        buttonDeleteCostItem.addActionListener(e -> deleteItem());
        buttonAddCostItem.addActionListener(e -> addItem());
        buttonFilterCostItemsByDates.addActionListener(e -> filterCosts());
    }

    /*
        get new category from user and move it to the viewModel in order to add her to the DB
        make also a validation of the input
     */
    private void addCategory()
    {
        String newCategoryName = JOptionPane.showInputDialog(frameMain, "Enter the name of the new category", "Add new category", JOptionPane.QUESTION_MESSAGE);
        if (newCategoryName != null && !Objects.equals(newCategoryName, "") && !Objects.equals(newCategoryName, "\n"))
        {
            viewModel.addNewCategory(newCategoryName);
        }
    }
    /*
        get new data for new cost from user
        validate the data and pass new cost obj to view model
     */
    private void addItem()
    {
        if (costItemInputValidation())
        {
            double costSum = Double.parseDouble(textFieldCostSum.getText());
            CostItem costItem = new CostItem(userName, costSum, textFieldDescription.getText(),
                    currentCategoryList.get(jListCategories.getFirstVisibleIndex()).getCategoryName(), textFieldCurrency.getText());

            viewModel.addCostItem(costItem);
            // clear the input fields
            textFieldCurrency.setText("");
            textFieldDescription.setText("");
            textFieldCostSum.setText("");
        }
    }
    // get if possible target item for removing and pass to vm in order to delete him
    private void deleteItem()
    {
        if (tableCostItems.getSelectedRow() == -1) // -1 -> indicate that no item was selected
        {
            JOptionPane.showMessageDialog(frameMain, "You have to select the cost you wish to delete");
        }
        else
        {
            viewModel.deleteCostItem(tableCostItems.getSelectedRow());
        }
    }

    private void filterCosts()
    {
        if (tableCostItems.getModel().getRowCount() == 0)
        {
            JOptionPane.showMessageDialog(frameMain, "Table is Empty");
        }
        else
        {
            ChooseDateView chooseDateView = new ChooseDateView();
            chooseDateView.start();
        }
    }


    /**
     * Validate the user input whether new Cost item is valid form or not.
     *
     * @return true if the new cost has valid form
     */
    private boolean costItemInputValidation()
    {
        if (textFieldCostSum.getText().isEmpty() || textFieldDescription.getText().isEmpty()
                || textFieldCurrency.getText().isEmpty() || currentCategoryList.isEmpty() || Character.isLetter(textFieldCostSum.getText().charAt(0)))
        {
            JOptionPane.showMessageDialog(frameMain, "Invalid input");
            return false;
        }
        return true;
    }

    /**
     * set the view model which through him view get data and
     * transport data
     * initialize the vm reference
     *
     * @param viewModel the view model reference the view will hold
     */
    @Override
    public void setIViewModel(IViewModel viewModel)
    {
        this.viewModel = viewModel;
    }

    /**
     * Show the view who contains specific cost items
     * that filtered from all cost according to selected time period
     *
     * @param allCostsList an instance of Cost item Collection that hold filters items
     */
    @Override
    public void showFilteredCosts(Collection<CostItem> allCostsList)
    {
        FilterCostsView filterCostsView = new FilterCostsView(allCostsList);
        filterCostsView.start();
    }

    /**
     * Responsible for displaying user costs in Table with cost attributes
     *
     * @param allCostsList Collection of CostItems that contains
     *                     all exiting costs
     */
    @Override
    public void showAllCosts(Collection<CostItem> allCostsList)
    {
        /* loop over each item in collection
            create new row and add to the table
         */
        defaultTableCostItems.setRowCount(0);
        for (CostItem costItem : allCostsList)
        {
            defaultTableCostItems.addRow(new Object[]{costItem.getCategory(), costItem.getDescription(),
                    costItem.getCostSum(), costItem.getCurrency(), costItem.getDate().toString()});
        }
    }

    /**
     * Provide data to the list model which inflate the Category List component
     * Which provide and show the optional cost categories
     *
     * @param categoryList Array List containing categories objects
     */
    @Override
    public void setCategoryList(ArrayList<Category> categoryList)
    {
        if (categoryList != null)
        {
            currentCategoryList = categoryList;
            jListModel.clear();
            for (int i = 0; i < categoryList.size(); i++)
            {
                jListModel.add(i, categoryList.get(i).getCategoryName());
            }
        }
    }

    /**
     * Get string that represent the current user how uses the app
     * and assign it to userName member field
     *
     * @param userName string which represent the user
     */
    @Override
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    /**
     * showMessage get Message Object and display popUp dialog over the view
     * and describe unique message to user     *
     *
     * @param message Message object is the unique message that user sees in the dialog
     */
    @Override
    public void showMessage(Message message)
    {
        JOptionPane.showMessageDialog(frameMain, message.getMessageContent());
    }

    /**
     * Inner class represent the Date Chooser View
     * Provide user option to choose Date range in order to filter costs by
     */
    public class ChooseDateView
    {
        // components of  ChooseDateView
        private JDialog chooseDateJFrame;
        private JLabel title, leftDate, rightDate;
        private JDateChooser jDateChooserLeft;
        private JDateChooser jDateChooserRight;
        private JButton dateButton;
        private JPanel datePanel, labelsPanel;
        private JPanel leftPanel, rightPanel;

        /**
         * Constructor of ChooseDateView that init all the components
         * and assign action listeners
         */
        public ChooseDateView()
        {
            // init the view components
            chooseDateJFrame = new JDialog();
            chooseDateJFrame.setTitle("Choose Date");
            datePanel = new JPanel();
            labelsPanel = new JPanel();
            dateButton = new JButton("Choose Date");
            jDateChooserLeft = new JDateChooser();
            jDateChooserLeft.setDateFormatString("yyyy-MM-dd");
            jDateChooserRight = new JDateChooser();
            jDateChooserRight.setDateFormatString("yyyy-MM-dd");
            title = new JLabel("Chosen Date:");
            title.setHorizontalAlignment(SwingConstants.CENTER);
            leftDate = new JLabel("Start Date");
            rightDate = new JLabel("End Date");
            leftPanel = new JPanel();
            rightPanel = new JPanel();

            /* button which takes the chosen date,parsing them into String
             and pass them to the getFilteredCosts function
             */
            dateButton.addActionListener(e -> {
                Date startDate = jDateChooserLeft.getDate();
                Date endDate = jDateChooserRight.getDate();
                String strDateString = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
                String endDateString = new SimpleDateFormat("yyyy-MM-dd").format(endDate);
                chooseDateJFrame.dispose(); // close the frame
                viewModel.getFilteredCosts(userName, strDateString, endDateString);
            });

        }

        /**
         * start responsible for make ChooseDateView visible and display this view to user
         */
        public void start()
        {
            // set and order components on thr view
            datePanel.setLayout(new BorderLayout());
            rightPanel.setLayout(new GridLayout(2, 0));
            rightPanel.add(rightDate);
            rightPanel.add(jDateChooserRight);
            leftPanel.setLayout(new GridLayout(2, 0));
            leftPanel.add(leftDate);
            leftPanel.add(jDateChooserLeft);
            labelsPanel.setLayout(new GridLayout(1, 0));
            datePanel.add(leftPanel, BorderLayout.WEST);
            datePanel.add(rightPanel, BorderLayout.EAST);
            datePanel.add(title, BorderLayout.NORTH);
            labelsPanel.add(dateButton);
            datePanel.add(labelsPanel, BorderLayout.SOUTH);
            chooseDateJFrame.add(datePanel);
            chooseDateJFrame.setSize(400, 200);
            chooseDateJFrame.setResizable(false);
            chooseDateJFrame.setLocationRelativeTo(frameMain);
            chooseDateJFrame.setVisible(true);
        }
    }

    /**
     * Inner class which represents a View
     * that show cost items between specific dated rage
     */
    public class FilterCostsView
    {
        private JFrame filterFrame;

        private JPanel mainPanel;
        private JScrollPane scrollPaneToTable;
        private JTable filteredCostsTable;
        private final DefaultTableModel tableModel;
        private JLabel emptyResultLabel;
        boolean emptyData; // flag to indicate whether the filter cost data is empty or not

        // set the emptyData with given value
        private void setEmptyData(boolean emptyData)
        {
            this.emptyData = emptyData;
        }

        /**
         * FilterCostsView Constructor which receives Collection of CostsItem as parameter
         *
         * @param filteredCostsList Collection of CostsItem that represents a filtered costs item
         */
        public FilterCostsView(Collection<CostItem> filteredCostsList)
        {
            filterFrame = new JFrame("Filter Window");
            emptyResultLabel = new JLabel("Empty");
            emptyResultLabel.setVisible(false);
            mainPanel = new JPanel();
            filteredCostsTable = new JTable();
            tableModel = new DefaultTableModel(0, 0);
            tableModel.setColumnIdentifiers(colNamesForTable);
            filteredCostsTable.setModel(tableModel);
            filteredCostsTable.setRowSelectionAllowed(true);
            filteredCostsTable.setBackground(new Color(102, 178, 255));
            filteredCostsTable.getTableHeader().setBackground(new Color(0, 128, 255));
            scrollPaneToTable = new JScrollPane(filteredCostsTable);
            scrollPaneToTable.getViewport().setBackground(new Color(204, 229, 255));
            setFilteredCostsTable(filteredCostsList); // fill the table
            setEmptyData(filteredCostsList.isEmpty()); // determine if the table is empty


        }

        /**
         * start is a method who place the components
         * and makes the view visible
         */
        public void start()
        {
            mainPanel.setLayout(new BorderLayout());
            /* if this condition is true -> main panel would set and have
            unique panel and not the table panel
             */
            if (emptyData)
            {
                mainPanel.add(emptyResultLabel, BorderLayout.CENTER);
                emptyResultLabel.setVisible(true);
                emptyResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }
            else
            {
                mainPanel.add(scrollPaneToTable, BorderLayout.CENTER);
            }

            filterFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            filterFrame.setSize(400, 400);
            filterFrame.add(mainPanel);
            filterFrame.setLocationRelativeTo(frameMain);
            filterFrame.setVisible(true);

        }

        /*
            setFilteredCostsTable fill the table with filtered items
         */
        public void setFilteredCostsTable(Collection<CostItem> allCostsList)
        {

            tableModel.setRowCount(0);
            for (CostItem costItem : allCostsList)
            {
                tableModel.addRow(new Object[]{costItem.getCategory(), costItem.getDescription(),
                        costItem.getCostSum(), costItem.getCurrency(), costItem.getDate().toString()});
            }
        }


    }

    public class LoginView
    {
        JFrame loginFrame;
        JPanel textAreaAndFieldPanel;
        JButton loginButton;
        JTextArea loginTextArea;
        JTextField loginTextField;

        public void init()
        {
            loginFrame = new JFrame();
            loginFrame.setTitle("Login");
            textAreaAndFieldPanel = new JPanel();
            loginButton = new JButton("LOGIN");
            loginTextArea = new JTextArea("Enter Username");
            loginTextField = new JTextField();
        }

        public void start()
        {
            loginFrame.setLayout(new BorderLayout());
            textAreaAndFieldPanel.setLayout(new GridLayout(2, 1));
            textAreaAndFieldPanel.add(loginTextArea, 0);
            textAreaAndFieldPanel.add(loginTextField, 1);
            loginFrame.add(textAreaAndFieldPanel, BorderLayout.CENTER);
            loginFrame.add(loginButton, BorderLayout.SOUTH);

            loginFrame.setSize(300, 300);
            loginFrame.setVisible(true);
            loginFrame.setLocationRelativeTo(frameMain);

            loginButton.addActionListener(e -> {
                userName = loginTextField.getText();
                loginFrame.dispose();
            });
        }
    }
}


