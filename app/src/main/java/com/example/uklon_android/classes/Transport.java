package com.example.uklon_android.classes;

public class Transport {
    private int id;
    private String model = "";
    public int type = 1;
    public String description = "";
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



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImage() {
        return urlImg;
    }

    public void setImage(String image) {
        this.urlImg = image;
    }
}
