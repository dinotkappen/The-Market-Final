package com.the.marketapp.Model;

public class Favourite_Item_Model {
    private String name;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String price;


 String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    String thumbnail;



    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    String qty;

    public Favourite_Item_Model() {
    }

    public Favourite_Item_Model(String id, String name,String qty, String price,String thumbnail) {
        this.id=id;
        this.name = name;
        this.qty = qty;
        this.price=price;
        this.thumbnail=thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
