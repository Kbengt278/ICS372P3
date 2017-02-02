package ics372Prgm1Items;

import java.util.Calendar;
/**
 * Created by Kevin on 1/22/2017.
 */
public class Dvd extends Item {


    public Dvd(String id, String name, String type) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
        available = true;
        checkOutTime = 7;
        dateDue = Calendar.getInstance();
    }

}
