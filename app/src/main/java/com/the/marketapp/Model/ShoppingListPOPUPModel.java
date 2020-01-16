package com.the.marketapp.Model;

public class ShoppingListPOPUPModel {

    String idShoppinListPopUp;
    String customerIDShoppinListPopUp;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    Boolean status;

    public String getIdShoppinListPopUp() {
        return idShoppinListPopUp;
    }

    public void setIdShoppinListPopUp(String idShoppinListPopUp) {
        this.idShoppinListPopUp = idShoppinListPopUp;
    }

    public String getCustomerIDShoppinListPopUp() {
        return customerIDShoppinListPopUp;
    }

    public void setCustomerIDShoppinListPopUp(String customerIDShoppinListPopUp) {
        this.customerIDShoppinListPopUp = customerIDShoppinListPopUp;
    }

    public String getNameShoppingListPopUp() {
        return nameShoppingListPopUp;
    }

    public void setNameShoppingListPopUp(String nameShoppingListPopUp) {
        this.nameShoppingListPopUp = nameShoppingListPopUp;
    }

    String nameShoppingListPopUp;

    public ShoppingListPOPUPModel() {
    }

    public ShoppingListPOPUPModel(String idShoppinListPopUp, String customerIDShoppinListPopUp, String nameShoppingListPopUp,Boolean status) {
        this.idShoppinListPopUp=idShoppinListPopUp;
        this.nameShoppingListPopUp = nameShoppingListPopUp;
        this.customerIDShoppinListPopUp=customerIDShoppinListPopUp;
        this.status=status;

    }
}
