package com.my.AndroidPatientTracker.models;


import com.google.firebase.Timestamp;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class UserModel implements Serializable {

    public  String id;
    public  String name;
    public  String email;
    public  String phone;
    public  String gender;
    public  String password;
    public  String age;
    private String RoomID;
    private String RoomName;
    private String userType;
    public  String userBio;
    public  String profileImageUrl;
    public  String address;

    //For All users
    private boolean userApproved;
    private boolean isAdmin;

    public long updated_at,created_at;

    public UserModel() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserModel(String id, String name, String email, String phone, String gender,
                     String password, String age, String userType, String userBio, String profileImageUrl,
                     String address, boolean userApproved, boolean isAdmin, long updated_at, long created_at) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.password = password;
        this.age = age;
        this.userType = userType;
        this.userBio = userBio;
        this.profileImageUrl = profileImageUrl;
        this.address = address;
        this.userApproved = userApproved;
        this.isAdmin = isAdmin;
        this.updated_at = updated_at;
        this.created_at = created_at;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isUserApproved() {
        return userApproved;
    }

    public void setUserApproved(boolean userApproved) {
        this.userApproved = userApproved;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getRoomID() {
        return RoomID;
    }

    public void setRoomID(String roomID) {
        RoomID = roomID;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }
}