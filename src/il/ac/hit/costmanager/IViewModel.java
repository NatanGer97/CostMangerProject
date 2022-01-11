package il.ac.hit.costmanager;

public interface IViewModel {
    public void addCostItem(CostItem costItem);
    public void getAllCosts(String userName);
    public void setView(IView view);
    public void setModel(IModel model);
    public void setUserName(String userName);
    public void getAllCategories();
    public void deleteCostItem(int indexOfCostItemToDelete);
    public void logOut(String newUsername);
    public void addNewCategory(String categoryName);
    public void getFilteredCosts(String userName,String startDate,String endDate);
}
