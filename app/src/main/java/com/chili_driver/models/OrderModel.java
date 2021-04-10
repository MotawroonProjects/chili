package com.chili_driver.models;

import java.io.Serializable;

public class OrderModel implements Serializable {
  private  int id;
  private  int restaurant_id;
  private  int client_id;
  private  int order_num;
  private  String barcode_code;
  private  String barcode_image;
  private  String order_status;
  private  long start_time_at;
  private  String end_time_at;
  private  String created_at;
  private  String updated_at;
  private  UserModel.User client_fk;

  public int getId() {
    return id;
  }

  public int getRestaurant_id() {
    return restaurant_id;
  }

  public int getClient_id() {
    return client_id;
  }

  public int getOrder_num() {
    return order_num;
  }

  public String getBarcode_code() {
    return barcode_code;
  }

  public String getBarcode_image() {
    return barcode_image;
  }

  public String getOrder_status() {
    return order_status;
  }

  public long getStart_time_at() {
    return start_time_at;
  }

  public String getEnd_time_at() {
    return end_time_at;
  }

  public String getCreated_at() {
    return created_at;
  }

  public String getUpdated_at() {
    return updated_at;
  }

  public UserModel.User getClient_fk() {
    return client_fk;
  }
}
