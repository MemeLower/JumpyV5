package com.mygdx.game;

public class ScoreEntry implements Comparable<ScoreEntry> {
    public String username;
    public float score;

    public ScoreEntry(String username, float score) {
        this.username = username;
        this.score = score;
    }

    @Override
    public int compareTo(ScoreEntry other) {
        return Float.compare(other.score, this.score);
    }
}
