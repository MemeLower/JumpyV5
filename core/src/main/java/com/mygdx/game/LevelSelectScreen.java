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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.Array;

public class LevelSelectScreen implements Screen {
    private final MainGame game;
    private final OrthographicCamera camera;
    private final ScreenViewport viewport;
    private final Stage stage;
    private final SpriteBatch batch;
    private final Skin skin;
    private final LevelManager levelManager;
    private final Table table;

    public LevelSelectScreen(MainGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new ScreenViewport(camera);
        this.stage = new Stage(viewport);
        this.batch = new SpriteBatch();
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.levelManager = LevelManager.getInstance();
        this.table = new Table();

        setupUI();
    }

    private void setupUI() {
        table.setFillParent(true);
        table.center();
        stage.addActor(table);

        // Title
        Label titleLabel = new Label("Select Level", skin);
        table.add(titleLabel).colspan(2).padBottom(50).row();

        // Create level buttons
        for (int i = 1; i <= levelManager.getTotalLevels(); i++) {
            final int level = i;
            TextButton levelButton = new TextButton("Level " + level, skin);
            
            if (!levelManager.isLevelUnlocked(level)) {
                levelButton.setDisabled(true);
                levelButton.setText("Level " + level + " (Locked)");
            }

            levelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                    if (levelManager.isLevelUnlocked(level)) {
                        game.setScreen(createLevel(level));
                    }
            }
        });

            table.add(levelButton).width(200).height(50).padBottom(10).row();
        }

        // Add Endless Mode button
        TextButton endlessButton = new TextButton("Endless Mode", skin);
        endlessButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new EndlessGameScreen(game));
            }
        });
        table.add(endlessButton).width(200).height(50).padBottom(10).row();

        // Add back button
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        table.add(backButton).width(200).height(50);

        Gdx.input.setInputProcessor(stage);
    }

    private GameScreen createLevel(int level) {
        switch (level) {
            case 1:
                return createLevel1();
            case 2:
                return createLevel2();
            case 3:
                return createLevel3();
            case 4:
                return createLevel4();
            case 5:
                return createLevel5();
            default:
                return createLevel1();
        }
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
        obstacles.add(new Obstacle(200, 120, 30, 30));
        obstacles.add(new Obstacle(500, 170, 30, 30));
        obstacles.add(new Obstacle(800, 220, 30, 30));
        obstacles.add(new Obstacle(1200, 220, 30, 30));

        Goal goal = new Goal(1800, 100, 40, 60);
        return new GameScreen(game, platforms, obstacles, goal, 1);
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
        obstacles.add(new Obstacle(450, 170, 30, 30));
        obstacles.add(new Obstacle(700, 220, 30, 30));
        obstacles.add(new Obstacle(900, 170, 30, 30));
        obstacles.add(new Obstacle(1150, 220, 30, 30));
        obstacles.add(new Obstacle(1350, 170, 30, 30));
        obstacles.add(new Obstacle(1600, 120, 30, 30));

        Goal goal = new Goal(2000, 150, 40, 60);
        return new GameScreen(game, platforms, obstacles, goal, 2);
    }

    private GameScreen createLevel3() {
        Array<Platform> platforms = new Array<>();
        platforms.add(new Platform(100, 100, 200, 20));
        platforms.add(new Platform(350, 150, 150, 20));
        platforms.add(new Platform(550, 200, 150, 20));
        platforms.add(new Platform(750, 150, 150, 20));
        platforms.add(new Platform(950, 200, 150, 20));
        platforms.add(new Platform(1150, 150, 150, 20));
        platforms.add(new Platform(1350, 200, 150, 20));
        platforms.add(new Platform(1550, 150, 150, 20));
        platforms.add(new Platform(1750, 100, 200, 20));

        Array<Obstacle> obstacles = new Array<>();
        obstacles.add(new Obstacle(400, 170, 30, 30));
        obstacles.add(new Obstacle(600, 220, 30, 30));
        obstacles.add(new Obstacle(800, 170, 30, 30));
        obstacles.add(new Obstacle(1000, 220, 30, 30));
        obstacles.add(new Obstacle(1200, 170, 30, 30));
        obstacles.add(new Obstacle(1400, 220, 30, 30));
        obstacles.add(new Obstacle(1600, 170, 30, 30));

        Goal goal = new Goal(1900, 100, 40, 60);
        return new GameScreen(game, platforms, obstacles, goal, 3);
    }

    private GameScreen createLevel4() {
        Array<Platform> platforms = new Array<>();
        platforms.add(new Platform(100, 100, 150, 20));
        platforms.add(new Platform(300, 150, 150, 20));
        platforms.add(new Platform(500, 200, 150, 20));
        platforms.add(new Platform(700, 150, 150, 20));
        platforms.add(new Platform(900, 200, 150, 20));
        platforms.add(new Platform(1100, 150, 150, 20));
        platforms.add(new Platform(1300, 200, 150, 20));
        platforms.add(new Platform(1500, 150, 150, 20));
        platforms.add(new Platform(1700, 200, 150, 20));
        platforms.add(new Platform(1900, 150, 150, 20));

        Array<Obstacle> obstacles = new Array<>();
        obstacles.add(new Obstacle(350, 170, 30, 30));
        obstacles.add(new Obstacle(550, 220, 30, 30));
        obstacles.add(new Obstacle(750, 170, 30, 30));
        obstacles.add(new Obstacle(950, 220, 30, 30));
        obstacles.add(new Obstacle(1150, 170, 30, 30));
        obstacles.add(new Obstacle(1350, 220, 30, 30));
        obstacles.add(new Obstacle(1550, 170, 30, 30));
        obstacles.add(new Obstacle(1750, 220, 30, 30));

        Goal goal = new Goal(2050, 150, 40, 60);
        return new GameScreen(game, platforms, obstacles, goal, 4);
    }

    private GameScreen createLevel5() {
        Array<Platform> platforms = new Array<>();
        platforms.add(new Platform(100, 100, 120, 20));
        platforms.add(new Platform(270, 150, 120, 20));
        platforms.add(new Platform(440, 200, 120, 20));
        platforms.add(new Platform(610, 150, 120, 20));
        platforms.add(new Platform(780, 200, 120, 20));
        platforms.add(new Platform(950, 150, 120, 20));
        platforms.add(new Platform(1120, 200, 120, 20));
        platforms.add(new Platform(1290, 150, 120, 20));
        platforms.add(new Platform(1460, 200, 120, 20));
        platforms.add(new Platform(1630, 150, 120, 20));
        platforms.add(new Platform(1800, 200, 120, 20));
        platforms.add(new Platform(1970, 150, 120, 20));

        Array<Obstacle> obstacles = new Array<>();
        obstacles.add(new Obstacle(320, 170, 30, 30));
        obstacles.add(new Obstacle(490, 220, 30, 30));
        obstacles.add(new Obstacle(660, 170, 30, 30));
        obstacles.add(new Obstacle(830, 220, 30, 30));
        obstacles.add(new Obstacle(1000, 170, 30, 30));
        obstacles.add(new Obstacle(1170, 220, 30, 30));
        obstacles.add(new Obstacle(1340, 170, 30, 30));
        obstacles.add(new Obstacle(1510, 220, 30, 30));
        obstacles.add(new Obstacle(1680, 170, 30, 30));
        obstacles.add(new Obstacle(1850, 220, 30, 30));

        Goal goal = new Goal(2100, 150, 40, 60);
        return new GameScreen(game, platforms, obstacles, goal, 5);
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
