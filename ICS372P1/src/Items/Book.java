package Items;

import java.io.Serializable;

/**
 * Class for Book type Items
 */
public class Book extends Item implements Serializable{     // added impl Serial
                                                        // even though I don't think it needs it
    private String author;                              // since Item implements it

    public Book() {
        super();
    }

    public Book(String id, String name, String type, String author) {
        super(id, name, type);
        this.author = author;
        checkOutTime = 21;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
