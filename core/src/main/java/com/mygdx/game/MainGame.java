package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.awt.*;

public class MainGame extends ApplicationAdapter {
    ShapeRenderer shape;
    Player player;
    Array<Platform> platforms;
    Array<Obstacle> obstacles;
    OrthographicCamera camera;
    FitViewport viewport;

    @Override
    public void create() {
        shape = new ShapeRenderer();    // Rechteck für Charakter wird visualisiert
        player = new Player(this);

        platforms = new Array<>();    //balken
        platforms.add(new Platform(100, 100, 300, 20));
        platforms.add(new Platform(400, 250, 200, 20));
        platforms.add(new Platform(700, 400, 150, 20));
        platforms.add(new Platform(1000, 550, 250, 20));

        obstacles = new Array<>();      //hindernisse
        obstacles.add(new Obstacle(200, 100, 30, 30));
        obstacles.add(new Obstacle(500, 250, 30, 30));


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
        player.draw(shape);  //spieler erstellen
        for (Platform platform : platforms) {
            platform.draw(shape);
        }
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(shape);
            if (player.rect.overlaps(obstacle.getRect())){
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

    public void resetGame() {
        player.rect.x = 80;
        player.rect.y = 200;
        player.yVelocity = 0;
        player.onGround = false;
    }

    @Override
    public void dispose() {
        shape.dispose();
    }
}
