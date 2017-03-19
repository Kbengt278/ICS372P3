package Library;


import Items.*;
import Member.Member;
import java.util.Calendar;
import java.io.*;
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
     * sets items DateDue to approprate due date
     * 
     * @param itemId - item ID to be checked out
     * @param member
     * @return message to user
     */
    public String checkOut(String itemId, Member member)
    {
    	String message = "";
        Item item = list.get(itemId);
        
        if (item == null)
        	message += ("Item " + itemId + " does not exist\n");
        else if (!item.isAvailable())
        	message += ("Item " + itemId + " is already checked out.\n");
        else
        {
            member.addItem(itemId);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, item.getCheckOutTimeDays());
            item.setAvailable(false);
            item.setDateDue(cal);
            
            message += ("Item " + itemId + " "
                    + item.getType() + " : "
                    + item.getName() + "\n"
                    + "checked out successfully. Due date is "
                    + (item.getDateDue().get(Calendar.MONTH) + 1)
                    + "/" + item.getDateDue().get(Calendar.DAY_OF_MONTH)
                    + "/" + item.getDateDue().get(Calendar.YEAR) + ".\n");
        }
        return message;
    }
    
    /**
     * checkIn method -- sets items available flag true, clears items checkeOutBy field
     * @param itemId - item ID to be checked in
     * @return null if item is null; false if item is available; otherwise, true
     */
    public Boolean checkIn(String itemId)
    {
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
    public String displayItems(String type) {
        String ret = "";
        for (Item value : list.values()) {
            if (type.equalsIgnoreCase(value.getType())) {
                ret += ("Id = " + value.getId());
                ret += (" " + value.getType() + " ");
                ret += (" Name = " + value.getName());
                if (value.isAvailable())
                    ret += (" - Available\n");
                else
                    ret += (" - Checked out\n");
            }
        }
        return ret;
    }
    
    public Item getItem(String id) {
        return list.get(id);
    }

    /**
    * Adds item to the list
    *
    * @param item The item object to be added to list
     **/
    public void addItem(Item item) {
        list.put(item.getId(), item);
    }

    //
    // Returns due date of item
    //
    public Calendar getDueDate(String itemId, Library library) {
        Library lib = library;
        Item item = lib.getItem(itemId);
        return item.getDateDue();
    }
    
    /**
     * 
     * @return size of library HashMap
     */
    public int size() {
    	return list.size();
    }
    
    public String toString(String itemId, Library library) {
        Library lib = library;
        Item item = lib.getItem(itemId);
        return ("Item " + itemId + " "
                + item.getType() + " : "
                + item.getName() + "\n");
    }
}
