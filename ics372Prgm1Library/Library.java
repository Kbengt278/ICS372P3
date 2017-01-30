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


/**
 * Created by Kevin on 1/20/2017.
 */
public class Library {

    private ArrayList<Item> list = new ArrayList<>(100);

    public Library() {
    }

    public boolean checkIn(String id) {
        int index = search(id, 0, list.size() - 1);
        if (index < 0)
            return false;
        else {
            list.get(index).setAvailable(true);
            return true;
        }
    }

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
//                    System.out.println(event.toString());
                    break;
                case END_ARRAY:
                    startArray = false;
//                    System.out.println(event.toString());
                    break;
                case END_OBJECT:
                    if (startArray) {
                        index = search(id, 0, list.size()-1);
                        if (index < 0) {
                            switch (type.toLowerCase()){
                                case "cd" :
                                    list.add(-(index + 1), new Cd(id, name, type, authorArtist));
                                    break;
                                case "book" :
                                    list.add(-(index + 1), new Book(id, name, type, authorArtist));
                                    break;
                                case "magazine" :
                                    list.add(-(index + 1), new Magazine(id, name, type));
                                    break;
                                case "dvd" :
                                    list.add(-(index + 1), new Dvd(id, name, type));
                                    break;
                            }
                        }
                        //break;
                    }
                    break;
                case START_OBJECT:
                case VALUE_FALSE:
                case VALUE_NULL:
                case VALUE_TRUE:
//                    System.out.println(event.toString());
                    break;
                case KEY_NAME:
//                    System.out.print(event.toString() + " " + parser.getString() + " - ");
                    keyName = parser.getString();
                    break;
                case VALUE_STRING:
                case VALUE_NUMBER:
//                    System.out.println(event.toString() + " " + parser.getString());
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
