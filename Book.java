package ics372Prgm1;

import java.util.Calendar;
/**
 * Created by Kevin on 1/22/2017.
 */
public class Book extends Item {

    private String author;

    public Book(String id, String name, String type, String author) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
        this.author = author;
        available = true;
        checkOutTime = 21;
        dateDue = Calendar.getInstance();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
