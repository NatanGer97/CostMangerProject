package il.ac.hit.costmanager;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class Model implements IModel
{

    public String driverFullQualifiedName = "com.mysql.jdbc.Driver";
    public String connectionString = "jdbc:mysql://localhost:3306/costmanger";
    private final String dataBaseUserName = "natan";
    private final String dataBasePassword = "1234";
    private String currentUser ;

    public Model() throws CostManagerException
    {
        try
        {
            Class.forName(driverFullQualifiedName);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            throw new CostManagerException("Error with the driver manager", e);
        }

    }

    @Override
    public String getCurrentUser() {return currentUser;}
    public void setCurrentUser(String currentUser) {this.currentUser = currentUser;}

    /**
     * A method which connects the user to the database
     * if the username is not in the users table a new row will be created in the database
     * @param userName The name of the user to connect
     * @throws CostManagerException
     */
    @Override
    public void connectUser(String userName) throws CostManagerException
    {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);
            String getAllUsersQuery =  "SELECT * FROM `users`";
            String findSpecificUserQuery = "SELECT  *  FROM `users` WHERE `userName` = ?" ;
            PreparedStatement allUserStatement = connection.prepareStatement(getAllUsersQuery);
            ResultSet allUserResult = allUserStatement.executeQuery();

            //users table isn't empty
            if (allUserResult.next()) {
                PreparedStatement specificUserStatement = connection.prepareStatement(findSpecificUserQuery);
                specificUserStatement.setString(1,userName);
                ResultSet specificUserResultSet = specificUserStatement.executeQuery();

                //Check if the user already exists in the table
                boolean isUserExist = specificUserResultSet.next();

                //The user doesn't exist a new one is created
                if (!isUserExist) {
                    // move to the last row in the users table
                    allUserResult.last();
                    PreparedStatement statement = connection.prepareStatement(
                            "INSERT INTO `users`(`id`, `userName`) VALUES(?,?)");

                    //Get the value of the last id in the table , set the new user id : last id + 1
                    statement.setInt(1,allUserResult.getInt("id") + 1 );
                    statement.setString(2,userName);

                    //The insert was successful
                    if (statement.executeUpdate() == 1) {
                        //adding the default categories for the new user
                        addDefaultCategories(userName);
                    }
                    else {
                        throw new CostManagerException("Error while inserting new user into the database");
                    }
                }
            }
            //The users table is empty , so we insert the first row of the table
            else {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO `users`(`id`, `userName`) VALUES(?,?)");
                statement.setInt(1,1);
                statement.setString(2,userName);

                //The insert was successful
                if (statement.executeUpdate() == 1) {
                    addDefaultCategories(userName);
                    System.out.println("New user was created");
                    System.out.println("new user is:"+userName);
                }
                else {
                    throw new CostManagerException("Error while trying to create the first user");
                }
            }
        } catch (SQLException e) {
            throw new CostManagerException("Error while connecting the user");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        setCurrentUser(userName);
    }

    /**
     * A method which creates a list of default categories for a new user in the database
     * @param userName The username of which the categories are created for
     * @throws CostManagerException
     */
    @Override
    public void addDefaultCategories(String userName) throws CostManagerException {
        List<String> defaultCategories = new ArrayList<>();
        //Default categories
        defaultCategories.add("General");
        defaultCategories.add("Transport");
        defaultCategories.add("Hygiene");
        defaultCategories.add("Clothes");
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);
            for (String category : defaultCategories) {
                PreparedStatement insertNewCategoryStatement = connection.prepareStatement("INSERT INTO `categories`(`categoryName` ,`userName`) VALUES (?,?)");
                insertNewCategoryStatement.setString(1, category);
                insertNewCategoryStatement.setString(2, userName);
                if (insertNewCategoryStatement.executeUpdate() == 0) {
                    throw new CostManagerException("Error while adding new category");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addCost(CostItem costItem) throws CostManagerException
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);
            String sqlInsertQuery = "INSERT INTO `costmanagerdb` (`userName`,`Category`, `CostSum`, `Currency`, `Description`,`Date Added`) VALUES(?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sqlInsertQuery);
            addCategory(new Category(costItem.getCategory()),costItem.getUserName());

            statement.setString(1, costItem.getUserName());
            statement.setString(2, costItem.getCategory());
            statement.setDouble(3, costItem.getCostSum());
            statement.setString(4, costItem.getCurrency());
            statement.setString(5, costItem.getDescription());
            statement.setDate(6, costItem.getDate());
            statement.executeUpdate();
            // add new category;
        }
        catch (SQLException e)
        {
            throw new CostManagerException("Error while trying to add a new cost to the db");
        }

        finally {
            try {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeCost(CostItem itemToRemove) throws CostManagerException
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);
            String sqlDeleteUpdate = "DELETE FROM `costmanagerdb` WHERE `UserName` = ? AND `CostID` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteUpdate);
            preparedStatement.setString(1, itemToRemove.getUserName());
            preparedStatement.setInt(2, itemToRemove.getCostId());
            int res = preparedStatement.executeUpdate();
            System.out.println(res);
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new CostManagerException("Couldn't remove the item");
        }
        finally {
            try {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeCostByDateRange(Date leftDateBoundary, Date rightDateBoundary) throws CostManagerException
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);
            String sqlDeleteUpdate = "DELETE FROM `costmanagerdb`  WHERE `Date Added` BETWEEN ? AND ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteUpdate);
            preparedStatement.setDate(1, leftDateBoundary);
            preparedStatement.setDate(2, rightDateBoundary);
            int queryResult = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new CostManagerException("Couldn't remove the items within the given range : " + leftDateBoundary.toString() + " - " + rightDateBoundary.toString());
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void addCategory(Category categoryToAdd ,String currentUser) throws CostManagerException {
        Connection connection = null;
        boolean isCategoryAlreadyPresent = false;
        List<Category> currentCategoriesList = (List<Category>) getAllCategories(currentUser);
        for (Category category: currentCategoriesList) {
            if(Objects.equals(category.getCategoryName(), categoryToAdd.getCategoryName()))
            {
                isCategoryAlreadyPresent = true;
            }
        }
        if (!isCategoryAlreadyPresent) {
            try {
                connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);
                String isCategoryExistQuery = "SELECT `categoryName` FROM `categories` WHERE categoryName IN " +
                        "( SELECT `categoryName` FROM `categories` WHERE `categoryName` = ? AND 'userName' = ?)";

                PreparedStatement isCategoryExist = connection.prepareStatement(isCategoryExistQuery);
                PreparedStatement insertNewCategoryStatement = connection.prepareStatement("INSERT INTO `categories`(`categoryName` ,`userName`) VALUES (?,?)");
                insertNewCategoryStatement.setString(1, categoryToAdd.getCategoryName());
                insertNewCategoryStatement.setString(2, currentUser);
                isCategoryExist.setString(1, categoryToAdd.getCategoryName());
                isCategoryExist.setString(2, currentUser);
                ResultSet resultSet = isCategoryExist.executeQuery();
                if (!resultSet.next()) {
                    if (insertNewCategoryStatement.executeUpdate() != 0) {
                        System.out.println("New Category was added!");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new CostManagerException("error in add new category");
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Collection<CostItem> getAllCosts(String userName) throws CostManagerException
    {
        Connection connection = null;
        List<CostItem> costItems = new ArrayList<>();
        try
        {
            connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);
            PreparedStatement getAllCostsStatement = connection.prepareStatement("SELECT * FROM `costmanagerdb` WHERE `userName` = ?");
            getAllCostsStatement.setString(1,userName);
            ResultSet allCostsResultSet = getAllCostsStatement.executeQuery();
            while (allCostsResultSet.next())
            {
                String category = allCostsResultSet.getString("category");
                String currency = allCostsResultSet.getString("currency");
                String description = allCostsResultSet.getString("description");
                Date date = allCostsResultSet.getDate("Date Added");
                double costSum = allCostsResultSet.getDouble("costSum");
                int costID = allCostsResultSet.getInt("CostID");
                costItems.add(new CostItem(userName, costID , costSum, description, category, currency ,date));
            }

        } catch (SQLException e) {
            throw new CostManagerException("Error while getting all the cost items from the db");
        }
        finally
        {
            try {
                if (connection != null)
                {
                    connection.close();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return  costItems;
    }

    @Override
    public Collection<Category> getAllCategories(String currentUser) throws CostManagerException
    {
        Connection connection = null;
        List<Category> categoryList = new ArrayList<>();
        try
        {
            connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);
            PreparedStatement getAllCategoriesStatement = connection.prepareStatement("SELECT * FROM `categories` WHERE `userName` = ?");
            getAllCategoriesStatement.setString(1,currentUser);
            ResultSet allCategoriesResultSet = getAllCategoriesStatement.executeQuery();
            while (allCategoriesResultSet.next())
            {
                String category = allCategoriesResultSet.getString("categoryName");

                categoryList.add(new Category(category));
            }

        } catch (SQLException e) {
            throw new CostManagerException("Error while getting the collection of categories from the db");
        }
        finally
        {
            try {
                if (connection != null)
                {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return  categoryList;
    }

    @Override
    public void removeCategory(String categoryToRemove) throws CostManagerException
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);
            String sqlDeleteUpdate = "DELETE FROM `categories` WHERE `categoryName` = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlDeleteUpdate);
            preparedStatement.setString(1, categoryToRemove);
            int res = preparedStatement.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new CostManagerException("Error while trying to remove category " + categoryToRemove,e);
        }

    }

    @Override
    public Collection<CostItem> getCostByDateRange(String leftDateBoundary, String rightDateBoundary,String currentUser) throws CostManagerException
    {
        Connection connection = null;
        List<CostItem> costItems = new ArrayList<>();
        try
        {
            connection = DriverManager.getConnection(connectionString, dataBaseUserName, dataBasePassword);
            PreparedStatement getFilteredCostsStatement = connection.prepareStatement(
                    "SELECT * FROM `costmanagerdb` WHERE `Date Added` BETWEEN ? AND ? AND `userName` = ?");
            getFilteredCostsStatement.setDate(1,Date.valueOf(leftDateBoundary));
            getFilteredCostsStatement.setDate(2,Date.valueOf(rightDateBoundary));
            getFilteredCostsStatement.setString(3,getCurrentUser());
            ResultSet allCostsResultSet = getFilteredCostsStatement.executeQuery();

            while (allCostsResultSet.next())
            {
                String category = allCostsResultSet.getString("category");
                String currency = allCostsResultSet.getString("currency");
                String description = allCostsResultSet.getString("description");
                Date date = allCostsResultSet.getDate("Date Added");
                int costSum = allCostsResultSet.getInt("costSum");
                int costID = allCostsResultSet.getInt("CostID");
                costItems.add(new CostItem(getCurrentUser(), costID , costSum, description, category, currency,date));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally
        {
            try {
                if (connection != null)
                {
                    connection.close();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return  costItems;
    }
}
