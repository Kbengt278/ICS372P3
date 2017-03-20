package Items;

/**
 * Class for Magazine type Items
 */
public class Magazine extends Item {

    private String volume;

    public Magazine() {
        super();
    }

    public Magazine(String id, String name, Type type, String volume) {
        super(id, name, type);
        this.volume = volume;
        checkOutTimeDays = 7;
    }

    public Magazine(String id, String name, Type type) {
        super(id, name, type);
        this.volume = "";
        checkOutTimeDays = 7;
    }
}
