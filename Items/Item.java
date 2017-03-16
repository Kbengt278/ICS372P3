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
    protected String type;
    protected boolean available;    // Available in the library -- false = checked out
    protected Calendar dateDue;     // Due date
    protected int checkOutTimeDays; // Number of days that item can be checked out
    protected int checkedOutBy;     // Library card number of member that has it checcked out


    public Item() {
    }

    public Item(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.available = true;
        this.dateDue = Calendar.getInstance();
        this.checkedOutBy = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Calendar getDateDue() {
        return dateDue;
    }

    public void setDateDue(Calendar dateDue) {
        this.dateDue = dateDue;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getCheckOutTimeDays() {
        return checkOutTimeDays;
    }

    public void setCheckOutTimeDays(int checkOutTimeDays) {
        this.checkOutTimeDays = checkOutTimeDays;
    }

    public int getCheckedOutBy() {
        return checkedOutBy;
    }

    public void setCheckedOutBy(int checkedOutBy) {
        this.checkedOutBy = checkedOutBy;
    }

}
