package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GameScreen implements Screen {
    final MainGame game;
    ShapeRenderer shape;
    SpriteBatch batch;
    Player player;
    Array<Platform> platforms;
    Array<Obstacle> obstacles;
    Goal goal;
    OrthographicCamera camera;
    Viewport viewport;
    int levelNumber;
    World world;

    // Parallax background textures
    private Texture backgroundFar;
    private Texture mountainsMid;
    private Texture treesNear;

    // Add these fields to GameScreen
    private boolean showFellMessage = false;
    private float fellMessageTimer = 0f;
    private static final float FELL_MESSAGE_DURATION = 2.0f;
    private boolean showDiedMessage = false;
    private float diedMessageTimer = 0f;
    private BitmapFont fellFont;

    public GameScreen(MainGame game, Array<Platform> platforms, Array<Obstacle> obstacles, Goal goal, int levelNumber) {
        this.game = game;
        this.platforms = platforms != null ? platforms : new Array<>();
        this.obstacles = obstacles != null ? obstacles : new Array<>();
        this.goal = goal;
        this.levelNumber = levelNumber;

        // Initialize Box2D
        Box2D.init();
        world = new World(new Vector2(0, -9.81f), true);

        shape = new ShapeRenderer();
        player = new Player(this);

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(800, 480, camera);
    }

    private void resetPlayer() {
        player.rect.x = 150;
        player.rect.y = 200;
        player.yVelocity = 0;
        player.onGround = false;
        player.resetState();
    }

    public void resetGame() {
        if (levelNumber == -1) {
            // For endless mode, show game over screen
            game.setScreen(new GameOverScreen(game, 0));
        } else {
            resetPlayer();
        }
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        // Load background textures
        backgroundFar = new Texture("background_far.png");
        mountainsMid = new Texture("trees_mid.png");
        treesNear = new Texture("foreground_near.png");

        resetPlayer();

        // Set up camera and viewport
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(800, 480, camera);

        // Center the camera initially
        camera.position.set(400, 300, 0);
        camera.update();

        // Initialize the font for the 'You fell' message
        fellFont = new BitmapFont();
        fellFont.getData().setScale(4f);
        fellFont.setColor(1, 1, 0, 1); // Yellow
    }

    @Override
    public void render(float delta) {
        // Check for menu escape
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            game.setScreen(new MainMenuScreen(game));
            return;
        }

        // Update physics
        world.step(1/60f, 6, 2);

        player.update(delta, platforms);
        // Only follow player horizontally, keep camera at fixed height
        camera.position.set(player.rect.x + player.rect.width / 2, 300, 0);
        camera.update();

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            resetGame();
        }

        // Show the 'You fell' message if the player just fell
        if (player.justFell()) {
            showFellMessage = true;
            fellMessageTimer = 0f;
            player.clearJustFell();
        }
        if (showFellMessage) {
            fellMessageTimer += delta;
            if (fellMessageTimer > FELL_MESSAGE_DURATION) {
                showFellMessage = false;
            }
        }
        // Show the 'You died' message if the player just died to obstacle
        if (player.justDiedToObstacle()) {
            showDiedMessage = true;
            diedMessageTimer = 0f;
            player.clearJustDiedToObstacle();
        }
        if (showDiedMessage) {
            diedMessageTimer += delta;
            if (diedMessageTimer > FELL_MESSAGE_DURATION) {
                showDiedMessage = false;
            }
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw parallax background
        float camX = camera.position.x;
        float farFactor = 1.0f;
        float midFactor = 0.7f;
        float nearFactor = 0.3f;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Draw far background STATIC - scale to fill viewport using center-crop (no black bars)
        float viewportHeight = viewport.getWorldHeight();
        float viewportWidth = viewport.getWorldWidth();
        float imgWidth = backgroundFar.getWidth();
        float imgHeight = backgroundFar.getHeight();
        // Calculate scale to cover viewport (center-crop)
        float scale = Math.max(viewportWidth / imgWidth, viewportHeight / imgHeight);
        float drawWidth = imgWidth * scale;
        float drawHeight = imgHeight * scale;
        float farX = camera.position.x - drawWidth / 2f;
        float farY = camera.position.y - drawHeight / 2f;
        batch.draw(backgroundFar, farX, farY, drawWidth, drawHeight);

        // Draw middle layer (parallax)
        float midX = camX * midFactor;
        float midWidth = mountainsMid.getWidth();
        float midStartX = midX - viewportWidth * 20.0f;
        float midEndX = midX + viewportWidth * 20.0f;
        for (float x = midStartX; x < midEndX; x += midWidth) {
            batch.draw(mountainsMid, x, 0, midWidth, mountainsMid.getHeight());
        }

        // Draw foreground layer (parallax)
        float nearX = camX * nearFactor;
        float nearWidth = treesNear.getWidth();
        float nearStartX = nearX - viewportWidth * 20.0f;
        float nearEndX = nearX + viewportWidth * 20.0f;
        for (float x = nearStartX; x < nearEndX; x += nearWidth) {
            batch.draw(treesNear, x, 0, nearWidth, treesNear.getHeight());
        }

        // Draw the player (animated)
        player.draw(batch);

        // Draw the 'You fell' or 'You died' message if needed (at the very end, before batch.end)
        if (showFellMessage) {
            String msg = "You fell";
            float x = camera.position.x - 120;
            float y = camera.position.y + 120;
            fellFont.draw(batch, msg, x, y);
        } else if (showDiedMessage) {
            String msg = "You died";
            float x = camera.position.x - 120;
            float y = camera.position.y + 120;
            fellFont.draw(batch, msg, x, y);
        }

        batch.end();

        // Draw visual hitbox for testing
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Line);
        // player.drawHitbox(shape); // (hidden)
        shape.end();

        // Draw game objects
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        for (Platform platform : platforms) {
            platform.draw(shape);
        }
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(shape);
            // Only check collision if player is not dead
            if (!player.isDead() && player.rect.overlaps(obstacle.getRect())) {
                player.triggerDeath();
            }
        }

        if (goal != null) {
            goal.draw(shape);
            if (player.rect.overlaps(goal.getRect())) {
                goal.setReached(true);
                game.setScreen(new LevelCompleteScreen(game, levelNumber));
            }
        }

        shape.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(player.rect.x + player.rect.width / 2, 300, 0);
        camera.update();
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        shape.dispose();
        batch.dispose();
        backgroundFar.dispose();
        mountainsMid.dispose();
        treesNear.dispose();
        if (fellFont != null) fellFont.dispose();
        world.dispose();
    }

    /**
     * Getter for the ShapeRenderer to allow other classes to draw shapes
     */
    public ShapeRenderer getShapeRenderer() {
        return shape;
    }
}
