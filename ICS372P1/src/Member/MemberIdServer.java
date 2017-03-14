package Member;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * MemberIdServer class
 */
public class MemberIdServer implements Serializable {

    private int idCounter = 1;              // New server starts at 1
    private transient static MemberIdServer server;

    // Singleton
    private MemberIdServer(){}

    public static MemberIdServer instance(){
        if (server == null) {
            return (server = new MemberIdServer());
        }
        else {
            return server;
        }
    }

    public int getId(){
        return idCounter++;
    }

    public static void retrieve(ObjectInputStream in){
        try {
            server = (MemberIdServer) in.readObject();
        }catch (ClassNotFoundException e){

        }catch(IOException e){}
    }
}
