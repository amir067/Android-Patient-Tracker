package com.my.AndroidPatientTracker.models;

import com.google.firebase.Timestamp;

public class DoctorsModel {

    String id;
    Timestamp created_at;
    String name, email, phone, address, gender, experence, category;
    int fee;
    int rating;


    public DoctorsModel() {
    }

    public DoctorsModel(String id, String d_name, String email, String phone, String address, String gender, String experence, int fee, Timestamp created_at,int rating, String category) {
        this.id = id;
        this.name = d_name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.experence = experence;
        this.fee = fee;
        this.rating = rating;
        this.created_at = created_at;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExperence() {
        return experence;
    }
    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
    public void setExperence(String experence) {
        this.experence = experence;
    }

}


