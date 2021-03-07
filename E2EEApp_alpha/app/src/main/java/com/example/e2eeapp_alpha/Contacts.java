package com.example.e2eeapp_alpha;

import java.io.Serializable;

public class Contacts implements Serializable {
    //this is the firebase db attribute format
    //kep the variable names same as the attributes name in DB
    public String name = "", phone = "", status  = "", image = "",  uid = "";

    // Friendly Note: Java does not support default values like C++
    // you need method overloading to define different types of input arguments for the same function


    // if a user makes a call like: Contacts() -> this constructor will set all the variables to null
    public Contacts() {}

    // if user provides only name and phone number
    public Contacts(String name, String phone) {
        this.name = name;
        this.phone = phone;
        this.status = "";
        this.image = "";
        this.uid = "";
    }

    // if user provides all 4 parameters
    public Contacts(String name, String phone, String status, String image, String uid) {

        this.name = name;
        this.phone = phone;
        this.status = status;
        this.image = image;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
}
