package com.my.AndroidPatientTracker.models;


import com.google.firebase.Timestamp;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserModel {

    public  String name;
    public  String email;
    public  String phone;
    public  String gender;
    public  String password;
    public  String age;
    public  String id;

    public  String userBio;
    public  String profileImageUrl;
    public  String address;

    public long updated_at,created_at;

    public UserModel() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String Password) {
        this.password = Password;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String Gender) {
        this.gender = Gender;
    }

    public String getProfileUrl() {
        return profileImageUrl;
    }

    public void setProfileUrl(String ProfileUrl) {
        this.profileImageUrl = ProfileUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}