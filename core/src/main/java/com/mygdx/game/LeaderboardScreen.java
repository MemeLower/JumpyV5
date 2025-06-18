package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.Array;

public class LeaderboardScreen implements Screen {
    private final MainGame game;
    private final OrthographicCamera camera;
    private final ScreenViewport viewport;
    private final Stage stage;
    private final SpriteBatch batch;
    private final Skin skin;
    private final LeaderboardManager leaderboardManager;
    private final Table table;

    public LeaderboardScreen(MainGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new ScreenViewport(camera);
        this.stage = new Stage(viewport);
        this.batch = new SpriteBatch();
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.leaderboardManager = new LeaderboardManager();
        this.table = new Table();
        
        setupUI();
    }

    private void setupUI() {
        table.setFillParent(true);
        table.top().padTop(50);
        
        // Title
        Label titleLabel = new Label("High Scores", skin);
        table.add(titleLabel).colspan(2).padBottom(30);
        table.row();
        
        // Headers
        Label nameLabel = new Label("Player", skin);
        Label scoreLabel = new Label("Score", skin);
        table.add(nameLabel).padRight(50);
        table.add(scoreLabel);
        table.row();
        
        // Scores
        Array<ScoreEntry> scores = leaderboardManager.getScores();
        for (ScoreEntry score : scores) {
            Label playerLabel = new Label(score.getUsername(), skin);
            Label scoreValueLabel = new Label(String.format("%.0f", score.getScore()), skin);
            table.add(playerLabel).padRight(50);
            table.add(scoreValueLabel);
            table.row();
        }
        
        // Back button
        Label backLabel = new Label("Press ESC to go back", skin);
        table.add(backLabel).colspan(2).padTop(50);
        
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.act(delta);
        stage.draw();
        
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        skin.dispose();
    }
} 