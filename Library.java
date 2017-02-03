package ics372Prgm1Library;

import ics372Prgm1Items.*;

import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;


/*
 * Library class manages a library of items. Items are placed in an
 * ArrayList. Methods used to manage the list include:
 * checkIn(), checkOut(), addFileData(), search(), and printLib().
 *
 * Created by Kevin on 1/20/2017.
 */

public class Library {

    private ArrayList<Item> list = new ArrayList<>(100);

    public Library() {
    }

    /*
    * checkIn() accepts a Sting argument. The String is supposed to
    * be a valid itemId of an Item object. The class method search()
    * is used to assure the Item is in the Library. If it is, the
    * Item class setAvailable() method is used to make the Item
    * available, and the Item is returned to the UI where the method
    * is called. If it is not, null is returned to UI where method
    * is called.
     */

    public Item checkIn(String id) {
        int index = search(id, 0, list.size() - 1);
        if (index < 0)
            return null;
        else {
            list.get(index).setAvailable(true);
            return list.get(index);
        }
    }

    /*
    * checkOut() accepts a String argument. The String is supposed
    * to be a valid itemId of an Item object. The class method search()
    * is used to assure the Item is in the Library. If it is,
    * isAvailable() method of the Item class is used to determine if
    * the Item is currently checked out. If it is checked out,
    * null is returned to the call from the UI. If it is available,
    * a Calender object is used in addition to Item's getCheckOutTime
    * to setDateDue(), and the Item is returned to the call from the UI.
    * If the Item is not in the Library, null is returned.
     */

    public Item checkOut(String id) {
        int index = search(id, 0, list.size() - 1);
        if (index < 0)
            return null;
        else if (!list.get(index).isAvailable())
            return null;
        else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, list.get(index).getCheckOutTime());
            list.get(index).setAvailable(false);
            list.get(index).setDateDue(cal);
            return list.get(index);
        }
    }

    /*
    * addFileData() accepts a file argument. A Scanner object is
    * used to read the file if it exists. If it does not,
    * FileNotFoundException is thrown. A JsonParser object is used
    * to parse the file and add the file's data to the Library.
     */

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
                        index = search(id, 0, list.size()-1);
                        if (index < 0) {
                            switch (type.toLowerCase()) {
                                case "cd":
                                    list.add(-(index + 1), new Cd(id, name, type, authorArtist));
                                    break;
                                case "book":
                                    list.add(-(index + 1), new Book(id, name, type, authorArtist));
                                    break;
                                case "magazine":
                                    list.add(-(index + 1), new Magazine(id, name, type));
                                    break;
                                case "dvd":
                                    list.add(-(index + 1), new Dvd(id, name, type));
                                    break;
                            }
                        }
                    }
                    break;
                case START_OBJECT:
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

    /*
    * search() accepts a String argument for the itemId being
    * searched for, and integer arguments for lower and upper
    * bounds of the array. A binary search is used to find and
    * return the index of the Item in the array.
     */

    private int search(String id, int lwr, int upr) {
        if (list.size() == 0) {
            return -1;
        }
        if (lwr == upr) {
            //
            // If id's are equal, then we're on the last item
            //
            if (id.compareToIgnoreCase(list.get(lwr).getId()) == 0) {
                return lwr;
            }
            else if (id.compareToIgnoreCase(list.get(lwr).getId()) < 0) {
                return -(lwr + 1);
            }
            else {
                return -(lwr + 2);
            }
        }
        if (id.compareToIgnoreCase(list.get((lwr + upr)/2).getId()) == 0) {
            return (lwr + upr)/2;
        }
        if (id.compareToIgnoreCase(list.get((lwr + upr)/2).getId()) < 0) {
            if ((upr - lwr) == 1) {
                return search(id, lwr, lwr);
            }
            else {
                return search(id, lwr, ((lwr + upr) / 2) + ((lwr + upr) % 2));
            }
        }
        else if ((upr - lwr) == 1) {
            return search(id, upr, upr);
        }
        else {
            return search(id, (((lwr + upr) / 2)) + ((lwr + upr) % 2), upr);
        }
    }

    /*
    * printLib() prints information about all Items in the Library.
     */

    public void printLib(){
        String temp;
        for (int i = 0; i < list.size(); i++) {
            System.out.println("Id = " + list.get(i).getId());
            System.out.println("Name = " + list.get(i).getName());
            System.out.println("Type = " + list.get(i).getType());
            System.out.println("Check out period = " + list.get(i).getCheckOutTime());
            if (list.get(i).getType().compareToIgnoreCase("cd") == 0) {
                Cd tmp = (Cd) list.get(i);
                System.out.println("Artist = " + tmp.getArtist() + "\n");
            }
            if (list.get(i).getType().compareToIgnoreCase("book") == 0) {
                Book tmp = (Book) list.get(i);
                System.out.println("Artist = " + tmp.getAuthor() + "\n");
           }

        }
    }
}
