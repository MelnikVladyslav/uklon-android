package com.example.uklon_android.DTOs;

public class PhoneNumberVerificationDto {
    private String phoneNumber = "";
    private String email = "";
    private String Password = "";

    public String getPhoneNumber(){return phoneNumber;}
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getEmail(){return email;}
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword(){return Password;}
    public void setPassword(String Password) {
        this.Password = Password;
    }
}
