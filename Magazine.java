package ics372Prgm1Items;

import java.util.Calendar;
/**
 * Created by Kevin on 1/22/2017.
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
