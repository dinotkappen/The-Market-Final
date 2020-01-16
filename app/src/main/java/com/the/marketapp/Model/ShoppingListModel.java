package com.the.marketapp.Model;

public class ShoppingListModel {

    public String getIdShoppinList() {
        return idShoppinList;
    }

    public void setIdShoppinList(String idShoppinList) {
        this.idShoppinList = idShoppinList;
    }

    public String getNameShoppingList() {
        return nameShoppingList;
    }

    public void setNameShoppingList(String nameShoppingList) {
        this.nameShoppingList = nameShoppingList;
    }

    String idShoppinList;

    public String getCustomerIDShoppinList() {
        return customerIDShoppinList;
    }

    public void setCustomerIDShoppinList(String customerIDShoppinList) {
        this.customerIDShoppinList = customerIDShoppinList;
    }

    String customerIDShoppinList;
    String nameShoppingList;

    public ShoppingListModel() {
    }

    public ShoppingListModel(String idShoppinList,String customerIDShoppinList,String nameShoppingList) {
        this.idShoppinList=idShoppinList;
        this.nameShoppingList = nameShoppingList;
        this.customerIDShoppinList=customerIDShoppinList;

    }
}
