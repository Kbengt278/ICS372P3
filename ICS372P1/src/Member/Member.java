package Member;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Member Class :
 * Contains a member number (library card number) and a list of checked out items for teh member
 */
public class Member implements Serializable{

    private String name;
    private int libraryCardNum;
    private ArrayList<String> checkedOutItems = new ArrayList<String>();

    public Member(){};

    public Member(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLibraryCardNum() {
        return libraryCardNum;
    }

    public void setLibraryCardNum(int libraryCardNum) {
        this.libraryCardNum = libraryCardNum;
    }
    //
    // Adds an item to the members checked out list
    //
    public void addItem(String itemId) {
        checkedOutItems.add(itemId);
    }

    //
    // Removes an item from the members checked out list
    //
    public void removeItem(String itemId){
        checkedOutItems.remove(itemId);
    }

    //
    // Returns the list of checked out items
    //
    public ArrayList<String> getCheckedOutItems() {
        return checkedOutItems;
    }



}
