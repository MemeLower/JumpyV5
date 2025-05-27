package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;

public class GameScreen implements Screen {
    final MainGame game;
    ShapeRenderer shape;
    Player player;
    Array<Platform> platforms;
    Array<Obstacle> obstacles;
    OrthographicCamera camera;
    Viewport viewport;

    public GameScreen(MainGame game, Array<Platform> platforms, Array<Obstacle> obstacles) {
        this.game = game;
        this.platforms = platforms;
        this.obstacles = obstacles;

        shape = new ShapeRenderer();
        player = new Player(this); // Pass GameScreen to Player

        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 480, camera);
    }

    private void resetPlayer() {
        player.rect.x = 80;
        player.rect.y = 200;
        player.yVelocity = 0;
        player.onGround = false;
    }

    public void resetGame() {
        resetPlayer();
    }

    @Override
    public void render(float delta) {
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
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        shape.dispose();
    }
}
