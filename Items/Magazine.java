package Items;

/**
 * Class for Magazine type Items
 */
public class Magazine extends Item {

    private String volume;
    public Magazine() {
        super();
    }

    public Magazine(String id, String name, String type, String volume) {
        super(id, name, type);
        this.volume = volume;
        checkOutTime = 7;
    }

    public Magazine(String id, String name, String type) {
        super(id, name, type);
        this.volume = "";
        checkOutTime = 7;
    }
}
