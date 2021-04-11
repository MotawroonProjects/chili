package com.chili_driver.models;

import java.io.Serializable;

public class SingleOrderModel implements Serializable {
    private OrderModel order;
    private int status;

    public OrderModel getData() {
        return order;
    }

}
