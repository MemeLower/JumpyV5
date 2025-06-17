package com.mygdx.game;

public class ScoreEntry {
    private String username;
    private float score;

    public ScoreEntry(String username, float score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public float getScore() {
        return score;
    }

    @Override
    public String toString() {
        return username + ": " + String.format("%.0f", score);
    }
} 