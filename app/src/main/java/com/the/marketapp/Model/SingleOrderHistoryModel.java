package com.the.marketapp.Model;

public class SingleOrderHistoryModel {



   String imgSingleOrderHistory;
   String qtySingleOrderHistory;
   String priceSingleOrderHistory;
   String itemNameSingleOrderHistory;

    public String getImgSingleOrderHistory() {
        return imgSingleOrderHistory;
    }

    public void setImgSingleOrderHistory(String imgSingleOrderHistory) {
        this.imgSingleOrderHistory = imgSingleOrderHistory;
    }

    public String getQtySingleOrderHistory() {
        return qtySingleOrderHistory;
    }

    public void setQtySingleOrderHistory(String qtySingleOrderHistory) {
        this.qtySingleOrderHistory = qtySingleOrderHistory;
    }

    public String getPriceSingleOrderHistory() {
        return priceSingleOrderHistory;
    }

    public void setPriceSingleOrderHistory(String priceSingleOrderHistory) {
        this.priceSingleOrderHistory = priceSingleOrderHistory;
    }

    public String getItemNameSingleOrderHistory() {
        return itemNameSingleOrderHistory;
    }

    public void setItemNameSingleOrderHistory(String itemNameSingleOrderHistory) {
        this.itemNameSingleOrderHistory = itemNameSingleOrderHistory;
    }

    public SingleOrderHistoryModel(String imgSingleOrderHistory, String qtySingleOrderHistory, String priceSingleOrderHistory, String itemNameSingleOrderHistory) {

        this.imgSingleOrderHistory=imgSingleOrderHistory;
        this.qtySingleOrderHistory=qtySingleOrderHistory;
        this.priceSingleOrderHistory=priceSingleOrderHistory;
        this.itemNameSingleOrderHistory=itemNameSingleOrderHistory;



    }
}
