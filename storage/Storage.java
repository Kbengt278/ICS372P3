package storage;

import Controller.Controller;
import Member.MemberIdServer;

import javax.swing.*;
import java.io.*;

/**
 * Storage class: this class works with the ControllerData.bin and MemberServerData.bin files
 * To create a persistent state for the library items, libraries, and member checkouts.
 * It is dependent on instances of the Controller and MemberIdServer classes.
 * Check-ins are saved after each completion. Check-outs are saved after each completion.
 */
public class Storage {

    private static final String controllerFile = "ControllerData.bin";
    private static final String memberServerFile = "MemberServerData.bin";

    private static Controller app = new Controller();
    private static MemberIdServer server = MemberIdServer.instance();

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
        catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error loading controller file. Restart the application" +
                    " to try loading the file again.");
        }
        // TODO decide if we want to exit the program if it cannot load the controller file.
        return app;
    }


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
                    "Could not find the member file. Please try again. ");
        }

        catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Could not read the member file. Please try again.");
        }
        // TODO decide if we want to exit the program if it cannot load the member file.
        // This means any saved information was not loaded, so information used could be inaccurate.
        return server;
    }

    public static boolean save(Controller contData, MemberIdServer idServer) {

        // Save the whole controller object
        try {
            FileOutputStream file = new FileOutputStream(controllerFile);
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(contData);
            output.close();
            file.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Could not find the controller file. Please try again.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Could not save changes to the controller file. Please retry.");
        }

        // Save the MemberIdServer
        try {
            FileOutputStream serverFile = new FileOutputStream(memberServerFile);
            ObjectOutputStream serverOut = new ObjectOutputStream(serverFile);
            serverOut.writeObject(idServer);
            serverOut.close();
            serverFile.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Could not find or access the member file. Please retry.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Could not save changes to the member file. Please retry.");
        }
        return true;
    }


    public static boolean save(Controller contData) {

        // Save the whole controller object
        try {
            FileOutputStream file = new FileOutputStream(controllerFile);
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(contData);
            output.close();
            file.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        return true;
    }
}
