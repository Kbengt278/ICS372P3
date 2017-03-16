package storage;

import Controller.Controller;
import Member.MemberIdServer;

import java.io.*;

/**
 * Storage class
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
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } catch (ClassNotFoundException e) {

        }
        return app;
    }

    public static MemberIdServer loadServer() {

        try {
            FileInputStream serverFile = new FileInputStream(memberServerFile);
            ObjectInputStream serverInput = new ObjectInputStream(serverFile);
            MemberIdServer.retrieve(serverInput);
            serverInput.close();
            serverFile.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
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

        } catch (IOException e) {

        }

        // Save the MemberIdServer
        try {
            FileOutputStream serverFile = new FileOutputStream(memberServerFile);
            ObjectOutputStream serverOut = new ObjectOutputStream(serverFile);
            serverOut.writeObject(idServer);
            serverOut.close();
            serverFile.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

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
