package Controller;

import Items.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ControllerTest {
    private static Controller testController;

    private Item itemNull;
    private Item itemAvailable;
    private Item itemCheckedOutBy1;
    private Item itemNotAvailableAndNotCheckedOut;

    @BeforeClass
    public static void setUpClass() {
        testController = new Controller();

        testController.addMember("Member 1");
        testController.addMember("Member 2");
    }

    @Before
    public void setUp() throws Exception {

        itemNull = null;

        itemAvailable = new Item("itemAvailable", "item1", "Book");
        itemAvailable.setAvailable(true);
        testController.addItemToLibrary(itemAvailable,1);

        itemCheckedOutBy1 = new Item("itemCheckedOutBy1", "item2", "Book");
        testController.addItemToLibrary(itemCheckedOutBy1,1);
        testController.checkOut(1, "itemCheckedOutBy1", 1);

        itemNotAvailableAndNotCheckedOut = new Item("itemNotAvailableAndNotCheckedOut", "item3", "Book");
        testController.addItemToLibrary(itemNotAvailableAndNotCheckedOut,1);
        testController.getLib(1).getItem("itemNotAvailableAndNotCheckedOut").setAvailable(false);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void checkOut() throws Exception {
        String response = "";
        // check out item that doesn't exist
        response = testController.checkOut(1, "BadItemID", 1);
        assertTrue(response.contains("does not exist"));

        // check out item already checked out
        response = testController.checkOut(1, "itemCheckedOutBy1", 1);
        assertTrue(response.contains("already checked out"));

        // use invalid card number
        response = testController.checkOut(99, "itemCheckedOutBy1", 1);
        assertTrue(response.contains("invalid"));

        // test success
        response = testController.checkOut(1, "itemAvailable", 1);
        assertTrue(response.contains("checked out successfully"));
    }

    @Test
    public void checkIn() throws Exception {
        String response = "";
        // test error condition
        response = testController.checkIn("itemNotAvailableAndNotCheckedOut", 1);
        assertTrue(response.contains("marked as checked out but no member has it checked out"));

        // check in item that doesn't exist
        response = testController.checkIn("BadItemID", 1);
        assertTrue(response.contains("does not exist"));

        // check in item that is not checked out
        response = testController.checkIn("itemAvailable", 1);
        assertTrue(response.contains("is not checked out"));

        // test success
        response = testController.checkIn("itemCheckedOutBy1", 1);
        assertTrue(response.contains("checked in successfully"));
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