package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class LevelManager {
    private static final String PROGRESS_FILE = "level_progress.json";
    private static final int TOTAL_LEVELS = 5;
    private static LevelManager instance;
    private int highestUnlockedLevel;
    private Json json;

    private LevelManager() {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        loadProgress();
    }

    public static LevelManager getInstance() {
        if (instance == null) {
            instance = new LevelManager();
        }
        return instance;
    }

    public void unlockLevel(int level) {
        if (level > highestUnlockedLevel && level <= TOTAL_LEVELS) {
            highestUnlockedLevel = level;
            saveProgress();
        }
    }

    public boolean isLevelUnlocked(int level) {
        return level <= highestUnlockedLevel;
    }

    public int getHighestUnlockedLevel() {
        return highestUnlockedLevel;
    }

    public int getTotalLevels() {
        return TOTAL_LEVELS;
    }

    private void saveProgress() {
        try {
            FileHandle file = Gdx.files.local(PROGRESS_FILE);
            file.writeString(json.toJson(highestUnlockedLevel), false);
        } catch (Exception e) {
            Gdx.app.error("LevelManager", "Error saving progress", e);
        }
    }

    private void loadProgress() {
        try {
            FileHandle file = Gdx.files.local(PROGRESS_FILE);
            if (file.exists()) {
                highestUnlockedLevel = json.fromJson(Integer.class, file);
            } else {
                highestUnlockedLevel = 1; // First level is always unlocked
                saveProgress();
            }
        } catch (Exception e) {
            Gdx.app.error("LevelManager", "Error loading progress", e);
            highestUnlockedLevel = 1;
        }
    }

    public void resetProgress() {
        highestUnlockedLevel = 1;
        saveProgress();
    }
} 