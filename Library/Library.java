package Library;


import Items.*;
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
import java.util.HashMap;
import java.util.Scanner;

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
     * Reads a JSON file and adds items to current library.
     * Detects bad file entries and reports them.
     *
     * @param file File to read data from.
     * @return boolean False if file can't be read.
     */
    public boolean addFileDataJson(File file) {
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
    public boolean addFileDataXml(File file) {
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
                    if (id != null && name != null && type != null) {
                        switch (type.toLowerCase()) {
                            case "cd":
                                list.put(id, new Cd(id, name, "CD", artist));
                                break;
                            case "book":
                                list.put(id, new Book(id, name, "Book", author));
                                break;
                            case "magazine":
                                list.put(id, new Magazine(id, name, "Magazine", volume));
                                break;
                            case "dvd":
                                list.put(id, new Dvd(id, "DVD", type));
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

    public void addItem(Item addThisItem) {
        list.put(addThisItem.getId(), new Item(addThisItem.getId(), addThisItem.getName(), addThisItem.getType()));
    }
}
