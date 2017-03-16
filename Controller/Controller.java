package Controller;

import Items.Item;
import Library.Library;
import Member.Member;
import Member.MemberIdServer;
import MemberList.MemberList;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import storage.Storage;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Controller Class :
 * Creates Library objects and MemberList object. Handles checkIn, checkOut,
 * displayLibraryItems, addFileData, and displayCheckedOutItems functionality
 * between the UI and the appropriate objects
 */

public class Controller implements Serializable {
    private transient final FileChooser fileChooser = new FileChooser();
    transient SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private Library main = new Library();
    private Library sister = new Library();
    private MemberList memberList = new MemberList();

    public Controller() {
    }

    /**
     * Calls checkIn on the appropriate library and removes
     * item from member's checkedOut list
     *
     * @param cardNumber Member's id number
     * @param itemId     ID of item to check out
     * @param library    Library to check item into
     * @param text       Text area to write results to
     */
    public void checkIn(int cardNumber, String itemId, int library, TextArea text) {
        if (checkLibraryCardNumber(cardNumber, text)) {
            Library lib = getLib(library);

            Item item = (lib.checkIn(cardNumber, itemId.trim()));
            if (item == null) {
                text.appendText("Item " + itemId.trim() + " does not exist\n");
            } else {

                memberList.getMember(cardNumber).removeItem(itemId);
                text.appendText("Item " + itemId.trim() + " "
                        + item.getType() + " : "
                        + item.getName() + "\n" +
                        "checked in successfully\n");
            }
        }
        Storage.save(this);
    }

    /**
     * Calls checkout on the appropriate library and adds item to
     * member's checkedOut list
     *
     * @param cardNumber Member's id number
     * @param itemId     ID of item to check out
     * @param library    Library to check item out of
     * @param text       Text area to write results to
     */
    public void checkOut(int cardNumber, String itemId, int library, TextArea text) {
        if (checkLibraryCardNumber(cardNumber, text)) {
            Library lib = getLib(library);

            Item item = (lib.checkOut(cardNumber, itemId.trim()));
            if (item == null) {
                text.appendText("Item " + itemId + " is not available\n");
            } else {
                memberList.getMember(cardNumber).addItem(itemId);
                text.appendText("Item " + itemId + " "
                        + item.getType() + " : "
                        + item.getName() + "\n"
                        + "checked out successfully. Due date is "
                        + (item.getDateDue().get(Calendar.MONTH) + 1)
                        + "/" + item.getDateDue().get(Calendar.DAY_OF_MONTH)
                        + "/" + item.getDateDue().get(Calendar.YEAR) + "\n");
            }
        }
        Storage.save(this);
    }

    /**
     * Adds items from input file to appropriate library
     *
     * @param file    File to read data from
     * @param library true = main, false = sister
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
     * @param text Text area to write output to
     */
    public void addMember(String name, TextArea text) {

        Member member = new Member(name);
        member.setLibraryCardNum(memberList.addMember(member));
        text.appendText("New Member: " + member.getName().trim()
                + " created successfully.\nLibrary card number is: "
                + member.getLibraryCardNum() + ".\n");
        Storage.save(this, MemberIdServer.instance());
    }

    /**
     * Displays items in library catalog by type
     *
     * @param library true = main, false = sister
     * @param text    Text area to write output to
     * @param mask    Mask of types to display 1 = book, 2 = cd, 4 = dvd, 8 = magazine
     */
    public void displayLibraryItems(int library, TextArea text, int mask) {
        text.clear();
        Library lib = getLib(library);
        if ((mask & 1) == 1) {
            lib.displayItems("Book", text);
        }
        if ((mask & 2) == 2) {
            lib.displayItems("CD", text);
        }
        if ((mask & 4) == 4) {
            lib.displayItems("DVD", text);
        }
        if ((mask & 8) == 8) {
            lib.displayItems("Magazine", text);
        }
    }

    /**
     * Gets the checked out items for this member
     *
     * @param libraryCardNum Member's id number
     * @param text           Text area to write output to
     */
    public void displayCheckedOutItems(int libraryCardNum, TextArea text) {
        if (checkLibraryCardNumber(libraryCardNum, text)) {
            ArrayList<String> items = memberList.getMember(libraryCardNum).getCheckedOutItems();
            text.clear();
            Item item;
            text.appendText("Checked out items of member #: " + libraryCardNum + "\n");
            for (String element : items) {
                item = main.getItem(element);
                if (item != null) {
                    text.appendText("Id = " + item.getId());
                    text.appendText(" " + item.getType() + " ");
                    text.appendText(" Name = " + item.getName());
                    text.appendText(" Due Date = "
                            + (item.getDateDue().get(Calendar.MONTH) + 1)
                            + "/" + item.getDateDue().get(Calendar.DAY_OF_MONTH)
                            + "/" + item.getDateDue().get(Calendar.YEAR) + "\n");
                    continue;
                }
                item = sister.getItem(element);
                if (item != null) {
                    text.appendText("Id = " + item.getId());
                    text.appendText(" " + item.getType() + " ");
                    text.appendText(" Name = " + item.getName());
                    text.appendText(" Due Date = "
                            + (item.getDateDue().get(Calendar.MONTH) + 1)
                            + "/" + item.getDateDue().get(Calendar.DAY_OF_MONTH)
                            + "/" + item.getDateDue().get(Calendar.YEAR) + "\n");
                }
            }
        }
    }

    /**
     * Returns the library object designated by library
     *
     * @param library library number
     * @return Library object
     */
    private Library getLib(int library) {
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
     * @param text       Text area to write error message
     * @return true if valid
     */
    private boolean checkLibraryCardNumber(int cardNumber, TextArea text) {
        if (cardNumber <= memberList.getNumberMembers()) {
            return true;
        } else {
            text.appendText("Library Card Number " + cardNumber + " is Invalid\n");
            return false;
        }
    }
}