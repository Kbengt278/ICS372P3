package Member;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * MemberIdServer class
 */
public class MemberIdServer implements Serializable {

    private transient static MemberIdServer server;
    private int idCounter = 1;              // New server starts at 1

    // Singleton
    private MemberIdServer() {
    }

    public static MemberIdServer instance() {
        if (server == null) {
            return (server = new MemberIdServer());
        } else {
            return server;
        }
    }

    public static void retrieve(ObjectInputStream in) {
        try {
            server = (MemberIdServer) in.readObject();
        } catch (ClassNotFoundException e) {

        } catch (IOException e) {
        }
    }

    public int getId() {
        return idCounter++;
    }
}
