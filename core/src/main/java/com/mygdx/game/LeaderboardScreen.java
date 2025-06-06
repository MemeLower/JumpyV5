package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.List;

public class LeaderboardScreen implements Screen {
    final MainGame game;
    Stage stage;
    Skin skin;

    public LeaderboardScreen(MainGame game) {
        this.game = game;
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.stage = new Stage(new ScreenViewport());

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label title = new Label("Top Scores", skin, "big");
        table.add(title).padBottom(30).row();

        List<ScoreEntry> scores = game.loadHighScores();

        for (int i = 0; i < Math.min(scores.size(), 10); i++) {
            ScoreEntry entry = scores.get(i);
            Label scoreLabel = new Label((i + 1) + ". " + entry.username + " - " + (int) entry.score, skin);
            table.add(scoreLabel).padBottom(10).row();
        }

        TextButton backButton = new TextButton("Back", skin);
        table.add(backButton);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(delta, 0.016f));
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
