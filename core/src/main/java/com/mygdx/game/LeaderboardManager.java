package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class LeaderboardManager {
    private static final String SCORES_FILE = "highscores.json";
    private static final int MAX_SCORES = 10;
    private Array<ScoreEntry> scores;
    private Json json;

    public LeaderboardManager() {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        scores = new Array<>();
        loadScores();
    }

    public void addScore(String username, float score) {
        // Check if username already exists
        ScoreEntry existingEntry = null;
        for (ScoreEntry entry : scores) {
            if (entry.getUsername().equals(username)) {
                existingEntry = entry;
                break;
            }
        }

        if (existingEntry != null) {
            // Update existing score if new score is higher
            if (score > existingEntry.getScore()) {
                existingEntry.setScore(score);
            }
        } else {
            // Add new score entry
            scores.add(new ScoreEntry(username, score));
        }

        // Sort scores
        scores.sort((a, b) -> Float.compare(b.getScore(), a.getScore()));
        
        // Keep only top MAX_SCORES
        if (scores.size > MAX_SCORES) {
            scores.truncate(MAX_SCORES);
        }
        
        saveScores();
    }

    public Array<ScoreEntry> getScores() {
        return scores;
    }

    public Array<String> getUniqueUsernames() {
        Array<String> usernames = new Array<>();
        for (ScoreEntry entry : scores) {
            if (!usernames.contains(entry.getUsername(), false)) {
                usernames.add(entry.getUsername());
            }
        }
        return usernames;
    }

    private void saveScores() {
        FileHandle file = Gdx.files.local(SCORES_FILE);
        file.writeString(json.toJson(scores), false);
    }

    private void loadScores() {
        FileHandle file = Gdx.files.local(SCORES_FILE);
        if (file.exists()) {
            try {
                scores = json.fromJson(Array.class, ScoreEntry.class, file);
            } catch (Exception e) {
                Gdx.app.error("LeaderboardManager", "Error loading scores", e);
                scores = new Array<>();
            }
        }
    }
} 