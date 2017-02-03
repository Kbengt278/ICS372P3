package ics372Prgm1UI;

import ics372Prgm1Items.Item;
import ics372Prgm1Library.Library;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.Calendar;

import javafx.stage.FileChooser;


/**
 * This program will take a .JSON file of items in a Library, and create a collection
 * of the items. Then it will allow items to be checcked out and checked back in.
 * The 4 types of items are : CD, DVD, book, and magazine.
 *
 * @author Kevin Bengtson
 */
public class LibraryUI extends Application {

    private final TextField itemId = new TextField();
    private final TextArea text = new TextArea();
    private final ScrollPane scrollPane = new ScrollPane();
    private final FileChooser fileChooser = new FileChooser();
    private Library lib = new Library();
    //   Library lib = new Library();

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) throws IOException {

        // Create a border pane
        VBox pane = new VBox();

        // Create a HBox plane for the Top pane
        HBox topPane = new HBox();
        topPane.setAlignment(Pos.CENTER);
        topPane.setPadding(new Insets(.5, .5, .5, .5));

        // Create the left top VBox
        VBox leftTopPane = new VBox(9);
        leftTopPane.setAlignment(Pos.BASELINE_LEFT);
        leftTopPane.getChildren().addAll(new Label("Item ID: "));

        // Create the right top VBox for the HBox Top pane
        VBox rightTopPane = new VBox();
        Button btAddFileData = new Button("Add File Data");
        btAddFileData.setMaxWidth(Double.MAX_VALUE);
        Button btCheckOut = new Button("Check Out");
        btCheckOut.setMaxWidth(Double.MAX_VALUE);
        Button btCheckIn = new Button("Check In");
        btCheckIn.setMaxWidth(Double.MAX_VALUE);
        rightTopPane.getChildren().addAll(itemId, btAddFileData, btCheckOut, btCheckIn);

        // put the VBoxs in the HBox Top pane
        topPane.getChildren().addAll(leftTopPane, rightTopPane);
        HBox.setHgrow(leftTopPane, Priority.ALWAYS);
        HBox.setHgrow(rightTopPane, Priority.ALWAYS);

        // Create a HBox plane for the File Button pane
        HBox textPane = new HBox();
        textPane.setAlignment(Pos.CENTER);
        textPane.setPadding(new Insets(1.5, .5, .5, .5));
        textPane.getChildren().add(scrollPane);
        scrollPane.setContent(text);
        HBox.setHgrow(textPane, Priority.ALWAYS);
        textPane.setMaxHeight(Double.MAX_VALUE);
        textPane.setMinHeight(300);
        text.setScrollTop(Double.MAX_VALUE);

        // Place nodes in the pane
        pane.getChildren().addAll(topPane, textPane);

        //
        // Create a scene and place it in the stage
        //
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Library"); // Set the stage title
        primaryStage.setHeight(350);
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        //
        // Process the btCheckIn button -- run the simulation
        //
        btCheckIn.setOnAction(e -> {
            Item item = (lib.checkIn(itemId.getText().trim()));
            if (item == null) {
                text.appendText("Item " + itemId.getText().trim() + " does not exist\n");
            } else {
                text.appendText("Item " + itemId.getText().trim() + " "
                        + item.getType() + " : "
                        + item.getName() + "\n" +
                        "checked in successfully\n");
            }
        });

        //
        // Process the btCheckOut button -- call the writeFile() method
        //
        btCheckOut.setOnAction(e -> {
            Item item = (lib.checkOut(itemId.getText().trim()));
            if (item == null) {
                text.appendText("Item " + itemId.getText().trim() + " is not available\n");
            } else {
                text.appendText("Item " + itemId.getText().trim() + " "
                        + item.getType() + " : "
                        + item.getName() + "\n"
                        + "checked out successfully. Due date is "
                        + (item.getDateDue().get(Calendar.MONTH) + 1)
                        + "/" + item.getDateDue().get(Calendar.DAY_OF_MONTH)
                        + "/" + item.getDateDue().get(Calendar.YEAR) + "\n");
            }
        });

        //
        // Process the btAddFileData button -- call the readFile() method
        //
        btAddFileData.setOnAction(e -> {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("JSON", "*.json"),
                    new FileChooser.ExtensionFilter("TXT", "*.txt")
            );
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                text.appendText("File added: " + file.getAbsolutePath() + "\n");
                lib.addFileData(file);
            } else {
                text.appendText("No file added." + "\n");
            }
//            lib.printLib();
        });
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
