package il.ac.hit.costmanager;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class ModelTest
{
    private Model model;
    @BeforeEach
    void setUp()
    {
//        try
//        {
//            model = new Model("Alexi");
//
//        }
//        catch (CostManagerException e)
//        {
//            e.printStackTrace();
//        }

    }

    @AfterEach
    void tearDown()
    {
        model = null;




    }

    @Test
    void addCost()
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(model.connectionString, "natan", "1234");
            CostItem costItem = new CostItem("Avi",150,"fuel ","Car stuff","ISL");
            int oldSize  = model.getAllCosts(costItem.getUserName()).size();
            model.addCost(costItem);
            assertEquals(model.getAllCosts(costItem.getUserName()).size(),oldSize+1);

        }
        catch (SQLException | CostManagerException e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                if (connection != null)
                {
                    connection.createStatement().executeUpdate("DELETE FROM `costmanagerdb`");

                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    @Test
    void removeCost()
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(model.connectionString, "natan", "1234");
            CostItem costItem = new CostItem("Avi",150,"fuel ","Car stuff","ISL");
            int oldSize = model.getAllCosts(costItem.getUserName()).size();
            model.addCost(costItem);
            model.removeCost(costItem);
            assertEquals(oldSize,model.getAllCosts(costItem.getUserName()).size());



        }
        catch (SQLException | CostManagerException e)
        {
            e.printStackTrace();
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

    @Test
    void removeCostByDateRange()
    {

    }

    @Test
    void addCategory() throws CostManagerException
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(model.connectionString, "natan", "1234");
            Category categoryItem = new Category("TestCategory");
            int oldSize  = model.getAllCategories("").size();
            model.addCategory(categoryItem,"");
            assertEquals(model.getAllCategories("").size(),oldSize+1);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                if (connection != null)
                {
                    connection.createStatement().executeUpdate("DELETE FROM `categories`");

                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    @Test
    void removeCategory() throws CostManagerException
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(model.connectionString, "natan", "1234");
            Category categoryItem = new Category("TestCategory");
            int oldSize  = model.getAllCategories("").size();
            model.addCategory(categoryItem,"");
            model.removeCategory(categoryItem.getCategoryName());
            assertEquals(model.getAllCategories("").size(),oldSize);

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                if (connection != null)
                {
                    connection.createStatement().executeUpdate("DELETE FROM `categories`");

                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    @Test
    void filterByUserName()
    {
        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection(model.connectionString, "natan", "1234");
            String sqlSelectQuery = "SELECT * FROM `costmanagerdb` WHERE `userName` =  ?" ;

            CostItem costItem = new CostItem("Avi",150,"fuel ","Car stuff","ISL");
            model.addCost(costItem);
            PreparedStatement statement = connection.prepareStatement(sqlSelectQuery);
            statement.setString(1,costItem.getUserName());
            ResultSet resultSet =  statement.executeQuery();
            while (resultSet.next())
            {
                assertNotNull(resultSet.getString("userName"));
                assertEquals(resultSet.getString("userName"),costItem.getUserName());

            }

            model.removeCost(costItem);

        }
        catch (SQLException | CostManagerException e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                if (connection != null)
                {
//                    connection.createStatement().executeUpdate("DELETE FROM `costmanagerdb`");

                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}