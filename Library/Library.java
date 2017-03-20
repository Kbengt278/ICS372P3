package Library;


import Items.Item;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Library Class :
 * Creates a Library object. Allows items to be added to it via JSON or XML files. Allows items to
 * be checked out and checked in. Allows all items in the library catalog to be displayed by type
 */
public class Library implements Serializable {

    private HashMap<String, Item> list = new HashMap<>();

    public Library() {
    }

    /**
     * checkOut method -- sets items available flag false, sets checkedOutBy to cardNumber,
     * sets items DateDue to appropriate due date
     *
     * @param itemId - item ID to be checked out
     * @return message to user
     */
    public Boolean checkOut(String itemId) {
        Item item = this.getItem(itemId);
        if (item == null) {
            return null;
        } else if (!item.isAvailable()) {
            return false;
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, item.getCheckOutTimeDays());
            item.setAvailable(false);
            item.setDateDue(cal);
            return true;
        }
    }

    /**
     * checkIn method -- sets items available flag true, clears items checkeOutBy field
     *
     * @param itemId - item ID to be checked in
     * @return null if item is null; false if item is available; otherwise, true
     */
    public Boolean checkIn(String itemId) {
        Item item = list.get(itemId);
        if (item == null) {
            return null;
        } else if (item.isAvailable()) {
            return false;
        } else {
            item.setAvailable(true);
            item.setDateDue(null);
            return true;
        }
    }

    /**
     * Displays items in the Library catalog
     *
     * @param type The type of items to display (e.g book, cd, dvd)
     * @return String display text
     */
    public String displayItems(Item.Type type) {
        String ret = "";
        for (Item value : list.values()) {
            if (type.equals(value.getType())) {
                ret += ("Id = " + value.getId());
                ret += (" " + value.getType().name() + " ");
                ret += (" Name = " + value.getName());
                if (value.isAvailable())
                    ret += (" - Available\n");
                else
                    ret += (" - Checked out\n");
            }
        }
        return ret;
    }

    public String toString(String itemId) {
        Item item = getItem(itemId);
        return item.toString();
    }

    public Item getItem(String id) {
        return list.get(id);
    }

    /**
     * Adds item to the list of items in this library
     *
     * @param item The item object to be added to list
     **/
    public void addItem(Item item) {
        list.put(item.getId(), item);
    }

    public enum Type {
        MAIN, SISTER;
    }
}
