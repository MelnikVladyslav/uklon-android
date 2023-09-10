package com.example.uklon_android.classes;

import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private float totalPrice;
    private Date date = new Date();
    private String userId;
    private String type;
    private String startPoint;
    private String endPoint;
    private int rating = 5;
    private List<Transport> transportList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrice() {
        return totalPrice;
    }

    public void setPrice(float price) {
        this.totalPrice = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getUser() {
        return userId;
    }

    public void setUser(String User) {
        this.userId = userId;
    }
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public List<Transport> getTransports() {
        return transportList;
    }

    public void setTransports(List<Transport> transportList) {
        this.transportList = transportList;
    }
}
