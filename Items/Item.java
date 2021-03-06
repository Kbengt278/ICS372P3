package Items;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Item class is to be extended by subclasses of each type of Item.
 * It maintains attributes common to all types of Items.
 */
public class Item implements Serializable {

    int checkOutTimeDays; // Number of days that item can be checked out
    private String id;
    private String name;
    private Item.Type type;
    private boolean available;    // Available in the library - false = checked out
    private Calendar dateDue;     // Due date
    private Status status;

    public Item() {
    }

    public Item(String id, String name, Type type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.available = true;
        this.dateDue = Calendar.getInstance();
        this.status = Status.CHECKED_IN;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return Makes a string of the attributes of the item.
     */
    @Override
    public String toString() {
        return "Item ID: " + this.getId() +
                " Title: " + this.getName();
    }

    /**
     * Adds more info to the item. Called by subclasses only.
     *
     * @return Adds the availability and due date if the item has one.
     */
    String toString2() {
        String message = "";
        if (this.isAvailable())
            message += (" -- Available");
        else
            message += (" -- Unavailable");
        return message;
    }

    public enum Type {
        BOOK, CD, DVD, MAGAZINE
    }

    public enum Status {
        CHECKED_IN, CHECKED_OUT, MISSING, OVERDUE, SHELVING, REMOVED_FROM_CIRCULATION, REFERENCE_ONLY, CHECK_STATUS
    }
}
