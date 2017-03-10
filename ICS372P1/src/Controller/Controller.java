package Controller;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.*;

import Storage.Storage;
import javafx.scene.control.TextArea;

import Items.Item;
import Library.Library;
import MemberList.*;
import Member.Member;
import Member.MemberIdServer;
import javafx.stage.FileChooser;

/**
 * Controller Class :
 * Creates Library objects and MemberList object. Handles checkIn, checkOut,
 * displayLibraryItems, addFileData, and displayCheckedOutItems functionality
 * between the UI and the appropriate objects
 */

public class Controller implements Serializable {
    private transient final FileChooser fileChooser = new FileChooser();
    private Library main = new Library();
    private Library sister = new Library();
    private MemberList memberList = new MemberList();
    transient SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    public Controller(){
        Storage.load(this);
    }


    // All following commented out was attempt to save objects separately with calls to load and save
    // inside Controller instead of call to Storage


 /*   private void load(Library m, Library s, MemberList ml){
        try {
            FileInputStream file = new FileInputStream("mainData");
            ObjectInputStream input = new ObjectInputStream(file);
            m = (Library) input.readObject();
            input.close();
            file.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        }
        try {
            FileInputStream file = new FileInputStream("sisterData");
            ObjectInputStream input = new ObjectInputStream(file);
            s = (Library) input.readObject();
            input.close();
            file.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        }
        try {
            FileInputStream file = new FileInputStream("memberData");
            ObjectInputStream input = new ObjectInputStream(file);
            ml = (MemberList) input.readObject();
            input.close();
            file.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        }
    }
    private void save(Library m, Library s, MemberList ml) {

        try {
            FileOutputStream file = new FileOutputStream("mainData");
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(m);
            output.close();
            file.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e){}
        try {
            FileOutputStream file = new FileOutputStream("sisterData");
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(s);
            output.close();
            file.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e){}
        try {
            FileOutputStream file = new FileOutputStream("memberData");
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(ml);
            output.close();
            file.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e){}
    }*/

    //
    // checkIn method - calls checkIn on the appropriate library and removes item from
    //      members checkedOut list
    // Inputs : int cardNumber : members id number
    //          String itemId : id of item to check out
    //          int library : library to check item into
    //          TextArea text : text area to write results to
    //
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


    //
    // checkOut method - calls checkout on the appropriate library and adds item to
    //      members checkedOut list
    // Inputs : int cardNumber : members id number
    //          String itemId : id of item to check out
    //          int library : library to check item out of
    //          TextArea text : text area to write results to
    //
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

    //
    // addFileData method - adds items from input file to appropriate library
    // Inputs : File file : file to read data from
    //          int library : true = main, false = sister
    //
    public void addFileData(File file,int library) {
        Library lib = getLib(library);
        if (file.getAbsolutePath().toLowerCase().endsWith("json")) {
            lib.addFileDataJson(file);
        } else {
            lib.addFileDataXml(file);
        }
        Storage.save(this);
    }

    public void addMember(String name, TextArea text){

        Member member = new Member(name);
        member.setLibraryCardNum(memberList.addMember(member));
        text.appendText("New Member: " + member.getName().trim()
                + " created successfully.\nLibrary card number is: "
                + member.getLibraryCardNum() + ".\n");
        Storage.save(this, MemberIdServer.instance());

    }
    //
    // displayLibraryItems method - displays items in library catalog by type
    // Inputs : int library : true = main, false = sister
    //          TextArea text : text area to write output to
    //          int mask : mask of types to display 1 = book, 2 = cd, 4 = dvd, 8 = magazine
    //
    public void displayLibraryItems(int library, TextArea text, int mask){
        text.clear();
        Library lib = getLib(library);
        if ((mask & 1) == 1){
            lib.displayItems("Book", text);
        }
        if ((mask & 2) == 2){
            lib.displayItems("CD", text);
        }
        if ((mask & 4) == 4){
            lib.displayItems("DVD", text);
        }
        if ((mask & 8) == 8){
            lib.displayItems("Magazine", text);
        }
    }

    //
    // displayCheckedOutItems method - Gets the checked out items for this member
    // Inputs : int libraryCardNum : memebers id number
    //          TextArea text : Text area to write output to
    //
    public void displayCheckedOutItems(int libraryCardNum, TextArea text){
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

    //
    // getLib method - returns the library object designated by library
    // Input : int library - library number
    // Returns : library object
    //
    private Library getLib(int library){
        switch (library) {
            case 1 : return main;
            case 2 : return sister;
            default : return null;
        }
    }

    //
    // checkLibraryCardNumber - checks that the Library card number is valid. If not
    //      writes error message to text area
    // Inputs : int cardNumber - member's library card number
    //          TextArea text - text area to write error message
    // returns : true if valid
    //
    private boolean checkLibraryCardNumber(int cardNumber, TextArea text){
        if (cardNumber <= memberList.getNumberMembers()) {
            return true;
        } else {
            text.appendText("Library Card Number " + cardNumber + " is Invalid\n");
            return false;
        }
    }



}
