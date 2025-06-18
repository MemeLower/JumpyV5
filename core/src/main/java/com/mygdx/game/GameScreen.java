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

public class GameScreen implements Screen {
    final MainGame game;
    ShapeRenderer shape;
    SpriteBatch batch; // For drawing textures
    Player player;
    Array<Platform> platforms;
    Array<Obstacle> obstacles;
    Goal goal;
    OrthographicCamera camera;
    Viewport viewport;
    int levelNumber;

    // Parallax background textures
    private Texture backgroundFar;
    private Texture mountainsMid;
    private Texture treesNear;

    public GameScreen(MainGame game, Array<Platform> platforms, Array<Obstacle> obstacles, Goal goal, int levelNumber) {
        this.game = game;
        this.platforms = platforms != null ? platforms : new Array<>();
        this.obstacles = obstacles != null ? obstacles : new Array<>();
        this.goal = goal;
        this.levelNumber = levelNumber;

        shape = new ShapeRenderer();
        player = new Player(this); // Pass GameScreen to Player

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(800, 480, camera);
    }

    private void resetPlayer() {
        player.rect.x = 150;
        player.rect.y = 200;
        player.yVelocity = 0;
        player.onGround = false;
    }

    public void resetGame() {
        resetPlayer();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        // Load background textures
        backgroundFar = new Texture("background_far.png");
        mountainsMid = new Texture("trees_mid.png"); // This is actually mountains
        treesNear = new Texture("foreground_near.png"); // This is actually trees

        resetPlayer();

        // Set up camera and viewport
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(800, 480, camera); // Adjust these values to match your desired resolution

        // Center the camera initially
        camera.position.set(400, 300, 0); // Assuming 800x600 screen
        camera.update();
    }

    @Override
    public void render(float delta) {
        player.update(delta, platforms);
        camera.position.set(player.rect.x + player.rect.width / 2, 300, 0);
        camera.update();

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            resetGame();
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // ---- PARALLAX BACKGROUND DRAWING ----
        float camX = camera.position.x;

        // Parallax factors
        float farFactor = 1.0f;     // Far background moves at camera speed
        float midFactor = 0.7f;     // Mountains move slower
        float nearFactor = 0.3f;    // Trees move even slower

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Draw far background (single centered image)
        float farX = camX * farFactor;
        batch.draw(backgroundFar,
            farX - backgroundFar.getWidth() / 2, 0,
            backgroundFar.getWidth(), backgroundFar.getHeight());

        // Draw middle layer (mountains) with repeating pattern
        float midX = camX * midFactor;
        float midWidth = mountainsMid.getWidth();
        float viewportWidth = viewport.getWorldWidth();
        
        // Calculate visible range for mountains - extremely large coverage for endless mode
        float midStartX = midX - viewportWidth * 20.0f;
        float midEndX = midX + viewportWidth * 20.0f;
        
        // Draw mountains
        for (float x = midStartX; x < midEndX; x += midWidth) {
            batch.draw(mountainsMid, x, 0, midWidth, mountainsMid.getHeight());
        }

        // Draw foreground layer (trees) with repeating pattern
        float nearX = camX * nearFactor;
        float nearWidth = treesNear.getWidth();
        
        // Calculate visible range for trees - extremely large coverage for endless mode
        float nearStartX = nearX - viewportWidth * 20.0f;
        float nearEndX = nearX + viewportWidth * 20.0f;
        
        // Draw trees
        for (float x = nearStartX; x < nearEndX; x += nearWidth) {
            batch.draw(treesNear, x, 0, nearWidth, treesNear.getHeight());
        }

        batch.end();
        // ---- END OF PARALLAX BACKGROUND ----

        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeRenderer.ShapeType.Filled);

        player.draw(shape);
        for (Platform platform : platforms) {
            platform.draw(shape);
        }
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(shape);
            if (player.rect.overlaps(obstacle.getRect())) {
                resetGame();
            }
        }

        // Draw and check goal
        if (goal != null) {
            goal.draw(shape);
            if (player.rect.overlaps(goal.getRect())) {
                goal.setReached(true);
                // Show level complete screen with level number
                game.setScreen(new LevelCompleteScreen(game, levelNumber));
            }
        }

        shape.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(player.rect.x + player.rect.width / 2, player.rect.y + player.rect.height / 2, 0);
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
    }
}
