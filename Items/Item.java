package Items;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Item class is to be extended by subclasses of each type of Item.
 * It maintains protected attributes common to all types of Items.
 */
public class Item implements Serializable {

    protected String id;
    protected String name;
    protected Type type;
    protected boolean available;    // Available in the library -- false = checked out
    protected Calendar dateDue;     // Due date
    protected int checkOutTimeDays; // Number of days that item can be checked out

    public Item() {
    }

    public Item(String id, String name, Type type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.available = true;
        this.dateDue = Calendar.getInstance();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Calendar getDateDue() {
        return dateDue;
    }

    public void setDateDue(Calendar dateDue) {
        this.dateDue = dateDue;
    }

    public int getCheckOutTimeDays() {
        return checkOutTimeDays;
    }

    public void setCheckOutTimeDays(int checkOutTimeDays) {
        this.checkOutTimeDays = checkOutTimeDays;
    }
  
    @Override
    public String toString()
    {
      String message = "Item ID: " + getId() +
                         " -- Type: " + getType() +
                           " -- Name: " + getName();
      if(dateDue != null)
      {
        message += " -- Due Date: " +
                        (dateDue.get(Calendar.MONTH) + 1) +
                         "/" + dateDue.get(Calendar.DAY_OF_MONTH) +
                         "/" + dateDue.get(Calendar.YEAR) + "\n";
      }

      return message;
    }
  
    public enum Type {
        BOOK, CD, DVD, MAGAZINE;
    }
}
