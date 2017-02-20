package ics372Prgm1Items;

import java.util.Calendar;
/**
 * Created by Kevin on 1/22/2017.
 */
public class Dvd extends Item {

    public Dvd() {
        super();
    }

    public Dvd(String id, String name, String type) {
        super(id, name, type);
        checkOutTime = 7;
    }
}
