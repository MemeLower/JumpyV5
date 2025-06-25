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

    /**
     * Erstellt das erste Level mit Plattformen, Hindernissen und Ziel
     * @return GameScreen mit dem konfigurierten Level
     */
    private GameScreen createLevel1() {
        Array<Platform> platforms = new Array<>();
        platforms.add(new Platform(40, 60, 220, 24)); // Spawn
        platforms.add(new Platform(270, 90, 120, 20));
        platforms.add(new Platform(420, 130, 100, 20));
        platforms.add(new Platform(570, 180, 80, 18));
        platforms.add(new Platform(700, 140, 120, 20));
        platforms.add(new Platform(860, 110, 100, 20));
        // Goal platform
        float goalPlatX = 1000, goalPlatY = 60, goalPlatW = 180, goalPlatH = 24;
        platforms.add(new Platform(goalPlatX, goalPlatY, goalPlatW, goalPlatH));
        Array<Obstacle> obstacles = new Array<>();
        obstacles.add(new Obstacle(320, 110, 30, 30));
        obstacles.add(new Obstacle(600, 200, 30, 30));
        float goalW = 40, goalH = 60;
        Goal goal = new Goal(goalPlatX + goalPlatW/2 - goalW/2, goalPlatY + goalPlatH, goalW, goalH);
        return new GameScreen(game, platforms, obstacles, goal, 1);
    }

    private GameScreen createLevel2() {
        Array<Platform> platforms = new Array<>();
        platforms.add(new Platform(40, 60, 220, 24));
        platforms.add(new Platform(300, 100, 100, 20));
        platforms.add(new Platform(420, 160, 80, 18));
        platforms.add(new Platform(540, 210, 60, 16));
        platforms.add(new Platform(650, 170, 120, 20));
        platforms.add(new Platform(800, 120, 100, 20));
        platforms.add(new Platform(950, 90, 180, 22));
        platforms.add(new Platform(1200, 130, 120, 20));
        // Goal platform
        float goalPlatX = 1350, goalPlatY = 60, goalPlatW = 200, goalPlatH = 24;
        platforms.add(new Platform(goalPlatX, goalPlatY, goalPlatW, goalPlatH));
        Array<Obstacle> obstacles = new Array<>();
        obstacles.add(new Obstacle(340, 120, 30, 30));
        obstacles.add(new Obstacle(570, 230, 30, 30));
        obstacles.add(new Obstacle(820, 140, 30, 30));
        float goalW = 40, goalH = 60;
        Goal goal = new Goal(goalPlatX + goalPlatW/2 - goalW/2, goalPlatY + goalPlatH, goalW, goalH);
        return new GameScreen(game, platforms, obstacles, goal, 2);
    }

    private GameScreen createLevel3() {
        Array<Platform> platforms = new Array<>();
        platforms.add(new Platform(40, 60, 220, 24));
        platforms.add(new Platform(300, 120, 120, 20));
        platforms.add(new Platform(470, 170, 80, 18));
        platforms.add(new Platform(600, 220, 60, 16));
        platforms.add(new Platform(700, 180, 100, 20));
        platforms.add(new Platform(850, 140, 120, 20));
        platforms.add(new Platform(1000, 100, 180, 22));
        platforms.add(new Platform(1250, 150, 120, 20));
        platforms.add(new Platform(1400, 200, 80, 18));
        platforms.add(new Platform(1500, 160, 120, 20));
        // Goal platform
        float goalPlatX = 1700, goalPlatY = 60, goalPlatW = 220, goalPlatH = 24;
        platforms.add(new Platform(goalPlatX, goalPlatY, goalPlatW, goalPlatH));
        Array<Obstacle> obstacles = new Array<>();
        obstacles.add(new Obstacle(350, 140, 30, 30));
        obstacles.add(new Obstacle(630, 240, 30, 30));
        obstacles.add(new Obstacle(870, 160, 30, 30));
        obstacles.add(new Obstacle(1420, 220, 30, 30));
        float goalW = 40, goalH = 60;
        Goal goal = new Goal(goalPlatX + goalPlatW/2 - goalW/2, goalPlatY + goalPlatH, goalW, goalH);
        return new GameScreen(game, platforms, obstacles, goal, 3);
    }

    private GameScreen createLevel4() {
        Array<Platform> platforms = new Array<>();
        platforms.add(new Platform(40, 60, 220, 24));
        platforms.add(new Platform(300, 110, 100, 20));
        platforms.add(new Platform(420, 170, 80, 18));
        platforms.add(new Platform(540, 220, 60, 16));
        platforms.add(new Platform(650, 180, 120, 20));
        platforms.add(new Platform(800, 140, 100, 20));
        platforms.add(new Platform(950, 100, 180, 22));
        platforms.add(new Platform(1200, 150, 120, 20));
        platforms.add(new Platform(1350, 200, 80, 18));
        platforms.add(new Platform(1450, 160, 120, 20));
        platforms.add(new Platform(1600, 120, 200, 24));
        platforms.add(new Platform(1850, 170, 120, 20));
        // Goal platform
        float goalPlatX = 2050, goalPlatY = 60, goalPlatW = 240, goalPlatH = 24;
        platforms.add(new Platform(goalPlatX, goalPlatY, goalPlatW, goalPlatH));
        Array<Obstacle> obstacles = new Array<>();
        obstacles.add(new Obstacle(340, 130, 30, 30));
        obstacles.add(new Obstacle(570, 240, 30, 30));
        obstacles.add(new Obstacle(820, 160, 30, 30));
        obstacles.add(new Obstacle(1370, 220, 30, 30));
        obstacles.add(new Obstacle(1700, 190, 30, 30));
        float goalW = 40, goalH = 60;
        Goal goal = new Goal(goalPlatX + goalPlatW/2 - goalW/2, goalPlatY + goalPlatH, goalW, goalH);
        return new GameScreen(game, platforms, obstacles, goal, 4);
    }

    private GameScreen createLevel5() {
        Array<Platform> platforms = new Array<>();
        platforms.add(new Platform(40, 60, 220, 24));
        platforms.add(new Platform(300, 120, 120, 20));
        platforms.add(new Platform(470, 170, 80, 18));
        platforms.add(new Platform(600, 220, 60, 16));
        platforms.add(new Platform(700, 180, 100, 20));
        platforms.add(new Platform(850, 140, 120, 20));
        platforms.add(new Platform(1000, 100, 180, 22));
        platforms.add(new Platform(1250, 150, 120, 20));
        platforms.add(new Platform(1400, 200, 80, 18));
        platforms.add(new Platform(1500, 160, 120, 20));
        platforms.add(new Platform(1650, 120, 200, 24));
        platforms.add(new Platform(1850, 170, 120, 20));
        platforms.add(new Platform(2000, 210, 100, 18));
        platforms.add(new Platform(2150, 180, 120, 20));
        // Goal platform
        float goalPlatX = 2350, goalPlatY = 60, goalPlatW = 260, goalPlatH = 24;
        platforms.add(new Platform(goalPlatX, goalPlatY, goalPlatW, goalPlatH));
        Array<Obstacle> obstacles = new Array<>();
        obstacles.add(new Obstacle(350, 140, 30, 30));
        obstacles.add(new Obstacle(630, 240, 30, 30));
        obstacles.add(new Obstacle(870, 160, 30, 30));
        obstacles.add(new Obstacle(1420, 220, 30, 30));
        obstacles.add(new Obstacle(1700, 190, 30, 30));
        obstacles.add(new Obstacle(2100, 230, 30, 30));
        float goalW = 40, goalH = 60;
        Goal goal = new Goal(goalPlatX + goalPlatW/2 - goalW/2, goalPlatY + goalPlatH, goalW, goalH);
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
