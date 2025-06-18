package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.Array;

public class LevelSelectScreen implements Screen {
    final MainGame game;
    Stage stage;
    Skin skin;

    public LevelSelectScreen(final MainGame game) {
        this.game = game;
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.stage = new Stage(new ScreenViewport());

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titleLabel = new Label("Select Level", skin);
        TextButton level1Button = new TextButton("Level 1", skin);
        TextButton level2Button = new TextButton("Level 2", skin);
        TextButton endlessModeButton = new TextButton("Endless Mode", skin);
        TextButton backButton = new TextButton("Back", skin);

        table.add(titleLabel).padBottom(50).row();
        table.add(level1Button).fillX().uniformX().pad(20).row();
        table.add(level2Button).fillX().uniformX().pad(20).row();
        table.add(endlessModeButton).fillX().uniformX().pad(20).row();
        table.add(backButton).fillX().uniformX().pad(20).row();

        level1Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(createLevel1());
            }
        });

        level2Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(createLevel2());
            }
        });

        endlessModeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(createEndlessMode());
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        Gdx.input.setInputProcessor(stage);
    }

    private GameScreen createLevel1() {
        Array<Platform> platforms = new Array<>();
        platforms.add(new Platform(100, 100, 300, 20));
        platforms.add(new Platform(450, 150, 200, 20));
        platforms.add(new Platform(700, 200, 150, 20));
        platforms.add(new Platform(900, 250, 200, 20));
        platforms.add(new Platform(1150, 200, 150, 20));
        platforms.add(new Platform(1350, 150, 200, 20));
        platforms.add(new Platform(1600, 100, 300, 20));

        Array<Obstacle> obstacles = new Array<>();
        obstacles.add(new Obstacle(200, 100, 30, 30));
        obstacles.add(new Obstacle(500, 150, 30, 30));
        obstacles.add(new Obstacle(800, 200, 30, 30));
        obstacles.add(new Obstacle(1200, 200, 30, 30));

        Goal goal = new Goal(1800, 100, 40, 60);

        return new GameScreen(game, platforms, obstacles, goal);
    }

    private GameScreen createLevel2() {
        Array<Platform> platforms = new Array<>();
        platforms.add(new Platform(100, 100, 250, 20));
        platforms.add(new Platform(400, 150, 200, 20));
        platforms.add(new Platform(650, 200, 150, 20));
        platforms.add(new Platform(850, 150, 200, 20));
        platforms.add(new Platform(1100, 200, 150, 20));
        platforms.add(new Platform(1300, 150, 200, 20));
        platforms.add(new Platform(1550, 100, 250, 20));
        platforms.add(new Platform(1850, 150, 200, 20));

        Array<Obstacle> obstacles = new Array<>();
        obstacles.add(new Obstacle(450, 150, 30, 30));
        obstacles.add(new Obstacle(700, 200, 30, 30));
        obstacles.add(new Obstacle(900, 150, 30, 30));
        obstacles.add(new Obstacle(1150, 200, 30, 30));
        obstacles.add(new Obstacle(1350, 150, 30, 30));
        obstacles.add(new Obstacle(1600, 100, 30, 30));

        Goal goal = new Goal(2000, 150, 40, 60);

        return new GameScreen(game, platforms, obstacles, goal);
    }

    private GameScreen createEndlessMode() {
        Array<Platform> platforms = new Array<>();
        platforms.add(new Platform(100, 100, 300, 20));
        platforms.add(new Platform(400, 250, 200, 20));

        return new EndlessGameScreen(game, platforms);
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
