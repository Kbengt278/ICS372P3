package Storage;

import Member.MemberIdServer;
import Controller.Controller;
import UI.LibraryUI;

import java.io.*;

/**
 * Created by matthew on 3/9/2017.
 */
public class Storage {

    private static final String controllerFile = "ControllerData.bin";          // I tried different file types
    private static final String memberServerFile = "MemberServerData.bin";      // not sure if it matters



    public static void load(Controller app){
        try {


            FileInputStream file = new FileInputStream(controllerFile);


            ObjectInputStream input = new ObjectInputStream(file);
            app = (Controller) input.readObject();
            input.close();
            file.close();
        }catch(FileNotFoundException e){
        }catch (IOException e){}
        catch (ClassNotFoundException e){}



        try{
            FileInputStream serverFile = new FileInputStream(memberServerFile);
            ObjectInputStream serverInput = new ObjectInputStream(serverFile);
            MemberIdServer.retrieve(serverInput);
            serverInput.close();
            serverFile.close();
        }catch(FileNotFoundException e){

        }catch (IOException e){}


    };

    public static boolean save(Controller contData, MemberIdServer idServer) {

        // Save the whole controller object
        try {
            FileOutputStream file = new FileOutputStream(controllerFile);
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(contData);
            output.close();
            file.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e){}

        // Save the MemberIdServer
        try {
            FileOutputStream serverFile = new FileOutputStream(memberServerFile);
            ObjectOutputStream serverOut = new ObjectOutputStream(serverFile);
            serverOut.writeObject(idServer);
            serverOut.close();
            serverFile.close();
        }catch (FileNotFoundException e){

        }catch (IOException e){}


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
        } catch (IOException e){}



        return true;
    }
}
