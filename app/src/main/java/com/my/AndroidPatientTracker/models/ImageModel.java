package com.my.AndroidPatientTracker.models;

public class ImageModel {

    String image;
    int scores;

    public ImageModel(String image, int scores) {
        this.image = image;
        this.scores = scores;
    }

    public ImageModel() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }
}
