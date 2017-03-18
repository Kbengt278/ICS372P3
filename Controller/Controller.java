package Controller;

import Items.*;
import Library.Library;
import Member.Member;
import Member.MemberIdServer;
import MemberList.MemberList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import storage.Storage;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

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

            if (lib.getItem(itemId) == null) {
                ret += ("Item " + itemId + " does not exist\n");
                return ret;
            }
            Item item = (lib.checkOut(itemId.trim()));
            if (item == null) {
                ret += ("Item " + itemId + " is already checked out.\n");
            } else {
                memberList.getMember(cardNumber).addItem(itemId);
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

        Item item = (lib.checkIn(itemId));
        if (item == null) {
            ret += ("Item " + itemId + " does not exist\n");
        } else {
            try {
                memberList.getMemberWithItem(itemId).removeItem(itemId);
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
            addFileDataJson(file, lib);
        } else {
            addFileDataXml(file, lib);
        }
        Storage.save(this);
    }

    /**
     * Reads a JSON file and adds items to current library.
     * Detects bad file entries and reports them.
     *
     * @param file File to read data from.
     * @return boolean False if file can't be read.
     */
    public boolean addFileDataJson(File file, Library lib) {
        String id = new String();
        String type = new String();
        String name = new String();
        String authorArtist = new String();
        String textLine = "";
        String keyName = "";
        String value;
        int index = 0;
        boolean startArray = false;

        Scanner input = null;
        try {
            input = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find file");
            return false;
        }
        try {
            while (input.hasNext()) {
                textLine = textLine + input.nextLine() + "\n";
            }
        } catch (NullPointerException e) {
            System.out.println("Couldn't find file");
        }
        input.close();

        JsonParser parser = Json.createParser(new StringReader(textLine));
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            switch (event) {
                case START_ARRAY:
                    startArray = true;
                    break;
                case END_ARRAY:
                    startArray = false;
                    break;
                case END_OBJECT:
                    if (startArray) {
                        if (id != null && name != null && type != null && !id.equals("") && !name.equals("") && !type.equals("")) {
                            switch (type.toLowerCase()) {
                                case "cd":
                                    lib.addItem(new Cd(id, name, type, authorArtist));
                                    break;
                                case "book":
                                    lib.addItem(new Book(id, name, type, authorArtist));
                                    break;
                                case "magazine":
                                    lib.addItem(new Magazine(id, name, type));
                                    break;
                                case "dvd":
                                    lib.addItem(new Dvd(id, name, type));
                                    break;
                            }
                        } else {
                            System.out.println("Invalid entry data: ID = " + id + " , " +
                                    "Type = " + type + "' " + "Name = " + name + "\n");
                        }
                    }
                    break;
                case START_OBJECT:
                    id = null;
                    name = null;
                    type = null;
                case VALUE_FALSE:
                case VALUE_NULL:
                case VALUE_TRUE:
                    break;
                case KEY_NAME:
                    keyName = parser.getString();
                    break;
                case VALUE_STRING:
                case VALUE_NUMBER:
                    value = parser.getString();
                    switch (keyName.toLowerCase()) {
                        case "item_name":
                            name = value;
                            break;
                        case "item_type":
                            type = value;
                            break;
                        case "item_id":
                            id = value;
                            break;
                        case "item_artist":
                        case "item_author":
                            authorArtist = value;
                            break;
                    }
                    break;
            }
        }
        return true;
    }

    /**
     * Reads a XML file and adds items to current library
     * Detects bad file entries and reports them
     *
     * @param file File to read data from
     * @return boolean False if file can't be read
     */
    public boolean addFileDataXml(File file, Library lib) {
        String id = new String();
        String type = new String();
        String name = new String();
        String author = new String();
        String artist = new String();
        String volume = new String();
        Document doc = null;

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Item");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    id = null;
                    type = null;
                    name = null;
                    author = null;
                    artist = null;
                    volume = null;
                    try {
                        id = eElement.getAttribute("id");
                    } catch (NullPointerException e) {
                    }
                    try {
                        type = eElement.getAttribute("type");
                    } catch (NullPointerException e) {
                    }
                    try {
                        name = eElement.getElementsByTagName("Name").item(0).getTextContent();
                    } catch (NullPointerException e) {
                    }
                    try {
                        if (type.toLowerCase().equals("book"))
                            author = eElement.getElementsByTagName("Author").item(0).getTextContent();
                    } catch (NullPointerException e) {
                        author = "";
                    }
                    try {
                        if (type.toLowerCase().equals("cd"))
                            artist = eElement.getElementsByTagName("Artist").item(0).getTextContent();
                    } catch (NullPointerException e) {
                        artist = "";
                    }
                    try {
                        if (type.toLowerCase().equals("magazine"))
                            volume = eElement.getElementsByTagName("Volume").item(0).getTextContent();
                    } catch (NullPointerException e) {
                        volume = "";
                    }
                    if (id != null && name != null && type != null && !id.equals("") && !name.equals("") && !type.equals("")) {
                        switch (type.toLowerCase()) {
                            case "cd":
                                lib.addItem(new Cd(id, name, "CD", artist));
                                break;
                            case "book":
                                lib.addItem(new Book(id, name, "Book", author));
                                break;
                            case "magazine":
                                lib.addItem(new Magazine(id, name, "Magazine", volume));
                                break;
                            case "dvd":
                                lib.addItem(new Dvd(id, "DVD", type));
                                break;
                        }
                    } else {
                        System.out.println("Invalid entry data: ID = " + id + " , " +
                                "Type = " + type + "' " + "Name = " + name + "\n");
                    }

                }
            }
        } catch (ParserConfigurationException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (SAXException e) {
            return false;
        }
        return true;
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