package com.chili_driver.models;

import java.io.Serializable;

public class RestaurantSettingModel implements Serializable{
    private int status;
    private Data data;

    public int getStatus() {
        return status;
    }

    public Data getData() {
        return data;
    }

    public class Data implements Serializable{
        private int id;
        private int restaurant_id;
        private int order_time_preparing;
        private int order_first_num;
        private int order_current_num;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public int getRestaurant_id() {
            return restaurant_id;
        }

        public int getOrder_time_preparing() {
            return order_time_preparing;
        }

        public int getOrder_first_num() {
            return order_first_num;
        }

        public int getOrder_current_num() {
            return order_current_num;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
}
