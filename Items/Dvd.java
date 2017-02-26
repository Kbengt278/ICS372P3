package Items;

/**
 * Class for DVD type Items
 */
public class Dvd extends Item {

    public Dvd() {
        super();
    }

    public Dvd(String id, String name, String type) {
        super(id, name, type);
        checkOutTime = 7;
    }
}
