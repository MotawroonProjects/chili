package com.chili_driver.models;

import java.io.Serializable;
import java.util.List;

public class MyOrderDataModel implements Serializable {
    private List<OrderModel> data;
    private int status;

    public List<OrderModel> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }
}
