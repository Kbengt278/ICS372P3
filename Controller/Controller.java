package Controller;

import Items.*;
import Library.Library;
import Member.Member;
import Member.MemberIdServer;
import MemberList.MemberList;
import Storage.Storage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Creates and maintains the Library objects and MemberList object.
 * Handles checkIn, checkOut, displayLibraryItems,
 * addFileData, and displayMemberCheckedOutItems functionality
 * between the UI and the appropriate objects.
 */

public class Controller implements Serializable {
    private Library main = new Library();
    private Library sister = new Library();
    private MemberList memberList = new MemberList();

    public Controller() {
    }

    /**
     * Checks out the item to the UI set library by given cardNumber.
     *
     * @param cardNumber member's cardNumber who will check out the item
     * @param itemId     id of the item to check out
     * @param library    library type of where the item is
     * @return text to display to user
     */
    public String checkOut(int cardNumber, String itemId, Library.Type library) {
        String message = "";
        Library lib = getLib(library);
        Boolean isCheckedIn = lib.checkOut(itemId);

        Member member = this.memberList.getMember(cardNumber);
        if (member != null) {
            if (isCheckedIn == null)
                message += "Item " + itemId + " does not exist\n";
            else if (!isCheckedIn)
                message += "Item " + itemId + " is currently checked out.\n";
            else {
                member.addItem(lib.getItem(itemId));
                message += "Checkout successful: \n";
                message += lib.getItem(itemId).toString();
            }
        } else
            message += "Library card number " + cardNumber + " is invalid\n";

        Storage.save(this);
        return message;
    }

    /**
     * Checks in the item to the UI set library.
     *
     * @param itemId  id of the item to check in
     * @param library library type of where the item is
     * @return text to display to user
     */
    public String checkIn(String itemId, Library.Type library) {
        String message = "";
        Library lib = getLib(library);
        Boolean isCheckedOut = lib.checkIn(itemId);

        if (isCheckedOut == null)
            message += "Item " + itemId + " does not exist\n";
        else if (!isCheckedOut)
            message += "Item " + itemId + " is not checked out.\n";
        else {
            try {
                memberList.getMemberWithItem(lib.getItem(itemId)).removeItem(lib.getItem(itemId));
                message += "Checkin successful:\n";
                message += lib.getItem(itemId).toString();
            } catch (NullPointerException e) {
                message += "Error: Item " + itemId + " is marked as checked out but no member has it checked out.\n";
            }
        }
        Storage.save(this);
        return message;
    }

    /**
     * Adds items from input file to appropriate library.
     *
     * @param file    file to read data from
     * @param library library type of where the item is
     */
    public void addFileData(File file, Library.Type library) {
        Library lib = getLib(library);
        if (file.getAbsolutePath().toLowerCase().endsWith("json"))
            addFileDataJson(file, lib);
        else if (file.getAbsolutePath().toLowerCase().endsWith("xml"))
            addFileDataXml(file, lib);
        else {
            // invalid file type -- should be displayed to the screen.
        }
        Storage.save(this);
    }

    /**
     * Reads a JSON file and adds items to UI set library.
     * Detects bad file entries and reports them.
     *
     * @param file file to read data from.
     * @param lib  library where the item is
     * @return false if file can't be read
     */
    boolean addFileDataJson(File file, Library lib) {
        String id = "";
        String type = "";
        String name = "";
        String optionalField = "";
        String textLine = "";
        String keyName = "";
        String value;
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
                                    lib.addItem(new Cd(id, name, Item.Type.CD, optionalField));
                                    break;
                                case "book":
                                    lib.addItem(new Book(id, name, Item.Type.BOOK, optionalField));
                                    break;
                                case "magazine":
                                    lib.addItem(new Magazine(id, name, Item.Type.MAGAZINE, optionalField));
                                    break;
                                case "dvd":
                                    lib.addItem(new Dvd(id, name, Item.Type.DVD));
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
                    optionalField = null;
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
                        case "item_volume":
                            optionalField = value;
                            break;
                    }
                    break;
            }
        }
        return true;
    }

    /**
     * Reads a XML file and adds items to UI set library.
     * Detects bad file entries and reports them.
     *
     * @param file file to read data from
     * @param lib  library where the item is
     * @return false if file can't be read
     */
    boolean addFileDataXml(File file, Library lib) {
        String id = null;
        String type = null;
        String name = null;
        String author = null;
        String artist = null;
        String volume = null;
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
                                lib.addItem(new Cd(id, name, Item.Type.CD, artist));
                                break;
                            case "book":
                                lib.addItem(new Book(id, name, Item.Type.BOOK, author));
                                break;
                            case "magazine":
                                lib.addItem(new Magazine(id, name, Item.Type.MAGAZINE, volume));
                                break;
                            case "dvd":
                                lib.addItem(new Dvd(id, name, Item.Type.DVD));
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
     * Adds a member to memberList with a generated library card number.
     *
     * @param name name of new member
     * @return text to display to user
     */
    public String addMember(String name) {
        String message = "";

        Member member = this.memberList.createMember(name);
        message += ("New Member: " + member.getName().trim() + " created successfully.\n" +
                "Library card number is: " + member.getLibraryCardNumber() + ".\n");

        Storage.save(this, MemberIdServer.instance());
        return message;
    }

    /**
     * Displays items in library catalog by type.
     *
     * @param mask    mask of types to display: 1 = book, 2 = cd, 4 = dvd, 8 = magazine
     * @param library library where the item is
     * @return text to display to user
     */
    public String displayLibraryItems(int mask, Library.Type library) {
        String message = "";
        Library lib = getLib(library);
        if ((mask & 1) == 1) {
            message += lib.displayItemsOfType(Item.Type.BOOK);
        }
        if ((mask & 2) == 2) {
            message += lib.displayItemsOfType(Item.Type.CD);
        }
        if ((mask & 4) == 4) {
            message += lib.displayItemsOfType(Item.Type.DVD);
        }
        if ((mask & 8) == 8) {
            message += lib.displayItemsOfType(Item.Type.MAGAZINE);
        }
        if (message.equals("")) {
            message += "No items in this library.\n";
        }
        return message;
    }

    /**
     * Gets the checked out items for this member.
     *
     * @param cardNumber member's cardNumber whose items will be displayed
     * @return text to display to user
     */
    public String displayMemberCheckedOutItems(int cardNumber) {
        String message = "";

        Member member = this.memberList.getMember(cardNumber);
        if (member != null) {
            ArrayList<Item> items = member.getCheckedOutItems();

            message += ("Items checked out by " + member.getName() + " - Member #: " + cardNumber + "\n");
            message += "------------------------------------------------------------------------------------------------------------\n";
            for (Item item : items) {
                message += item.toString();
            }
        } else
            message += ("Library card number " + cardNumber + " is invalid\n");
        return message;
    }

    /**
     * Returns the library object designated by the library type.
     *
     * @param library library type
     * @return the library object
     */
    Library getLib(Library.Type library) {
        switch (library) {
            case MAIN:
                return main;
            case SISTER:
                return sister;
            default:
                return null;
        }
    }

    // for testing
    void addItemToLibrary(Item addThisItem, Library.Type library) {
        getLib(library).addItem(addThisItem);
    }
}
