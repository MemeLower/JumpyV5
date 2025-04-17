package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
        player = new Player();

        // Create multiple platforms
        platforms = new Array<>();
        platforms.add(new Platform(100, 100, 300, 20)); // Ground platform
        platforms.add(new Platform(400, 250, 200, 20)); // Higher platform
        platforms.add(new Platform(700, 400, 150, 20)); // Even higher platform
        platforms.add(new Platform(1000, 550, 250, 20)); // Final platform

        // Set up the camera and viewport
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 480, camera); // Set initial virtual resolution
        viewport.apply(); // Apply the viewport settings
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // Update player and camera
        player.update(delta, platforms);
        camera.position.set(player.rect.x + player.rect.width / 2, player.rect.y + player.rect.height / 2, 0); // Center on player
        camera.update();

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

        // Debug rendering
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(1, 0, 0, 1); // Red outline for debugging
        shape.rect(player.rect.x, player.rect.y, player.rect.width, player.rect.height); // Player outline
        for (Platform platform : platforms) {
            shape.rect(platform.getRect().x, platform.getRect().y, platform.getRect().width, platform.getRect().height); // Platform outlines
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

    @Override
    public void dispose() {
        shape.dispose();
    }
}
