package com.example.uklon_android.DTOs;

public class ChangeDTO {
    String userId;
    String password;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUserId() {
        return userId;
    }
}
