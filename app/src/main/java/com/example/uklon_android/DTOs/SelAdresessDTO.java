package com.example.uklon_android.DTOs;

public class SelAdresessDTO {
    String NameAdr;
    String NameHome;
    String NameJob;
    String UserId;

    public String getNameAdr() {
        return NameAdr;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getNameHome() {
        return NameHome;
    }

    public void setNameAdr(String nameAdr) {
        NameAdr = nameAdr;
    }

    public String getNameJob() {
        return NameJob;
    }

    public void setNameHome(String nameHome) {
        NameHome = nameHome;
    }

    public void setNameJob(String nameJob) {
        NameJob = nameJob;
    }
}
