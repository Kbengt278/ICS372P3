package UI;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import Items.Item;
import Library.Library;
import Controller.*;



/**
 * This program will take a .JSON or .xml file of items in a Library, and create a collection
 * of the items. Then it will allow items to be checked out and checked back in.
 * The 4 types of items are : CD, DVD, book, and magazine.
 *
 */

public class LibraryUI extends Application {

    private final TextField itemId = new TextField();
    private final TextField cardNumber = new TextField();
    private final TextArea text = new TextArea();
    private final ScrollPane scrollPane = new ScrollPane();
    private final FileChooser fileChooser = new FileChooser();
    private int library = 1;
    private Library lib = new Library();
    private Controller app = new Controller();

    @Override // Override the start method in the Controller class
    public void start(Stage primaryStage) throws IOException {

        // Create a border pane
        VBox pane = new VBox();

        // Create a HBox plane for the Top pane
        HBox topPane = new HBox();
        topPane.setAlignment(Pos.CENTER);
        topPane.setPadding(new Insets(.5, .5, .5, .5));

        // Create the radio buttons for the Libraries
        final ToggleGroup libraries = new ToggleGroup();
        RadioButton rbMain = new RadioButton("Main Library");
        rbMain.setToggleGroup(libraries);
        rbMain.setSelected(true);
        RadioButton rbSister = new RadioButton("Sister Library");
        rbSister.setToggleGroup(libraries);

        // Create the left top VBox
        VBox leftTopPane = new VBox(9);
        leftTopPane.setAlignment(Pos.BASELINE_LEFT);
        leftTopPane.getChildren().addAll(new Label("Item ID: "), new Label("Library Card Number:"), new Label(""), rbMain, rbSister);

        // Create radio buttons for the different items
        HBox itemsPane = new HBox();
        itemsPane.setAlignment(Pos.CENTER);
        itemsPane.setPadding(new Insets(.5, .5, .5, .5));
        itemsPane.setMinHeight(30);
        CheckBox cbBooks = new CheckBox("Books");
        CheckBox cbCDs = new CheckBox("CDs");
        CheckBox cbDVDs = new CheckBox("DVDs");
        CheckBox cbMagazines = new CheckBox("Magazines");
        cbBooks.setMinWidth(100);
        cbCDs.setMinWidth(100);
        cbDVDs.setMinWidth(100);
        cbMagazines.setMinWidth(100);
        itemsPane.getChildren().addAll(cbBooks, cbCDs, cbDVDs, cbMagazines);


        // Create the right top VBox for the HBox Top pane
        VBox rightTopPane = new VBox();
        Button btAddFileData = new Button("Add File Data");
        btAddFileData.setMaxWidth(Double.MAX_VALUE);
        Button btCheckOut = new Button("Check Out");
        btCheckOut.setMaxWidth(Double.MAX_VALUE);
        Button btCheckIn = new Button("Check In");
        btCheckIn.setMaxWidth(Double.MAX_VALUE);
        Button btDisplay = new Button("Display Library Items");
        btDisplay.setMaxWidth(Double.MAX_VALUE);
        Button btCheckedOut = new Button("User's checked out items");
        btCheckedOut.setMaxWidth(Double.MAX_VALUE);
        rightTopPane.getChildren().addAll(itemId, cardNumber, btAddFileData, btCheckOut, btCheckIn, btCheckedOut, btDisplay);

        // put the VBoxs in the HBox Top pane
        topPane.getChildren().addAll(leftTopPane, rightTopPane);
        HBox.setHgrow(leftTopPane, Priority.ALWAYS);
        HBox.setHgrow(rightTopPane, Priority.ALWAYS);

        // Create a HBox plane for the Text pane
        HBox textPane = new HBox();
        textPane.setAlignment(Pos.CENTER);
        textPane.setPadding(new Insets(1.5, .5, .5, .5));
        textPane.getChildren().add(scrollPane);
        scrollPane.setContent(text);
        scrollPane.setFitToHeight(true);
        HBox.setHgrow(textPane, Priority.ALWAYS);
        textPane.setMaxHeight(Double.MAX_VALUE);
        textPane.setMinHeight(580);
        text.setScrollTop(Double.MAX_VALUE);


        // Place nodes in the pane
        pane.getChildren().addAll(topPane, itemsPane, textPane);


        //
        // Create a scene and place it in the stage
        //
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Library"); // Set the stage title
        primaryStage.setHeight(700);
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON", "*.json"),
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );

        //
        // Process the btCheckIn button -- call the checkIn() method
        //
        btCheckIn.setOnAction(e -> {
                app.checkIn((new Integer(cardNumber.getText()).intValue()), itemId.getText().trim(), library, text);
        });

        //
        // Process the btCheckOut button -- call the checkOut() method
        //
        btCheckOut.setOnAction(e -> {
                app.checkOut((new Integer(cardNumber.getText()).intValue()), itemId.getText().trim(), library, text);
        });

        //
        // Process the btCheckedOut button -- call the displayCheckedOutItems() method
        //
        btCheckedOut.setOnAction(e -> {
                app.displayCheckedOutItems((new Integer(cardNumber.getText()).intValue()), text);
        });

        //
        // Process the btAddFileData button -- call the addFileData() method
        //
        btAddFileData.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                text.appendText("File added: " + file.getAbsolutePath() + "\n");
                app.addFileData(file, library);
            } else {
                text.appendText("No file added." + "\n");
            }
        });

        //
        // Process the btDisplay button -- calls the displayLibraryItems() method
        //
        btDisplay.setOnAction(e -> {
            text.clear();
            int mask = 0;
            if (cbBooks.isSelected()){
                mask += 1;
            }
            if (cbCDs.isSelected()){
                mask += 2;
            }
            if (cbDVDs.isSelected()){
                mask += 4;
            }
            if (cbMagazines.isSelected()){
                mask += 8;
            }
            app.displayLibraryItems(library, text, mask);
        });

        //
        // Handles getting the set Library radio button and setting
        // the variable appropriately
        //
        libraries.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (rbMain.isSelected()){
                    library = 1;
                } else {
                    library = 2;
                }
                if (library == 1)
                    System.out.println("Main Library");
                else
                    System.out.println("Sister Library");

            }
        });
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
