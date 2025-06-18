package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameOverScreen implements Screen {
    private final MainGame game;
    private final OrthographicCamera camera;
    private final ScreenViewport viewport;
    private final Stage stage;
    private final SpriteBatch batch;
    private final Skin skin;
    private final Table table;
    private final float score;
    private final LeaderboardManager leaderboardManager;
    private TextField nameField;

    public GameOverScreen(MainGame game, float score) {
        this.game = game;
        this.score = score;
        this.camera = new OrthographicCamera();
        this.viewport = new ScreenViewport(camera);
        this.stage = new Stage(viewport);
        this.batch = new SpriteBatch();
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.table = new Table();
        this.leaderboardManager = new LeaderboardManager();
        
        setupUI();
    }

    private void setupUI() {
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        // Game Over text
        Label gameOverLabel = new Label("Game Over", skin);
        table.add(gameOverLabel).colspan(2).padBottom(20).row();

        // Score display
        Label scoreLabel = new Label(String.format("Score: %.0f", score), skin);
        table.add(scoreLabel).colspan(2).padBottom(20).row();

        // Name input
        Label nameLabel = new Label("Enter your name:", skin);
        table.add(nameLabel).padRight(10);
        nameField = new TextField("", skin);
        table.add(nameField).width(200).row();

        // Submit button
        TextButton submitButton = new TextButton("Submit Score", skin);
        submitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String name = nameField.getText().trim();
                if (!name.isEmpty()) {
                    leaderboardManager.addScore(name, score);
                    game.setScreen(new MainMenuScreen(game));
                }
            }
        });
        table.add(submitButton).colspan(2).padTop(20).row();

        // Skip button
        TextButton skipButton = new TextButton("Skip", skin);
        skipButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        table.add(skipButton).colspan(2).padTop(10);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.act(delta);
        stage.draw();
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