package com.the.marketapp.Model;

public class Order_Summary_Model {

    String item_Name;
    String item_units;

    String subtotal;

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    String price;

    public Order_Summary_Model(String item_Name,String item_units, String item_qty,String item_Price,String subtotal,String price) {
        this.item_Name=item_Name;
        this.item_units = item_units;
        this.item_qty = item_qty;
        this.item_Price=item_Price;
        this.price=price;
        this.subtotal=subtotal;
    }

    public String getItem_Name() {
        return item_Name;
    }

    public void setItem_Name(String item_Name) {
        this.item_Name = item_Name;
    }

    public String getItem_units() {
        return item_units;
    }

    public void setItem_units(String item_units) {
        this.item_units = item_units;
    }

    public String getItem_qty() {
        return item_qty;
    }

    public void setItem_qty(String item_qty) {
        this.item_qty = item_qty;
    }

    public String getItem_Price() {
        return item_Price;
    }

    public void setItem_Price(String item_Price) {
        this.item_Price = item_Price;
    }

    String item_qty;
    String item_Price;
}
