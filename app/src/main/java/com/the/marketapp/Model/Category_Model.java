package com.the.marketapp.Model;

public class Category_Model {
    String id;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    Boolean status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    String category_name;

    public Category_Model() {
    }

    public Category_Model(String id, String category_name,Boolean status) {
        this.id = id;
        this.category_name = category_name;
        this.status=status;


    }


}
