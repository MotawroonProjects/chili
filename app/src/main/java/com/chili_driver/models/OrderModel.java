package com.chili_driver.models;

import java.io.Serializable;
import java.util.List;

public class OrderModel implements Serializable {

  private int id;
  private String order_date;
  private int order_time_id;
  private String order_place;
  private int client_id;
  private int market_id;
  private int employee_id;
  private String driver_id;
  private double total;
  private String copone_id;
  private String status;
  private String order_type;
  private String client_address;
  private double longitude;
  private double latitude;
  private String payment_methoud;
  private int task;
  private double market_distance;
  private double client_distance;
  private String driver_status;
  private UserModel user;
  private MarketModel market;
  private List<ProductModel> details;
  private TimeModel time;
  public int getId() {
    return id;
  }

  public String getOrder_date() {
    return order_date;
  }

  public int getOrder_time_id() {
    return order_time_id;
  }

  public String getOrder_place() {
    return order_place;
  }

  public int getClient_id() {
    return client_id;
  }

  public int getMarket_id() {
    return market_id;
  }

  public int getEmployee_id() {
    return employee_id;
  }

  public String getDriver_id() {
    return driver_id;
  }

  public double getTotal() {
    return total;
  }

  public String getCopone_id() {
    return copone_id;
  }

  public String getStatus() {
    return status;
  }

  public String getOrder_type() {
    return order_type;
  }

  public String getClient_address() {
    return client_address;
  }

  public double getLongitude() {
    return longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public String getDriver_status() {
    return driver_status;
  }

  public String getPayment_methoud() {
    return payment_methoud;
  }

  public int getTask() {
    return task;
  }

  public double getMarket_distance() {
    return market_distance;
  }

  public double getClient_distance() {
    return client_distance;
  }

  public TimeModel getTime() {
    return time;
  }

  public UserModel getUser() {
    return user;
  }

  public MarketModel getMarket() {
    return market;
  }

  public List<ProductModel> getDetails() {
    return details;
  }

  public static class TimeModel implements Serializable{
    private int id;
    private int market_id;
    private String time;

    public int getId() {
      return id;
    }

    public int getMarket_id() {
      return market_id;
    }

    public String getTime() {
      return time;
    }
  }
}
