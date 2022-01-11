package il.ac.hit.costmanager;

import java.sql.Date;
import java.util.Collection;

/**
 * IModel is an interface which represents a model who deal with a database
 */
public interface IModel {

    public void addCategory(Category categoryToAdd ,String currentUser) throws CostManagerException;
    public Collection<CostItem> getAllCosts(String userName) throws CostManagerException;
    public Collection<Category>  getAllCategories(String currentUser) throws CostManagerException;
    public Collection<CostItem>  getCostByDateRange(String leftDateBoundary , String rightDateBoundary,String currentUser) throws CostManagerException;
    public void removeCategory(String categoryToRemove) throws CostManagerException;
    public void addCost(CostItem costItem) throws CostManagerException;
    public void removeCost(CostItem itemToRemove) throws CostManagerException;
    public void connectUser(String userName)throws CostManagerException;
    public String getCurrentUser() throws CostManagerException;
    public void addDefaultCategories(String userName) throws CostManagerException;


    public void removeCostByDateRange(Date leftDateBoundary , Date rightDateBoundary) throws CostManagerException;
    public void setCurrentUser(String currentUser) throws CostManagerException;
}
