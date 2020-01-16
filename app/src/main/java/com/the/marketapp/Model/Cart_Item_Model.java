package com.the.marketapp.Model;

public class Cart_Item_Model {


    private String total;



    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCartCount() {
        return cartCount;
    }

    public void setCartCount(String cartCount) {
        this.cartCount = cartCount;
    }

    private String cartCount;
    private String name;

    public String getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(String product_ID) {
        this.product_ID = product_ID;
    }

    private String product_ID;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String price;
    private String thumbnail;


    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    private String sub_total;


    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    String qty;

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    String params;

    public Cart_Item_Model() {
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Cart_Item_Model(String product_ID, String name, String thumbnail, String qty, String price, String sub_total, String params, String total,
                           String cartCount) {
        this.name = name;
        this.thumbnail = thumbnail;

        this.qty = qty;
        this.price = price;
        this.product_ID = product_ID;
        this.sub_total = sub_total;
        this.params = params;
        this.cartCount = cartCount;
        this.total = total;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
