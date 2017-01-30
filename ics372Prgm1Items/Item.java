package ics372Prgm1Items;

import java.util.Calendar;

/**
 * Created by Kevin on 1/20/2017.
 */
public class Item {

    protected String id;
    protected String name;
    protected String type;
    protected boolean available;
    protected Calendar dateDue;
    protected int checkOutTime;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
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

    public int getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(int checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

}
