package com.the.marketapp.Model;

public class Unit_List_Model {
    String id;
    String avail_qty;
    String price;




    public Unit_List_Model() {
    }

    public Unit_List_Model(String id, String avail_qty, String price, String value) {
        this.id = id;
        this.avail_qty = avail_qty;
        this.price = price;
        this.value = value;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvail_qty() {
        return avail_qty;
    }

    public void setAvail_qty(String avail_qty) {
        this.avail_qty = avail_qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    String value;
}
