package com.the.marketapp.Model;

public class Adrz_Book_Model {

    public String getAdrz_ID() {
        return adrz_ID;
    }

    public void setAdrz_ID(String adrz_ID) {
        this.adrz_ID = adrz_ID;
    }

    String adrz_ID;

    public String getAlias() {
        return alias;
    }


    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }


    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIs_default() {
        return is_default;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    String alias;
    String name;
    String address_1;
    String address_2;

    public String getProvinenceName() {
        return provinenceName;
    }

    public void setProvinenceName(String provinenceName) {
        this.provinenceName = provinenceName;
    }

    String provinenceName;
    String country_id;
    String customer_id;
    String phone;
    String is_default;
    String latitude;
    String longitude;

    public Adrz_Book_Model(String adrz_ID, String alias, String name, String address_1, String address_2,String provinenceName, String country_id,
                           String customer_id, String phone, String is_default, String latitude, String longitude) {
        this.adrz_ID = adrz_ID;
        this.alias = alias;
        this.name = name;
        this.address_1 = address_1;
        this.address_2 = address_2;
        this.provinenceName=provinenceName;
        this.country_id = country_id;
        this.customer_id = customer_id;
        this.phone = phone;
        this.is_default = is_default;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
