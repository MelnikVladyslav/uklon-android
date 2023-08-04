package com.example.uklon_android.classes;

import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private float totalPrice;
    private Date date;
    private User user;
    public String type;
    public String startPoint;
    public String endPoint;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Transport> getTransports() {
        return transportList;
    }

    public void setTransports(List<Transport> transportList) {
        this.transportList = transportList;
    }
}
