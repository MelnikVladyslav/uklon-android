package com.example.uklon_android.classes;

import java.util.List;

public class Types {
    public int id;
    public String name;
    public float price;
    public List<Transport> transports;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public List<Transport> getTransports() {
        return transports;
    }

    public void setTransports(List<Transport> transports) {
        this.transports = transports;
    }
}
