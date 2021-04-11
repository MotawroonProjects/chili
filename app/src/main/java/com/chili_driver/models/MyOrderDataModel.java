package com.chili_driver.models;

import java.io.Serializable;
import java.util.List;

public class MyOrderDataModel implements Serializable {
    private List<OrderModel> orders;
    public List<OrderModel> getData() {
        return orders;
    }
}
