package com.chili_driver.models;

import java.io.Serializable;

public class ProductModel implements Serializable {
    private int id;
    private int order_id;
    private int item_id;
    private int amount;
    private double price;
    public OneItem one_item;

    public int getId() {
        return id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public int getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public OneItem getOne_item() {
        return one_item;
    }

    public static class OneItem implements Serializable {
        private int id;
        private String title;
        private String main_image;
        private String type;
        private String details;
        private String price;
        private String price_out;
        private int market_id;
        private int department_id;
        private String time;

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getMain_image() {
            return main_image;
        }

        public String getType() {
            return type;
        }

        public String getDetails() {
            return details;
        }

        public String getPrice() {
            return price;
        }

        public String getPrice_out() {
            return price_out;
        }

        public int getMarket_id() {
            return market_id;
        }

        public int getDepartment_id() {
            return department_id;
        }

        public String getTime() {
            return time;
        }
    }

}
