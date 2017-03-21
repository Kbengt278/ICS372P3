package Storage;

import Controller.Controller;
import Member.MemberIdServer;

import javax.swing.*;
import java.io.*;

/**
 * Storage class: this class works with the ControllerData.bin and MemberServerData.bin files
 * To create a persistent state for the library items, libraries, and member checkouts.
 * It is dependent on instances of the Controller and MemberIdServer classes.
 * save is called whenever an instance of the Controller class completes the
 * checkIn, checkOut, addMember, and addFileData methods.
 */
public class Storage {

    // controllerFile is a serialized copy of a Controller object, which contains the main library,
    // sister library, and member list.
    private static final String controllerFile = "ControllerData.bin";
    private static final String memberServerFile = "MemberServerData.bin";

    private static Controller app = new Controller();
    private static MemberIdServer server = MemberIdServer.instance();

    // Loads a Controller object from the Controller.Data.bin file.
    public static Controller loadController() {
        try {
            FileInputStream file = new FileInputStream(controllerFile);
            ObjectInputStream input = new ObjectInputStream(file);
            app = (Controller) input.readObject();
            input.close();
            file.close();
        }
        // If the controller file is not found, notify the user that it did not load.
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Controller file not found.");
        }
        // If there is an IO Exception other than the file isn't found, notify the user and prompt them to restart the application.
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading controller file. Restart the application" +
                    " to try loading the file again.");
        }
        // If there is a ClassNotFoundException, there's something wrong with how the app loaded since it's probably missing a jar or
        // one of the other class files.
        catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error starting the library application. Restart the application" +
                    " to try loading the file again.");
        }

        /* TODO are we going to include an empty controller file when we turn this in? If one doesn't exist on the first run,
         * it is throwing FileNotFoundException. That or we need to try creating it first, since we aren't even making empty files if
         * it doesn't already exist now. */
        return app;
    }

    // Load the MemberIdServer object from its serialized file. This is needed to ensure
    // any new member IDs are incremented based off the last member ID (+1 added for each new member).
    public static MemberIdServer loadServer() {

        try {
            FileInputStream serverFile = new FileInputStream(memberServerFile);
            ObjectInputStream serverInput = new ObjectInputStream(serverFile);
            MemberIdServer.retrieve(serverInput);
            serverInput.close();
            serverFile.close();
        }
        // If the member file is not found, notify the user that it did not load.
        // This means any saved information was not loaded, so information used could be inaccurate.
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Could not find the member file. Restart the application.");
        }         // If the member did not load for another reason, notify the user that it did not load.
        // This means any saved information was not loaded, so information used could be inaccurate.
        catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Could not load the list of library member. Restart the application.");
        }
        return server;
    }

    /* Saves any changes made in the app to both the controller file and the memberServerId files.
    @param contData: the current instance of a Controller object that will be saved into ControllerData.bin
    @param idServer: the instance of MemberIdServer that will be saved into MemberServerData.bin
    @return boolean: whether the save was successful (true) or failed (false)
     */
    public static boolean save(Controller contData, MemberIdServer idServer) {

        // Save the whole controller object
        try {
            FileOutputStream file = new FileOutputStream(controllerFile);
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(contData);
            output.close();
            file.close();
        }
        // If the Controller object couldn't be saved to the controllerFile, warn the user no changes were saved.
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Could not save changes. Please try again.");
            return false;
        }
        // This means there was an error writing to or closing the Controller file. Warn the user the changes may not have saved.
        catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Could not save changes to the controller file. Please retry.");
            return false;
        }

        // Save the MemberIdServer
        try {
            FileOutputStream serverFile = new FileOutputStream(memberServerFile);
            ObjectOutputStream serverOut = new ObjectOutputStream(serverFile);
            serverOut.writeObject(idServer);
            serverOut.close();
            serverFile.close();
        }
        // If the MemberIdServer object couldn't be saved to the memberServerFile, warn the user no changes were saved.
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Could not find or access the member file. Please retry.");
            return false;
        }
        // This means there was an error writing to or closing the MemberIdServer file. Warn the user the changes may not have saved.
        catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Could not save changes to the member file. Please retry.");
            return false;
        }
        return true;
    }

    /* Saves any changes made in the app to both the controller file, and returns
    // a boolean based on whether it was successful.
    @param contData: instance of Controller to be saved in ControllerData.bin
    @return boolean: true if save was successful, false if it failed.
    */
    public static boolean save(Controller contData) {

        // Save the whole controller object
        try {
            FileOutputStream file = new FileOutputStream(controllerFile);
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(contData);
            output.close();
            file.close();
        }
        // If the Controller object couldn't be saved to the controllerFile, warn the user no changes were saved.
        catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Could not save changes. Please try again.");
            return false;
        }
        // This means there was an error writing to or closing the Controller file. Warn the user the changes may not have saved.
        catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Could not save changes to the controller file. Please retry.");
            return false;
        }
        return true;
    }
}
