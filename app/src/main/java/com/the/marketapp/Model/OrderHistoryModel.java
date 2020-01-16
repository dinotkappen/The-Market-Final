package com.the.marketapp.Model;

public class OrderHistoryModel {

    String idOrderHistory;
    String dateOrderHistory;
    String refrenceOrderHistory;

    public String getStatusOrderHistory() {
        return statusOrderHistory;
    }

    public void setStatusOrderHistory(String statusOrderHistory) {
        this.statusOrderHistory = statusOrderHistory;
    }

    String statusOrderHistory;

    public String getIdOrderHistory() {
        return idOrderHistory;
    }

    public void setIdOrderHistory(String idOrderHistory) {
        this.idOrderHistory = idOrderHistory;
    }

    public String getDateOrderHistory() {
        return dateOrderHistory;
    }

    public void setDateOrderHistory(String dateOrderHistory) {
        this.dateOrderHistory = dateOrderHistory;
    }

    public String getRefrenceOrderHistory() {
        return refrenceOrderHistory;
    }

    public void setRefrenceOrderHistory(String refrenceOrderHistory) {
        this.refrenceOrderHistory = refrenceOrderHistory;
    }

    public String getPriceOrderHistory() {
        return priceOrderHistory;
    }

    public void setPriceOrderHistory(String priceOrderHistory) {
        this.priceOrderHistory = priceOrderHistory;
    }

    String priceOrderHistory;

    public OrderHistoryModel(String idOrderHistory, String dateOrderHistory, String refrenceOrderHistory, String priceOrderHistory,String statusOrderHistory) {

        this.idOrderHistory=idOrderHistory;
        this.dateOrderHistory=dateOrderHistory;
        this.refrenceOrderHistory=refrenceOrderHistory;
        this.priceOrderHistory=priceOrderHistory;
        this.statusOrderHistory=statusOrderHistory;


    }
}
