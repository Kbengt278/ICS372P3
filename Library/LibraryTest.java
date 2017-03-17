package Library;

import Items.*;
import junit.framework.TestCase;

import java.awt.*;

public class LibraryTest extends TestCase {

    Library testLibrary;
    TextArea text;

    Item itemNull;
    Item itemBookAvailable;
    Item itemBookCheckedOut;
    Item itemCdAvailable;
    Item itemCdCheckedOut;
    Item itemDvdAvailable;
    Item itemDvdCheckedOut;
    Item itemMagazineAvailable;
    Item itemMagazineCheckedOut;

    public void setUp() throws Exception {
        super.setUp();

        testLibrary = new Library();

        itemNull = null;
        itemBookAvailable = new Book("book1", "itemBookAvailable", "Book", "Author1");
        itemBookCheckedOut = new Book("book2", "itemBookCheckedOut", "Book", "Author2");
        itemCdAvailable = new Cd("cd1", "itemCdAvailable", "CD", "Artist1");
        itemCdCheckedOut = new Cd("cd2", "itemCdCheckedOut", "CD", "Artist1");
        itemDvdAvailable = new Dvd("dvd1", "itemDvdAvailable", "DVD");
        itemDvdCheckedOut = new Dvd("dvd2", "itemDvdCheckedOut", "DVD");
        itemMagazineAvailable = new Magazine("magazine1", "itemMagazineAvailable", "Magazine");
        itemMagazineCheckedOut = new Magazine("magazine2", "itemMagazineCheckedOut", "Magazine");

//        testLibrary.list.add();

        int cardNumber1 = 1;
        int cardNumber2 = 2;
    }

    public void tearDown() throws Exception {

    }

    public void testCheckIn() throws Exception {
        Item itemNull = null;
        Item itemCheckedOut;
        Item itemAvailable;

        int cardNumber1 = 1;
        int cardNumber2 = 2;

//        assertEquals(null, checkIn(itemNull, ));
//        assertEquals(null, checkIn(itemBookAvailable,));
//        assertEquals(null, checkIn(itemBookCheckedOut,));
//        assertEquals(item, checkIn());
    }

    public void testCheckOut() throws Exception {

    }

    public void testAddFileDataJson() throws Exception {

    }

    public void testAddFileDataXml() throws Exception {

    }

    public void testDisplayItems() throws Exception {

    }

    public void testGetItem() throws Exception {

    }

}