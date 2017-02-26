package Member;

import java.util.ArrayList;

/**
 * Member Class :
 * Contains a member number (library card number) and a list of checked out items for teh member
 */
public class Member {
    // Static member that contains the next available library card number
    static int nextMemberNum = 1;

    private int libraryCardNum;
    private ArrayList<String> checkedOutItems = new ArrayList<String>();

    public Member() {
        libraryCardNum = nextMemberNum;
        nextMemberNum++;
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

    //
    // Returns the next available library card number
    //
    public static int getNextMemberNum() {
        return nextMemberNum;
    }

}
