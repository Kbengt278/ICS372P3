package Items;

/**
 * Class for Magazine type Items
 */
public class Magazine extends Item {

    public Magazine() {
        super();
    }

    public Magazine(String id, String name, String type) {
        super(id, name, type);
        checkOutTime = 7;
    }
}
