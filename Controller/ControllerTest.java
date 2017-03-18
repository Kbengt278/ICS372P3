package Controller;

import Items.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ControllerTest {
    private Controller testController;

    private Item itemNull;
    private Item itemAvailable;
    private Item itemCheckedOutBy2;
    private Item itemCheckedOutBy1;

    @Before
    public void setUp() throws Exception {
        testController = new Controller();

        itemNull = null;

        testController.addMember("Member 1");
        testController.addMember("Member 2");

        itemAvailable = new Item("itemAvailable", "item1", "Book");
        itemAvailable.setAvailable(true);
        testController.addItemToMain(itemAvailable);

        itemCheckedOutBy2 = new Item("itemCheckedOutBy2", "item2", "Book");
        itemCheckedOutBy2.setAvailable(false);
//        itemCheckedOutBy2.setCheckedOutBy(2);
        testController.addItemToMain(itemCheckedOutBy2);

        itemCheckedOutBy1 = new Item("itemCheckedOutBy1", "item3", "Book");
        itemCheckedOutBy1.setAvailable(false);
//        itemCheckedOutBy1.setCheckedOutBy(1);
        testController.addItemToMain(itemCheckedOutBy2);


    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void checkOut() throws Exception {

    }

    @Test
    public void checkIn() throws Exception {
        // check in item that doesn't exist
        assertEquals("Item " + "BadItemID" + " does not exist\n", testController.checkIn("BadItemID", 1));
        // check in item that is not checked out
        assertEquals("Item " + "itemAvailable" + " is not checked out.\n", testController.checkIn("itemAvailable", 1));
        // check in item that is checked out by a different library ID
//        assertEquals("Item " + "itemCheckedOutBy2" + " is was not checked out by library card number " + 1 + ".\n", testController.checkIn(1, "itemCheckedOutBy2", 1));
        // test worked
        assertEquals(itemCheckedOutBy1, testController.checkIn("itemCheckedOutBy1", 1));
    }

    @Test
    public void addFileData() throws Exception {

    }

    @Test
    public void addMember() throws Exception {

    }

    @Test
    public void displayLibraryItems() throws Exception {

    }

    @Test
    public void displayCheckedOutItems() throws Exception {

    }

}