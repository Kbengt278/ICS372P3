package ics372Prgm1;

import java.util.Calendar;
/**
 * Created by Kevin on 1/22/2017.
 */
public class Cd extends Item {


    private String artist;

    public Cd() {
        super();
    }

    public Cd(String id, String name, String type, String artist) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
        this.artist = artist;
        available = true;
        checkOutTime = 7;
        dateDue = Calendar.getInstance();
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

}
