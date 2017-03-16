package Items;

import java.io.Serializable;

/**
 * Class for DVD type Items
 */
public class Dvd extends Item implements Serializable {
    // added Ser even though
    // don't think necessary

    public Dvd() {
        super();
    }

    public Dvd(String id, String name, String type) {
        super(id, name, type);
        checkOutTime = 7;
    }
}
