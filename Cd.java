package ics372Prgm1Items;

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
        super(id, name, type);
        this.artist = artist;
        checkOutTime = 7;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

}
