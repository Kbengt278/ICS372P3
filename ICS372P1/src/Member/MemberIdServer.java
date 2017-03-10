package Member;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Created by matthew on 3/9/2017.
 */
public class MemberIdServer implements Serializable {

    private int idCounter = 1;              // New server starts at 1
    private transient static MemberIdServer server;                 // not sure about transient
                                                                    // tried with and without
    // Singleton
    private MemberIdServer(){}
   // public static Object SYNC = new Object();                     // Commented out stuff here
    public static MemberIdServer instance(){                        // is how he had done singleton
        if (server == null){                                        // in class, but though it might
         //   synchronized (SYNC){                                  // be giving me problems
           //     if (server == null)                               // changed to how book did this
             return       (server = new MemberIdServer());
            }
        else
            return server;
    }

    public int getId(){
        return idCounter++;
    }

    public static void retrieve(ObjectInputStream in){
        try {
            server = (MemberIdServer) in.readObject();
        }catch (ClassNotFoundException e){

        }catch(IOException e){}
    };
}
