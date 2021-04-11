package com.chili_driver.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    private int id;
    private String image;
    private String name;
    private String email;
    private String type;
    private String phone_code;
    private String phone;
    private String address;
    private double lat;
    private String is_login;
    private int balance;
    private double longitude;

    private String firebaseToken;



    public int getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public double getLat() {
        return lat;
    }

    public String getIs_login() {
        return is_login;
    }

    public int getBalance() {
        return balance;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }
}
