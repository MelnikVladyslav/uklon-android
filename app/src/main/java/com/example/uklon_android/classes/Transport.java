package com.example.uklon_android.classes;

public class Transport {
    private int id;
    private String model;
    public Types type;
    public float price;
    public String description;
    public String urlImg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Types getType() {
        return type;
    }

    public void setType(Types type) {
        this.type = type;
    }

    public String getImage() {
        return urlImg;
    }

    public void setImage(String image) {
        this.urlImg = image;
    }
}
