package com.chili_driver.models;

import java.io.Serializable;

public class OrderCountModel implements Serializable{
    private int status;
    private Data data;

    public int getStatus() {
        return status;
    }

    public Data getData() {
        return data;
    }

    public class Data implements Serializable{
        private int total_orders_count;
        private int current_orders_count;
        private int finished_orders_count;

        public int getTotal_orders_count() {
            return total_orders_count;
        }

        public int getCurrent_orders_count() {
            return current_orders_count;
        }

        public int getFinished_orders_count() {
            return finished_orders_count;
        }
    }
}
