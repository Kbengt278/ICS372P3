package UI;

import Controller.Controller;
import Items.Item;
import Items.Item.Status;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * This program will take a .JSON or .xml file of items in a Library, and create a collection
 * of the items. Then it will allow items to be checked out and checked back in.
 * The 4 types of items are : CD, DVD, book, and magazine.
 */

public class LibraryUI extends Application {

    // itemID is the unique string identifier (digits and letters) for a library item,
    // and is tracked during check in's and check out's.
    private final TextField itemId = new TextField();
    private final TextField itemId2 = new TextField();
    // cardNumber is a unique integer associated with a library user. The card numbers are
    // incremented up by 1 each time a user is added via the MemberIdServer class.
    private final TextField cardNumber = new TextField();
    // text area for outputting messages to the user during program execution.
    private final TextArea text = new TextArea();
    // Allows for scrolling of the text area; important for listing library items and
    // outputting longer checked-out lists for the user.
    private final ScrollPane scrollPane = new ScrollPane();
    // fileChooser is used to pick the XML and JSON files sent from the sister library and add them
    // to the application data.
    private final FileChooser fileChooser = new FileChooser();
    // Specifies that this application is the main Library using Type
    private Library.Library.Type library = Library.Library.Type.MAIN;
    // Instantiation of the Controller object that will load any serialized Controller data, and then be used
    // in the execution of the application.
    private Controller app = new Controller();
    Stage changeItemStatusStage = new Stage();

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override // Override the start method in the Controller class
    public void start(Stage primaryStage) throws IOException {
        app = Storage.Storage.loadController(); // Load data from file

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
        cbBooks.setSelected(true);
        cbCDs.setSelected(true);
        cbDVDs.setSelected(true);
        cbMagazines.setSelected(true);
        cbBooks.setMinWidth(100);
        cbCDs.setMinWidth(100);
        cbDVDs.setMinWidth(100);
        cbMagazines.setMinWidth(100);
        itemsPane.getChildren().addAll(cbBooks, cbCDs, cbDVDs, cbMagazines);


        // Create the right top VBox for the HBox Top pane
        VBox rightTopPane = new VBox();
        Button btAddFileData = new Button("Add File Data");
        btAddFileData.setMaxWidth(Double.MAX_VALUE);
        Button btAddMember = new Button("Add Member");
        btAddMember.setMaxWidth(Double.MAX_VALUE);
        Button btCheckOut = new Button("Check Out");
        btCheckOut.setMaxWidth(Double.MAX_VALUE);
        Button btCheckIn = new Button("Check In");
        btCheckIn.setMaxWidth(Double.MAX_VALUE);
        Button btChangeItemStatus = new Button("Change/Check Item Status");
        btChangeItemStatus.setMaxWidth(Double.MAX_VALUE);
        Button btDisplay = new Button("Display Library Items");
        btDisplay.setMaxWidth(Double.MAX_VALUE);
        Button btCheckedOut = new Button("User's checked out items");
        btCheckedOut.setMaxWidth(Double.MAX_VALUE);
        rightTopPane.getChildren().addAll(itemId, cardNumber, btAddFileData, btAddMember, btCheckOut,
                btCheckIn, btChangeItemStatus, btCheckedOut, btDisplay);

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
        scrollPane.setFitToWidth(true);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        textPane.setMaxHeight(Double.MAX_VALUE);
        textPane.setPrefHeight(400);
        text.setScrollTop(Double.MAX_VALUE);

        // Place nodes in the pane
        pane.getChildren().addAll(topPane, itemsPane, textPane);

        //
        // Create a scene and place it in the stage
        //
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Library"); // Set the stage title
        primaryStage.setHeight(750);
        primaryStage.setWidth(750);
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
            try {
                text.appendText(app.checkIn(itemId.getText().trim(), library));
            } catch (NumberFormatException ex) {
                text.appendText("\nIncorrect card number format");
            }
        });

        //
        // Create the ChangeItemStatus Popup
        //
        // Create a border pane
        VBox pane2 = new VBox();

        // Create a HBox plane for the Top pane
        HBox topPane2 = new HBox();
        topPane2.setAlignment(Pos.CENTER);
        topPane2.setPadding(new Insets(.5, .5, .5, .5));

        // Create the radio buttons for the States
        final ToggleGroup states = new ToggleGroup();
        RadioButton rbCheckStatus = new RadioButton("Check Item Status");
        rbCheckStatus.setToggleGroup(states);
        RadioButton rbCheckedIn = new RadioButton("Checked In");
        rbCheckedIn.setToggleGroup(states);
        RadioButton rbMissing = new RadioButton("Missing");
        rbMissing.setToggleGroup(states);
        RadioButton rbOverdue = new RadioButton("Overdue");
        rbOverdue.setToggleGroup(states);
        RadioButton rbShelving = new RadioButton("Shelving");
        rbShelving.setToggleGroup(states);
        RadioButton rbRemoved = new RadioButton("Removed From Circulation");
        rbRemoved.setToggleGroup(states);
        RadioButton rbReference = new RadioButton("Reference Only");
        rbReference.setToggleGroup(states);
        VBox bottomPane = new VBox();
        bottomPane.setPadding(new Insets(.5, .5, .5, 6.5));
        bottomPane.getChildren().addAll(rbCheckStatus, rbCheckedIn, rbMissing, rbOverdue, rbReference, rbRemoved, rbShelving);

        // Create the left top VBox
        VBox leftTopPane2 = new VBox(9);
        leftTopPane2.setAlignment(Pos.BASELINE_LEFT);
        leftTopPane2.getChildren().addAll(new Label("Item ID: "));


        // Create the right top VBox for the HBox Top pane
        VBox rightTopPane2 = new VBox();
        rightTopPane2.getChildren().addAll(itemId2);

        // put the VBoxs in the HBox Top pane
        topPane2.getChildren().addAll(leftTopPane2, rightTopPane2);
        HBox.setHgrow(leftTopPane2, Priority.ALWAYS);
        HBox.setHgrow(rightTopPane2, Priority.ALWAYS);

        HBox buttonPane = new HBox();
        buttonPane.setPadding(new Insets(3.5, 3.5, .5, 6.5));
        Button btSave = new Button("Save");
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.getChildren().addAll(btSave);


        pane2.getChildren().addAll(topPane2, bottomPane, buttonPane);
