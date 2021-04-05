package com.my.AndroidPatientTracker.models;

import com.google.firebase.Timestamp;

public class RemediesModel {

    String content,title,image;
    Timestamp updated_at;

    public RemediesModel(String content, String title, String image, Timestamp updated_at) {
        this.content = content;
        this.title = title;
        this.image = image;
        this.updated_at = updated_at;
    }

    public RemediesModel() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}
