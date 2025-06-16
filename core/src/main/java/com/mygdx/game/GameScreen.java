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
    OrthographicCamera camera;
    Viewport viewport;

    // Parallax background textures
    private Texture backgroundFar;
    private Texture treesMid;
    private Texture foregroundNear;

    public GameScreen(MainGame game, Array<Platform> platforms, Array<Obstacle> obstacles) {
        this.game = game;
        this.platforms = platforms;
        this.obstacles = obstacles;

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
        treesMid = new Texture("trees_mid.png");
        foregroundNear = new Texture("foreground_near.png");

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

        float farFactor = 1f;
        float midFactor = 0.9f;
        float nearFactor = 0.5f;

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Draw each layer
        batch.draw(backgroundFar,
            camX * farFactor - backgroundFar.getWidth() / 2, 0,
            backgroundFar.getWidth(), backgroundFar.getHeight());

        batch.draw(treesMid,
            camX * midFactor - treesMid.getWidth() / 2, 0,
            treesMid.getWidth(), treesMid.getHeight());

        batch.draw(foregroundNear,
            camX * nearFactor - foregroundNear.getWidth() / 2, 0,
            foregroundNear.getWidth(), foregroundNear.getHeight());

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
        treesMid.dispose();
        foregroundNear.dispose();
    }
}
