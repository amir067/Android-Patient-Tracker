package com.my.AndroidPatientTracker.models;


import com.google.firebase.Timestamp;

public class UserModel {

    public String age,email,gender,id,imageURL,password,phone,username;
    public Timestamp updated_at,created_at;

    public UserModel(){
    }

    public UserModel(String age, String email, String gender, String id, String imageURL, String password, String phone, String username, Timestamp updated_at, Timestamp created_at) {
        this.age = age;
        this.email = email;
        this.gender = gender;
        this.id = id;
        this.imageURL = imageURL;
        this.password = password;
        this.phone = phone;
        this.username = username;
        this.updated_at = updated_at;
        this.created_at = created_at;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}