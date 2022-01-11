package il.ac.hit.costmanager;

import java.sql.Date;
import java.time.LocalDate;

/**
 * CostItem represents an object of cost item which is used in the database and UI
 */
public class CostItem
{
    private int costId;
    private double costSum;
    private String userName, description ,category ,currency;
    private Date date;

    /**
     * CostItem Constructor which receives data and cost id as params
     * @param userName the users name which the costItem belongs to
     * @param costId   the costItem id which was given by the database
     * @param costSum  the total sum of the costItem
     * @param description the description of the costItem
     * @param category the category of the costItem
     * @param currency  the currency of the costItem
     * @param date the date when the costItem is created
     */
    public CostItem(String userName, int costId, double costSum, String description, String category, String currency, Date date){
        setUserName(userName);
        setCostSum(costSum);
        setDescription(description);
        setCategory(category);
        setCurrency(currency);
        setCostId(costId);
        setDate(date);
    }

    /**
     * CostItem Constructor which doesn't receive date and costId as params
     * @param userName the users name which the costItem belongs to
     * @param costSum the total sum of the costItem
     * @param description the description of the costItem
     * @param category the category of the costItem
     * @param currency the currency of the costItem
     */
    public CostItem(String userName, double costSum, String description, String category, String currency) {
        setUserName(userName);
        setCostSum(costSum);
        setDescription(description);
        setCategory(category);
        setCurrency(currency);
        setDate(Date.valueOf(LocalDate.now()));
    }

    /**
     * Overriding toString method
     * @return costItem represented in string
     */
    @Override
    public String toString()
    {
        return "CostItem{" +
                "userName=" + userName +
                ", costSum=" + costSum +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", currency='" + currency + '\'' +
                ", date=" + date +
                '}';
    }

    /**
     * costId getter
     * @return the costItem costId
     */
    public int getCostId()
    {
        return costId;
    }

    /**
     * costId setter
     * @param costId the unique costId of the costItem
     */
    public void setCostId(int costId)
    {
        this.costId = costId;
    }

    /**
     * costSum getter
     * @return the costSum of costItem
     */
    public double getCostSum()
    {
        return costSum;
    }

    /**
     * costSum setter
     * @param costSum the cost sum of the costItem
     */
    public void setCostSum(double costSum)
    {
        this.costSum = costSum;
    }

    /**
     * costItem description getter
     * @return the costItem description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * costItem description setter
     * @param description the description of the costItem
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * costItem category getter
     * @return the costItem category
     */
    public String getCategory()
    {
        return category;
    }

    /**
     * costItem category setter
     * @param category the category of the costItem
     */
    public void setCategory(String category)
    {
        this.category = category;
    }

    /**
     * costItem currency getter
     * @return the costItem currency
     */
    public String getCurrency()
    {
        return currency;
    }

    /**
     * costItem currency setter
     * @param currency the costItem currency
     */
    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    /**
     * costItem username getter
     * @return the username of the costItem
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * costItem username setter
     * @param userName the username of the costItem
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    /**
     * costItem date getter
     * @return the date in which the costItem was created
     */
    public Date getDate()
    {
        return this.date;
    }

    /**
     * costItem date setter
     * @param date the date in which the costItem was created
     */
    public void setDate(Date date)
    {
        this.date = date;
    }

}
