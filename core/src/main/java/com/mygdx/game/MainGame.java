package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport; // Add this import

public class MainGame extends ApplicationAdapter {
    ShapeRenderer shape;
    Player player;
    Array<Platform> platforms;
    OrthographicCamera camera;
    FitViewport viewport; // Add a viewport

    @Override
    public void create() {
        shape = new ShapeRenderer();
        player = new Player(this); // Pass MainGame instance

        // Create multiple platforms
        platforms = new Array<>();
        platforms.add(new Platform(100, 100, 300, 20)); // Ground platform
        platforms.add(new Platform(400, 250, 200, 20)); // Higher platform
        platforms.add(new Platform(700, 400, 150, 20)); // Even higher platform
        platforms.add(new Platform(1000, 550, 250, 20)); // Final platform

        // Set up the camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 480, camera);
        viewport.apply();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // Update player and camera
        player.update(delta, platforms);
        camera.position.set(player.rect.x + player.rect.width / 2, player.rect.y + player.rect.height / 2, 0); // Center on player
        camera.update();

        // Check for reset input
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) { // Press 'R' to reset
            resetGame();
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Apply camera transformation
        shape.setProjectionMatrix(camera.combined);

        shape.begin(ShapeRenderer.ShapeType.Filled);
        player.draw(shape);
        for (Platform platform : platforms) {
            platform.draw(shape);
        }
        shape.end();
    }

    @Override
    public void resize(int width, int height) {
        // Update the viewport when the window is resized
        viewport.update(width, height);
        camera.position.set(player.rect.x + player.rect.width / 2, player.rect.y + player.rect.height / 2, 0); // Keep camera centered on player
        camera.update();
    }

    public void resetGame() {
        // Reset player position
        player.rect.x = 150; // Starting X position
        player.rect.y = 200; // Starting Y position
        player.yVelocity = 0; // Reset vertical velocity
        player.onGround = false; // Ensure the player isn't on the ground initially

        // Optionally reset platforms or other game elements here
        // For example, if you have moving platforms or enemies, reset their positions.
    }

    @Override
    public void dispose() {
        shape.dispose();
    }
}
