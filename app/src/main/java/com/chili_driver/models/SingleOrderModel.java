package com.chili_driver.models;

import java.io.Serializable;

public class SingleOrderModel implements Serializable {
    private OrderModel data;
    private int status;

    public OrderModel getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }
}
