package Library;


import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.*;

import Items.*;
import javafx.scene.control.TextArea;

/**
 * Library Class :
 * Creates a Library object. Allows items to be added to it via JSON or XML files. Allows items to
 * be checked out and checked in. Allows all items in the library catalog to be displayed by type
 */
public class Library {

    //    private ArrayList<Item> list = new ArrayList<>(100);
    private HashMap<String, Item> list = new HashMap();

    public Library() {
    }

    //
    // checkIn method -- sets items available flag true, clears items checkeOutBy field
    // Inputs : int cardNumber - Library card number of member
    //          String itemId - item ID to be checked in
    // Output : Item - object to be checked in. null if item doesn't exist
    //
    public Item checkIn(int cardNumber, String itemId) {
        Item item = list.get(itemId);
        if (item != null) {
            item.setAvailable(true);
            item.setCheckedOutBy(0);
        }
        return item;
    }

    //
    // checkOut method -- sets items available flag false, sets checkedOutBy to cardNumber,
    //                  sets items DateDue to approprate due date
    // Inputs : int cardNumber - Library card number of member
    //          String itemId - item ID to be checked out
    // Output : Item - object to be checked out. null if item doesn't exist or is unavailable
    //
    public Item checkOut(int cardNumber, String itemId) {
        Item item = list.get(itemId);
        if (item == null || !item.isAvailable())
            return null;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, item.getCheckOutTime());
        item.setAvailable(false);
        item.setDateDue(cal);
        item.setCheckedOutBy(cardNumber);
        return item;

    }

    //
    // addFileData method - reads a JSON file and adds items to current library
    //            detects bad file entries and reports them
    // Input : File file - file to read data from
    // Output : boolean - false if file can't be read
    //
    public boolean addFileData(File file){
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
        }
        catch (NullPointerException e) {
            System.out.println("Couldn't find file");
        }
        input.close();

        JsonParser parser = Json.createParser(new StringReader(textLine));
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            switch(event) {
                case START_ARRAY:
                    startArray = true;
                    break;
                case END_ARRAY:
                    startArray = false;
                    break;
                case END_OBJECT:
                    if (startArray) {
                        if (id != null && name != null && type != null) {
                            switch (type.toLowerCase()) {
                                case "cd":
                                    list.put(id, new Cd(id, name, type, authorArtist));
                                    break;
                                case "book":
                                    list.put(id, new Book(id, name, type, authorArtist));
                                    break;
                                case "magazine":
                                    list.put(id, new Magazine(id, name, type));
                                    break;
                                case "dvd":
                                    list.put(id, new Dvd(id, name, type));
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
                    switch(keyName.toLowerCase()) {
                        case "item_name" :
                            name = value;
                            break;
                        case "item_type" :
                            type = value;
                            break;
                        case "item_id" :
                            id = value;
                            break;
                        case "item_artist" :
                        case "item_author" :
                            authorArtist = value;
                            break;
                    }
                    break;
            }
        }
        return true;
    }

    //
    // displayItems - displays items in the Library catalog
    // Inputs : String type : the type of items to display (e.g book, cd,dvd)
    //          TextArea text - text area to write output to
    //
    public void displayItems(String type, TextArea text){
        Set set = list.entrySet();
        Iterator i = set.iterator();
        // Display elements
        while(i.hasNext()) {
            Map.Entry temp = (Map.Entry)i.next();
            Item item = (Item)temp.getValue();
            if (type.compareToIgnoreCase(item.getType()) == 0) {
                text.appendText("Id = " + item.getId());
                text.appendText(" " + item.getType() + " ");
                text.appendText(" Name = " + item.getName());
                if (item.isAvailable())
                    text.appendText(" - Available\n");
                else
                    text.appendText(" - Checked out\n");
            }
        }

    }

    //
    // getItem method - returns the Item object pointed to by id
    // Input : String id - item id of object
    // Output : Item object
    //
    public Item getItem(String id){
        return list.get(id);
    }
}
