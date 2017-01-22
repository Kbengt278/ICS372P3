package ics372Prgm1;

import java.util.Calendar;

/**
 * Created by Kevin on 1/20/2017.
 */
public class Item {

    private String id;
    private String name;
    private String type;
    private String authorArtist;
    private boolean available;
    private Calendar dateDue;



    public Item() {};

    public Item(String id, String name, String type, String authorArtist) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.authorArtist = authorArtist;
        available = true;
        dateDue = Calendar.getInstance();
    }

    public Item(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.authorArtist = "";
        available = true;
        dateDue = Calendar.getInstance();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getAuthorArtist() {
        return authorArtist;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAuthorArtist(String authorArtist) {
        this.authorArtist = authorArtist;
    }

    public Calendar getDateDue() {
        return dateDue;
    }

    public void setDateDue(Calendar dateDue) {
        this.dateDue = dateDue;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
