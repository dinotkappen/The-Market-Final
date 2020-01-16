package com.the.marketapp.Model;

public class SearchModel {
    String productIDSearch;

    public String getProductIDSearch() {
        return productIDSearch;
    }

    public void setProductIDSearch(String productIDSearch) {
        this.productIDSearch = productIDSearch;
    }

    public String getProductNameSearch() {
        return ProductNameSearch;
    }

    public void setProductNameSearch(String productNameSearch) {
        ProductNameSearch = productNameSearch;
    }

    String ProductNameSearch;

    public SearchModel(String productIDSearch,String ProductNameSearch) {
        this.productIDSearch=productIDSearch;
        this.ProductNameSearch = ProductNameSearch;

    }
}
