package com.mygdx.game;

public class ScoreEntry {
    private String username;
    private float score;

    public ScoreEntry() {
        // Required for JSON serialization
    }

    public ScoreEntry(String username, float score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return username + ": " + String.format("%.0f", score);
    }
} 