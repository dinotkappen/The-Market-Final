package com.the.marketapp.Model;

public class Related_Products_Model {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String name;
    private String thumbnail;

    public Related_Products_Model() {
    }

    public Related_Products_Model(String id,String name, String thumbnail) {
       this.id=id;
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
