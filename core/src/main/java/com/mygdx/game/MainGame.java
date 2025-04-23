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
    FitViewport viewport;

    @Override
    public void create() {
        shape = new ShapeRenderer();
        player = new Player(this);

        platforms = new Array<>();
        platforms.add(new Platform(100, 100, 300, 20));
        platforms.add(new Platform(600, 100, 300, 20));
        platforms.add(new Platform(1200, 200, 100, 20));
        platforms.add(new Platform(1700, 50, 300, 20));

        platforms.add(new Platform(2200, 100, 300, 20));
        platforms.add(new Platform(2700, 200, 50, 20));
        platforms.add(new Platform(3100,300, 200, 20));
        platforms.add(new Platform(3500, 100, 50, 20));

        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 480, camera);
        viewport.apply();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        player.update(delta, platforms);
        camera.position.set(player.rect.x + player.rect.width / 2, player.rect.y + player.rect.height / 2, 0);
        camera.update();

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            resetGame();
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
        viewport.update(width, height);
        camera.position.set(player.rect.x + player.rect.width / 2, player.rect.y + player.rect.height / 2, 0);
        camera.update();
    }

    public void resetGame() {
        player.rect.x = 150;
        player.rect.y = 200;
        player.yVelocity = 0;
        player.onGround = false;
    }

    @Override
    public void dispose() {
        shape.dispose();
    }
}
