package com.example.uklon_android.classes;

import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private float totalPrice;
    private Date date;
    private User user;
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
