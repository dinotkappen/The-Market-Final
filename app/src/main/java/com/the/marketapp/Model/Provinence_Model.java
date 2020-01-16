package com.the.marketapp.Model;

public class Provinence_Model {
    public String getProvinence_ID() {
        return provinence_ID;
    }

    public void setProvinence_ID(String provinence_ID) {
        this.provinence_ID = provinence_ID;
    }

    public String getProvinence_Name() {
        return provinence_Name;
    }

    public void setProvinence_Name(String provinence_Name) {
        this.provinence_Name = provinence_Name;
    }

    String provinence_ID,provinence_Name;

    public Provinence_Model(String provinence_ID,String provinence_Name) {
        this.provinence_ID=provinence_ID;
        this.provinence_Name = provinence_Name;

    }
}
