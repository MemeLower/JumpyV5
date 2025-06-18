package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameOverScreen implements Screen {
    final MainGame game;
    private Stage stage;
    private Skin skin;
    private float finalScore;
    private LeaderboardManager leaderboardManager;
    private TextField usernameField;
    private Label scoreLabel;
    private Table leaderboardTable;

    public GameOverScreen(final MainGame game, float score) {
        this.game = game;
        this.finalScore = score;
        this.leaderboardManager = new LeaderboardManager();
        
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.stage = new Stage(new ScreenViewport());
        
        createUI();
    }

    private void createUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // Score display
        scoreLabel = new Label("Score: " + String.format("%.0f", finalScore), skin);
        mainTable.add(scoreLabel).padBottom(20).row();

        // Username input
        usernameField = new TextField("", skin);
        usernameField.setMessageText("Enter your username");
        mainTable.add(usernameField).width(200).padBottom(20).row();

        // Save score button
        TextButton saveButton = new TextButton("Save Score", skin);
        mainTable.add(saveButton).width(200).padBottom(20).row();

        // Retry button
        TextButton retryButton = new TextButton("Retry", skin);
        mainTable.add(retryButton).width(200).padBottom(20).row();

        // Leaderboard
        Label leaderboardLabel = new Label("Leaderboard", skin);
        mainTable.add(leaderboardLabel).padBottom(10).row();

        leaderboardTable = new Table(skin);
        mainTable.add(leaderboardTable).row();

        // Back to menu button
        TextButton menuButton = new TextButton("Back to Menu", skin);
        mainTable.add(menuButton).width(200).padTop(20).row();

        // Add listeners
        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String username = usernameField.getText().trim();
                if (!username.isEmpty()) {
                    leaderboardManager.addScore(username, finalScore);
                    updateLeaderboard();
                    saveButton.setDisabled(true);
                    usernameField.setDisabled(true);
                }
            }
        });

        retryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Create new initial platforms for endless mode
                Array<Platform> initialPlatforms = new Array<>();
                initialPlatforms.add(new Platform(100, 100, 300, 20));
                initialPlatforms.add(new Platform(400, 250, 200, 20));
                game.setScreen(new EndlessGameScreen(game, initialPlatforms));
            }
        });

        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        updateLeaderboard();
        Gdx.input.setInputProcessor(stage);
    }

    private void updateLeaderboard() {
        leaderboardTable.clear();
        Array<ScoreEntry> scores = leaderboardManager.getScores();
        for (ScoreEntry score : scores) {
            leaderboardTable.add(new Label(score.toString(), skin)).row();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
} 