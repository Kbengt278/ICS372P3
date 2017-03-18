package Controller;

import Items.Item;
import Library.Library;
import Member.Member;
import Member.MemberIdServer;
import MemberList.MemberList;
import storage.Storage;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Controller Class :
 * Creates Library objects and MemberList object. Handles checkIn, checkOut,
 * displayLibraryItems, addFileData, and displayMemberCheckedOutItems functionality
 * between the UI and the appropriate objects
 */

public class Controller implements Serializable {
    private Library main = new Library();
    private Library sister = new Library();
    private MemberList memberList = new MemberList();

    public Controller() {
    }

    /**
     * Adds item to member's checkedOut list
     * Sets item's available flag false
     * Sets item's DateDue to appropriate due date
     * Sets checkedOutBy to cardNumber
     *
     * @param cardNumber Member's id number
     * @param itemId     ID of item to check out
     * @param library    Library to check item out of
     * @return String    display text
     */
    public String checkOut(int cardNumber, String itemId, int library) {
        String ret = "";
        if (checkLibraryCardNumber(cardNumber)) {
            Library lib = getLib(library);

            Item item = (lib.getItem(itemId));
            if (item == null) {
                ret += ("Item " + itemId + " does not exist\n");
            } else if (!item.isAvailable()) {
                ret += ("Item " + itemId + " is already checked out.\n");
            } else {
                memberList.getMember(cardNumber).addItem(itemId);
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, item.getCheckOutTimeDays());
                item.setAvailable(false);
                item.setDateDue(cal);

                ret += ("Item " + itemId + " "
                        + item.getType() + " : "
                        + item.getName() + "\n"
                        + "checked out successfully. Due date is "
                        + (item.getDateDue().get(Calendar.MONTH) + 1)
                        + "/" + item.getDateDue().get(Calendar.DAY_OF_MONTH)
                        + "/" + item.getDateDue().get(Calendar.YEAR) + ".\n");
            }
        } else {
            ret += ("Library card number " + cardNumber + " is invalid\n");
        }
        Storage.save(this);
        return ret;
    }

    /**
     * Removes item from member's checkedOut list
     * Sets item's available flag true
     * Clears items checkedOutBy field
     *
     * @param itemId  ID of item to check out
     * @param library Library to check item into
     * @return String    display text
     */
    public String checkIn(String itemId, int library) {
        String ret = "";
        Library lib = getLib(library);

        Item item = (lib.getItem(itemId));
        if (item == null) {
            ret += ("Item " + itemId + " does not exist\n");
        } else if (item.isAvailable()) {
            ret += ("Item " + itemId + " is not checked out.\n");
        } else {
            try {
                memberList.getMemberWithItem(itemId).removeItem(itemId);
                System.out.println(item.isAvailable());
                item.setAvailable(true);

                ret += ("Item " + itemId + " "
                        + item.getType() + " : "
                        + item.getName() + "\n" +
                        "checked in successfully\n");
            } catch (NullPointerException e) {
                ret += ("Error: Item " + itemId + " is marked as checked out but no member has it checked out.\n");
            }
        }
        Storage.save(this);
        return ret;
    }

    /**
     * Adds items from input file to appropriate library
     *
     * @param file    File to read data from
     * @param library 1 = main, 2 = sister
     */
    public void addFileData(File file, int library) {
        Library lib = getLib(library);
        if (file.getAbsolutePath().toLowerCase().endsWith("json")) {
            lib.addFileDataJson(file);
        } else {
            lib.addFileDataXml(file);
        }
        Storage.save(this);
    }

    /**
     * Adds a member to memberList with a library card number
     *
     * @param name Name of new member
     * @return String display text
     */
    public String addMember(String name) {
        String ret = "";
        Member member = new Member(name);
        member.setLibraryCardNum(memberList.addMember(member));
        ret += ("New Member: " + member.getName().trim() + " created successfully.\n" +
                "Library card number is: " + member.getLibraryCardNum() + ".\n");
        Storage.save(this, MemberIdServer.instance());
        return ret;
    }

    /**
     * Displays items in library catalog by type
     *
     * @param library 1 = main, 2 = sister
     * @param mask    Mask of types to display 1 = book, 2 = cd, 4 = dvd, 8 = magazine
     * @return String display text
     */
    public String displayLibraryItems(int library, int mask) {
        String ret = "";
        Library lib = getLib(library);
        if ((mask & 1) == 1) {
            ret += lib.displayItems("Book");
        }
        if ((mask & 2) == 2) {
            ret += lib.displayItems("CD");
        }
        if ((mask & 4) == 4) {
            ret += lib.displayItems("DVD");
        }
        if ((mask & 8) == 8) {
            ret += lib.displayItems("Magazine");
        }
        if (ret.equals("")) {
            ret += "No items in this library.\n";
        }
        return ret;
    }

    /**
     * Gets the checked out items for this member
     *
     * @param cardNumber Member's library card number
     * @return String display text
     */
    public String displayMemberCheckedOutItems(int cardNumber) {
        String ret = "";
        if (checkLibraryCardNumber(cardNumber)) {
            ArrayList<String> items = memberList.getMember(cardNumber).getCheckedOutItems();

            Item item;
            ret += ("Checked out items of member #: " + cardNumber + "\n");
            for (String element : items) {
                item = main.getItem(element);
                if (item != null) {
                    ret += ("Id = " + item.getId());
                    ret += (" " + item.getType() + " ");
                    ret += (" Name = " + item.getName());
                    ret += (" Due Date = "
                            + (item.getDateDue().get(Calendar.MONTH) + 1)
                            + "/" + item.getDateDue().get(Calendar.DAY_OF_MONTH)
                            + "/" + item.getDateDue().get(Calendar.YEAR) + "\n");
                    continue;
                }
                item = sister.getItem(element);
                if (item != null) {
                    ret += ("Id = " + item.getId());
                    ret += (" " + item.getType() + " ");
                    ret += (" Name = " + item.getName());
                    ret += (" Due Date = "
                            + (item.getDateDue().get(Calendar.MONTH) + 1)
                            + "/" + item.getDateDue().get(Calendar.DAY_OF_MONTH)
                            + "/" + item.getDateDue().get(Calendar.YEAR) + "\n");
                }
            }
        } else {
            ret += ("Library card number " + cardNumber + " is invalid\n");
        }

        return ret;
    }

    /**
     * Returns the library object designated by library
     *
     * @param library library number
     * @return Library object
     */
    Library getLib(int library) {
        switch (library) {
            case 1:
                return main;
            case 2:
                return sister;
            default:
                return null;
        }
    }

    /**
     * Checks that the Library card number is valid.
     * If not writes error message to text area
     *
     * @param cardNumber Member's library card number
     * @return true if valid
     */
    boolean checkLibraryCardNumber(int cardNumber) {
        if (cardNumber <= memberList.getNumberMembers()) {
            return true;
        } else {
            return false;
        }
    }

    public void addItemToLibrary(Item addThisItem, int library) {
        getLib(library).addItem(addThisItem);
    }
}