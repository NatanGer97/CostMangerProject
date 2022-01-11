package il.ac.hit.costmanager;

import java.util.ArrayList;
import java.util.Collection;
/**
 * IView is an interface which represents the View which represent the UI of the Application
 */
public interface IView
{
    void initialize();
    void login();
    void logout();
    void start();
    void showMessage(Message message);
    void setIViewModel(IViewModel viewModel);
    void showAllCosts(Collection<CostItem> allCostsList);
    void setCategoryList(ArrayList<Category> categoryList);
    void setUserName(String userName);
    void showFilteredCosts(Collection<CostItem> allCostsList);
}
