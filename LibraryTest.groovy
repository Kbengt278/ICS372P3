package Library

import Items.Book;
import Items.Cd
import Items.Item;
import static org.junit.Assert.*;
import UI.LibraryUI;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Created by Kevin on 3/18/2017.
 */
class LibraryTest extends GroovyTestCase {


     void testCheckOutCheckIn() {
        // Initialize the lib object to null
        Library lib = new Library();
       //
        // Now get the items into the Library :
        //
        File file = new File("Test.json");
        lib.addFileDataJson(file);
        //
        // Checkout an item
        //
        Item item = lib.checkOut(1, "1adf5");
        // check that the correct item was returned
        assertEquals(lib.getItem("1adf5"), item);
        assertEquals(false, item.isAvailable());    // Check it isn't available
        assertEquals(1, item.getCheckedOutBy());    // Checked that it was checked out by 1
        // Try to check out the same  item -- it should return null
        item = lib.checkOut(1, "1adf5");
        assertEquals(null, item);
        // Try to check out a nonexistent item -- it should return null
        item = lib.checkOut(1, "invalid");
        assertEquals(null, item);
        // Now check the item in
        item = lib.checkIn(1, "1adf5");
        assertEquals(lib.getItem("1adf5"), item);
        assertEquals(true, item.isAvailable());    // Check it is available
        assertEquals(0, item.getCheckedOutBy());    // Checked that it was checked out by 1




    }

    void testAddFileDataJson() {
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

    void testAddFileDataXml() {
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

    void testDisplayItems() {
        // Initialize the lib object to null
        Library lib = new Library();
        //
        // Get the Library data from a file
        //
        File file = new File("Test.json");
        lib.addFileDataJson(file);
        Item item = lib.checkOut(1, "48934k");
        System.out.println(lib.displayItems("cd"));
    }

 }
