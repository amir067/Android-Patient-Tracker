package com.my.AndroidPatientTracker.models;

public class StatementModel {

    String question;
    int scores;

    public StatementModel() {
    }

    public StatementModel(String question, int scores) {
        this.question = question;
        this.scores = scores;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }
}
