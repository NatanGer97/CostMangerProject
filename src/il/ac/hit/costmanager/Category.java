package il.ac.hit.costmanager;

/**
 * Category represents an object of category which is used in costItem
 */
public class Category
{
    String categoryName;

    /**
     * Category constructor
     * @param categoryName the category name
     */
    public Category(String categoryName){
        setCategoryName(categoryName);
    }

    /**
     * categoryName getter
     * @return the category name
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * categoryName setter
     * @param categoryName the category name
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
