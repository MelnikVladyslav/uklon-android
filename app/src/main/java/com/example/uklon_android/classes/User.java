package com.example.uklon_android.classes;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String roleId;
    private String token;
    private String url;
    private List<Order> orderList;
    private List<Card> cardList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return roleId;
    }

    public void setRole(String roleId) {
        this.roleId = roleId;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Order> getOrders() {
        return orderList;
    }

    public void setOrders(List<Order> orderList) {
        this.orderList = orderList;
    }

    public List<Card> getCards() {
        return cardList;
    }

    public void setCards(List<Card> cardList) {
        this.cardList = cardList;
    }
}
