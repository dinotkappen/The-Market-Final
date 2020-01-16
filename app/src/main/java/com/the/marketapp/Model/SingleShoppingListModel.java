package com.the.marketapp.Model;

public class SingleShoppingListModel {
    public String getIdSingleShopList() {
        return idSingleShopList;
    }

    public void setIdSingleShopList(String idSingleShopList) {
        this.idSingleShopList = idSingleShopList;
    }

    public String getImgSingleShopList() {
        return imgSingleShopList;
    }

    public void setImgSingleShopList(String imgSingleShopList) {
        this.imgSingleShopList = imgSingleShopList;
    }

    public String getNameSingleShopList() {
        return nameSingleShopList;
    }

    public void setNameSingleShopList(String nameSingleShopList) {
        this.nameSingleShopList = nameSingleShopList;
    }

    public String getQtySingleShopList() {
        return qtySingleShopList;
    }

    public void setQtySingleShopList(String qtySingleShopList) {
        this.qtySingleShopList = qtySingleShopList;
    }

    public String getPriceSingleShopList() {
        return priceSingleShopList;
    }

    public void setPriceSingleShopList(String priceSingleShopList) {
        this.priceSingleShopList = priceSingleShopList;
    }

    String idSingleShopList;

    public String getProductIDSingleShopList() {
        return productIDSingleShopList;
    }

    public void setProductIDSingleShopList(String productIDSingleShopList) {
        this.productIDSingleShopList = productIDSingleShopList;
    }

    String productIDSingleShopList;
    String imgSingleShopList;
    String nameSingleShopList;
    String qtySingleShopList;
    String priceSingleShopList;


    public SingleShoppingListModel(String idSingleShopList,String productIDSingleShopList,String imgSingleShopList, String nameSingleShopList,
                                   String qtySingleShopList, String priceSingleShopList) {
        this.idSingleShopList = idSingleShopList;
        this.imgSingleShopList = imgSingleShopList;

        this.nameSingleShopList = nameSingleShopList;
        this.qtySingleShopList = qtySingleShopList;
        this.priceSingleShopList = priceSingleShopList;
        this.productIDSingleShopList=productIDSingleShopList;

    }
}
