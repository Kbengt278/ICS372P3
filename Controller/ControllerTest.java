package Controller;

import Items.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ControllerTest {
    private static Controller testController;

    private Item itemAvailable;
    private Item itemCheckedOutBy1;
    private Item itemNotAvailableAndNotCheckedOut;

    private Item itemBookLib1;
    private Item itemMagazineLib1;
    private Item itemCDLib1;
    private Item itemDVDLib1;
    private Item itemBookLib2;
    private Item itemMagazineLib2;
    private Item itemCDLib2;
    private Item itemDVDLib2;

    @BeforeClass
    public static void setUpClass() {
        testController = new Controller();

        testController.addMember("Member 1");
        testController.addMember("Member 2");
    }

    @Before
    public void setUp() throws Exception {

        itemAvailable = new Item("itemAvailable", "item1", "Book");
        itemAvailable.setAvailable(true);
        testController.addItemToLibrary(itemAvailable,1);

        itemCheckedOutBy1 = new Item("itemCheckedOutBy1", "item2", "Book");
        testController.addItemToLibrary(itemCheckedOutBy1,1);
        testController.checkOut(1, "itemCheckedOutBy1", 1);

        itemNotAvailableAndNotCheckedOut = new Item("itemNotAvailableAndNotCheckedOut", "item3", "Book");
        testController.addItemToLibrary(itemNotAvailableAndNotCheckedOut,1);
        testController.getLib(1).getItem("itemNotAvailableAndNotCheckedOut").setAvailable(false);

        itemBookLib1 = new Item("Book1", "Book1", "Book");
        testController.addItemToLibrary(itemBookLib1,1);
        testController.checkOut(2, "Book1", 1);
        itemMagazineLib1 = new Item("Magazine1", "Magazine1", "Magazine");
        testController.addItemToLibrary(itemMagazineLib1,1);
        testController.checkOut(2, "Magazine1", 1);
        itemCDLib1 = new Item("CD1", "CD1", "CD");
        testController.addItemToLibrary(itemCDLib1,1);
        testController.checkOut(2, "CD1", 1);
        itemDVDLib1 = new Item("DVD1", "DVD1", "DVD");
        testController.addItemToLibrary(itemDVDLib1,1);
        testController.checkOut(2, "DVD1", 1);

        itemBookLib2 = new Item("Book2", "Book2", "Book");
        testController.addItemToLibrary(itemBookLib2,2);
        testController.checkOut(2, "Book2", 2);
        itemMagazineLib2 = new Item("Magazine2", "Magazine2", "Magazine");
        testController.addItemToLibrary(itemMagazineLib2,2);
        testController.checkOut(2, "Magazine2", 2);
        itemCDLib2 = new Item("CD2", "CD2", "CD");
        testController.addItemToLibrary(itemCDLib2,2);
        testController.checkOut(2, "CD2", 2);
        itemDVDLib2 = new Item("DVD2", "DVD2", "DVD");
        testController.addItemToLibrary(itemDVDLib2,2);
        testController.checkOut(2, "DVD2", 2);

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
        assertTrue(response.contains("not checked in"));

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
        assertTrue(response.contains("not checked out"));

        // test success
        response = testController.checkIn("itemCheckedOutBy1", 1);
        assertTrue(response.contains("checked in successfully"));
    }

    @Test
    public void addFileData() throws Exception {

    }

    @Test
    public void addMember() throws Exception {
        String response = "";
        response = testController.addMember("testMember");
        assertTrue(response.contains("Library card number is: 3"));
        assertTrue(testController.checkLibraryCardNumber(3));
    }

    @Test
    public void displayLibraryItems() throws Exception {
        String response = "";
        response = testController.displayLibraryItems(1,1+2+4+8);
        assertTrue(response.contains("Book1"));
        assertTrue(response.contains("Magazine1"));
        assertTrue(response.contains("CD1"));
        assertTrue(response.contains("DVD1"));
    }

    @Test
    public void displayCheckedOutItems() throws Exception {
        String response = "";
        response = testController.displayMemberCheckedOutItems(2);
        assertTrue(response.contains("Book1"));
        assertTrue(response.contains("Magazine1"));
        assertTrue(response.contains("CD1"));
        assertTrue(response.contains("DVD1"));
        assertTrue(response.contains("Book2"));
        assertTrue(response.contains("Magazine2"));
        assertTrue(response.contains("CD2"));
        assertTrue(response.contains("DVD2"));
    }

    @Test
    public void getLib() throws Exception {

    }

    @Test
    public void checkLibraryCardNumber() throws Exception {

    }

    @Test
    public void addItemToLibrary() throws Exception {

    }

}