package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainGame extends Game {
    public SpriteBatch batch;
    private Json json = new Json();
    private List<ScoreEntry> highScores = new ArrayList<>();

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new MainMenuScreen(this));
    }

    public void startEndlessModeWithUsername(String username) {
        Array<Platform> platforms = new Array<>();
        platforms.add(new Platform(100, 100, 300, 20));
        platforms.add(new Platform(400, 250, 200, 20));

        setScreen(new EndlessGameScreen(this, platforms, username));
    }

    public void saveHighScore(ScoreEntry entry) {
        loadHighScores(); // Reload latest scores
        highScores.add(entry);
        highScores.sort(null); // Sort descending
        Gdx.files.local("highscores.json").writeString(json.toJson(highScores), false);
    }

    public List<ScoreEntry> loadHighScores() {
        highScores.clear();
        FileHandle file = Gdx.files.local("highscores.json");
        if (file.exists()) {
            highScores.addAll(Arrays.asList(json.fromJson(ScoreEntry[].class, file.readString())));
        }
        return highScores;
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }
}
