package Items;

/**
 * Class for Book type Items
 */
public class Book extends Item {

    private String author;

    public Book() {
        super();
    }

    public Book(String id, String name, Type type, String author) {
        super(id, name, type);
        this.author = author;
        checkOutTimeDays = 21;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
