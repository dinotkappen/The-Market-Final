package com.the.marketapp.Model;

public class Item_Sub_Model {
    private String name;

    public String getStatusWishList() {
        return statusWishList;
    }

    public void setStatusWishList(String statusWishList) {
        this.statusWishList = statusWishList;
    }

    String statusWishList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String price;
    private String thumbnail;




    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    String qty;

    public Item_Sub_Model() {
    }

    public Item_Sub_Model(String id,String name, String thumbnail,String qty, String price,String statusWishList) {
       this.id=id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.qty = qty;
        this.price = price;
        this.statusWishList=statusWishList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
