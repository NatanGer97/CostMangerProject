package il.ac.hit.costmanager;

import java.util.ArrayList;
import java.util.Collection;

public interface IView {
    public void initialize();
    public void login();
    public void logout();
    public void start();
    public void showMessage(Message message);
    public void setIViewModel(IViewModel viewModel);
    public void showAllCosts(Collection<CostItem> allCostsList);
    public void setCategoryList(ArrayList<Category> categoryList);
    public void setUserName(String userName);
    public void showFilteredCosts(Collection<CostItem> allCostsList);
}
