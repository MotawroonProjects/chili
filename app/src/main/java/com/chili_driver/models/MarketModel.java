package com.chili_driver.models;

import java.io.Serializable;

public class MarketModel implements Serializable {
    private int id;
    private String name;
    private String email;
    private String password;
    private String remember_token;
    private String featured;
    private String feature_type;
    private String logo;
    private String panner;
    private String address;
    private double lat;
    private String phone_code;
    private String phone;
    private String rate;
    private String status;
    private int city_id;
    private int neighborhood_id;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRemember_token() {
        return remember_token;
    }

    public String getFeatured() {
        return featured;
    }

    public String getFeature_type() {
        return feature_type;
    }

    public String getLogo() {
        return logo;
    }

    public String getPanner() {
        return panner;
    }

    public String getAddress() {
        return address;
    }

    public double getLat() {
        return lat;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public String getRate() {
        return rate;
    }

    public String getStatus() {
        return status;
    }

    public int getCity_id() {
        return city_id;
    }

    public int getNeighborhood_id() {
        return neighborhood_id;
    }
}


