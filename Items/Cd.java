package Items;


import java.io.Serializable;

/**
 * Class for CD type Items.
 */
public class Cd extends Item implements Serializable{   // added Ser even though
                                                        // don't think necessary

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
