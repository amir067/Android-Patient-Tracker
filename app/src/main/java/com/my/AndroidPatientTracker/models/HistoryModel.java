package com.my.AndroidPatientTracker.models;

import com.google.firebase.Timestamp;

public class HistoryModel {

    String score;
    Timestamp updated_at;
    String user_id;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public HistoryModel() {
    }

    public HistoryModel(String score, Timestamp updated_at, String user_id) {
        this.score = score;
        this.updated_at = updated_at;
        this.user_id = user_id;
    }
}
