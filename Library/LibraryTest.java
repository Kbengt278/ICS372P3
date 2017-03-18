package Library;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import Items.Item;

import java.io.File;

/**
 * Created by Kevin on 3/18/2017.
 */
public class LibraryTest {
    static Library lib;

    @BeforeClass
    public static void setUpClass() {
        // Initialize the lib object

        lib = new Library();
        //
        // Now get the items into the Library:
        //
        String currentDir = System.getProperty("user.dir");
        File file = new File(currentDir + "\\Test.json");
        lib.addFileDataJson(file);
        System.out.println(lib.displayItems("Book"));
    }

    @Test
    public void testCheckOutCheckIn() {
        //
        // Checkout an item
        //
        Item item = lib.getItem("1adf4");
        Boolean response = lib.checkOut("1adf4", lib);

        // check that the correct item was returned
        assertEquals(true, response);
        assertEquals(false, item.isAvailable());    // Check it isn't available

        // Try to check out the same  item -- it should return false
        response = lib.checkOut("1adf4", lib);
        assertEquals(false, response);

        // Try to check out a nonexistent item -- it should return null
        response = lib.checkOut("invalid", lib);
        assertEquals(null, response);

        // Now check the item in
        response = lib.checkIn("1adf4", lib);
        assertEquals(true, response);
        assertEquals(true, item.isAvailable());    // Check it is available
    }

    @Test
    public void testAddFileDataJson() {
        // Initialize the lib object to null
        Library lib = new Library();
        //
        // First test for a nonexistent file
        //
        File file = new File("TestFileDoesNotExist.json");
        lib.addFileDataJson(file);
        //
        // Now test with a valid file that has three entries that have errors :
        // 1) No ID (ID = "")
        // 2) Invalid type = VHS
        // 3) Name = null (No name field)
        // It then checks that 8 items were added
        //
        file = new File("Test.json");
        lib.addFileDataJson(file);
        assertEquals("Number of items added is incorrect: ", 8, lib.size());
        assertEquals("The Stand", lib.getItem("1adf5").getName());
    }

    @Test
    public void testAddFileDataXml() {
        // Initialize the lib object to null
        Library lib = new Library();
        //
        // First test for a nonexistent file
        //
        File file = new File("TestFileDoesNotExist.xml");
        lib.addFileDataXml(file);
        //
        // Now test with a valid file that has three entries that have errors :
        // 1) No ID (ID = "")
        // 2) Invalid type = VHS
        // 3) Name = null (No name field)
        // It then checks that 4 items were added
        //
        file = new File("Test.xml");
        lib.addFileDataXml(file);
        assertEquals("Number of items added is incorrect: ", 4, lib.size());
    }

    @Test
    public void testDisplayItems() {
        // Initialize the lib object to null
        Library lib = new Library();
        //
        // Get the Library data from a file
        //
        File file = new File("Test.json");
        lib.addFileDataJson(file);
        Boolean response = lib.checkOut("48934k", lib);
        System.out.println(lib.displayItems("cd"));
    }
}