//        changeItemStatusStage.setTitle("Change Item State");
        changeItemStatusStage.setTitle("Change/Check Item Status"); // Set the stage title
        changeItemStatusStage.setHeight(250);
        changeItemStatusStage.setWidth(300);
        changeItemStatusStage.setScene(new Scene(pane2)); // Place the scene in the stage

        btSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
//                System.out.println("Click save");
                if (rbMissing.isSelected()) {
                    text.appendText(app.changeItemStatus(itemId2.getText().trim(),Item.Status.MISSING, library));
                } else if (rbOverdue.isSelected()) {
                    text.appendText(app.changeItemStatus(itemId2.getText().trim(),Item.Status.OVERDUE, library));
                } else if (rbCheckStatus.isSelected()) {
                    text.appendText(app.changeItemStatus(itemId2.getText().trim(),Item.Status.CHECK_STATUS, library));
                } else if (rbReference.isSelected()) {
                    text.appendText(app.changeItemStatus(itemId2.getText().trim(), Status.REFERENCE_ONLY, library));
                } else if (rbRemoved.isSelected()) {
                    text.appendText(app.changeItemStatus(itemId2.getText().trim(), Status.REMOVED_FROM_CIRCULATION, library));
                }  else if (rbShelving.isSelected()) {
                    text.appendText(app.changeItemStatus(itemId2.getText().trim(), Status.SHELVING, library));
                } else if (rbCheckedIn.isSelected()) {
                    text.appendText(app.changeItemStatus(itemId2.getText().trim(), Status.CHECKED_IN, library));
                } else {
                    text.appendText("No action taken, there was no status button selected");
                }
//                System.out.println(itemId.getText().trim());
//                System.out.println(Status.CHECKED_IN.toString());
                changeItemStatusStage.close();
            }
        });


        //
        // Process the btCheckIn button -- call the checkIn() method
        //
        btChangeItemStatus.setOnAction(e -> {
                changeItemStatusStage.showAndWait();
        });

        //
        // Process the btCheckOut button -- call the checkOut() method
        //
        btCheckOut.setOnAction(e -> {
            try {
                text.appendText(app.checkOut((Integer.parseInt(cardNumber.getText().trim())), itemId.getText().trim(), library));
            } catch (NumberFormatException ex) {
                text.appendText("\nIncorrect card number format");
            }
        });

        //
        // Process the btCheckedOut button -- call the displayMemberCheckedOutItems() method
        //
        btCheckedOut.setOnAction(e -> {
            try {
                text.clear();
                text.appendText(app.displayMemberCheckedOutItems((Integer.parseInt(cardNumber.getText().trim()))));
            } catch (NumberFormatException ex) {
                text.appendText("\nIncorrect card number format");
            }
        });

        //
        // Process the btAddMember button -- call the addMember() method
        //
        btAddMember.setOnAction(e -> {
            TextInputDialog newMember = new TextInputDialog("New Member");
            newMember.setTitle("New Member");
            newMember.setContentText("Enter Member Name:");

            Optional<String> result = newMember.showAndWait();
            // if Optional contains a value, it will get it and the Controller will add a new member
            // with the next member ID available via MemberIdServer.
            if (result.isPresent()) {
                text.appendText(app.addMember(result.get()));
            }
        });

        //
        // Process the btAddFileData button -- call the addFileData() method
        //
        btAddFileData.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                app.addFileData(file, library);
                text.appendText("\nFile added: " + file.getAbsolutePath());
            } else {
                text.appendText("\nNo file added.");
            }
        });

        //
        // Process the btDisplay button -- calls the displayLibraryItems() method
        // Masks allow for the selection of more than one Item type to display.
        //
        btDisplay.setOnAction(e -> {
            text.clear();
            int mask = 0;
            if (cbBooks.isSelected()) {
                mask += 1;
            }
            if (cbCDs.isSelected()) {
                mask += 2;
            }
            if (cbDVDs.isSelected()) {
                mask += 4;
            }
            if (cbMagazines.isSelected()) {
                mask += 8;
            }
            text.appendText(app.displayLibraryItems(mask, library));
        });

        //
        // Handles getting the set Library radio button and setting
        // the variable appropriately
        //
        libraries.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (rbMain.isSelected()) {
                    library = Library.Library.Type.MAIN;
                } else {
                    library = Library.Library.Type.SISTER;
                }
                if (library ==  Library.Library.Type.MAIN)
                    text.appendText("\nMain Library:");
                else
                    text.appendText("\nSister Library:");

            }
        });
    }
}