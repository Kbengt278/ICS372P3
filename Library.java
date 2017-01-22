package ics372Prgm1;

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
            int days = 7;
            if (list.get(index).getType().compareToIgnoreCase("book") == 0)
                days = 21;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, days);
            list.get(index).setAvailable(false);
            list.get(index).setDateDue(cal);
            return list.get(index);
        }
    }

    public boolean addFileData(File file){
        Item item = new Item();
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
                        index = search(item.getId(), 0, list.size()-1);
                        if (index < 0) {
                            list.add(-(index + 1), new Item(item.getId(), item.getName(), item.getType(), item.getAuthorArtist()));
                        }
                        //break;
                    }
                    break;
                case START_OBJECT:
                case VALUE_FALSE:
                case VALUE_NULL:
                case VALUE_TRUE:
//                    System.out.println(event.toString());
                    item.setAuthorArtist("");
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
                            item.setName(value);
                            break;
                        case "item_type" :
                            item.setType(value);
                            break;
                        case "item_id" :
                            item.setId(value);
                            break;
                        case "item_artist" :
                        case "item_author" :
                            item.setAuthorArtist(value);
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
        for (int i = 0; i < list.size(); i++) {
            System.out.println("Id = " + list.get(i).getId());
            System.out.println("Name = " + list.get(i).getName());
            System.out.println("Type = " + list.get(i).getType());
            System.out.println("AuthorArtist = " + list.get(i).getAuthorArtist() + "\n");

        }
    }
}
