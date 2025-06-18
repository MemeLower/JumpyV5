package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class EndlessGameScreen extends GameScreen {
    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 600;
    private static final float PLATFORM_SPACING = 200;
    private static final float MIN_PLATFORM_HEIGHT = 100;
    private static final float MAX_PLATFORM_HEIGHT = 250;
    private static final float PLATFORM_WIDTH = 200;
    private static final float PLATFORM_HEIGHT = 20;
    private static final float OBSTACLE_SIZE = 30;
    private static final float SPAWN_DISTANCE = 800;
    private static final float CLEANUP_DISTANCE = -1000;

    private float lastPlatformX;
    private float lastObstacleX;
    private float score;
    private float distanceTraveled;
    private float lastScoreUpdate;
    private static final float SCORE_UPDATE_INTERVAL = 0.1f;
    private BitmapFont font;
    private SpriteBatch scoreBatch;

    public EndlessGameScreen(MainGame game) {
        super(game, new Array<>(), new Array<>(), null, -1); // -1 indicates endless mode
        this.lastPlatformX = 0;
        this.lastObstacleX = 0;
        this.score = 0;
        this.distanceTraveled = 0;
        this.lastScoreUpdate = 0;
        this.font = new BitmapFont();
        this.scoreBatch = new SpriteBatch();

        // Create initial platforms
        createInitialPlatforms();
    }

    private void createInitialPlatforms() {
        // Create starting platform
        platforms.add(new Platform(100, 100, 300, 20));
        lastPlatformX = 400;

        // Create a few more platforms to start
        for (int i = 0; i < 5; i++) {
            createNewPlatform();
        }
    }

    private void createNewPlatform() {
        float platformX = lastPlatformX + MathUtils.random(150, PLATFORM_SPACING);
        float platformY = MathUtils.random(MIN_PLATFORM_HEIGHT, MAX_PLATFORM_HEIGHT);
        float platformWidth = MathUtils.random(150, PLATFORM_WIDTH);

        if (platforms.size > 0) {
            Platform lastPlatform = platforms.get(platforms.size - 1);
            float heightDiff = Math.abs(platformY - lastPlatform.getRect().y);
            if (heightDiff > 150) {
                platformY = lastPlatform.getRect().y + MathUtils.random(-150, 150);
                platformY = MathUtils.clamp(platformY, MIN_PLATFORM_HEIGHT, MAX_PLATFORM_HEIGHT);
            }
        }

        platforms.add(new Platform(platformX, platformY, platformWidth, PLATFORM_HEIGHT));
        lastPlatformX = platformX + platformWidth;

        if (MathUtils.randomBoolean(0.5f)) {
            float obstacleX = platformX + MathUtils.random(10, platformWidth - OBSTACLE_SIZE - 10);
            float obstacleY = platformY + PLATFORM_HEIGHT;

            obstacles.add(new Obstacle(obstacleX, obstacleY, OBSTACLE_SIZE, OBSTACLE_SIZE));
        }
    }

    @Override
    public void show() {
        super.show();
        // Reset player position
        player.rect.x = 150;
        player.rect.y = 200;
        player.yVelocity = 0;
        player.onGround = false;
    }

    @Override
    public void render(float delta) {
        // Call parent render for basic game functionality
        super.render(delta);

        // Update score based on time
        lastScoreUpdate += delta;
        if (lastScoreUpdate >= SCORE_UPDATE_INTERVAL) {
            score += 1;
            lastScoreUpdate = 0;
        }

        // Check if we need to create new platforms
        if (camera.position.x + SPAWN_DISTANCE > lastPlatformX) {
            createNewPlatform();
        }

        // Clean up old platforms and obstacles
        cleanupOldObjects();

        // Draw score
        scoreBatch.begin();
        font.draw(scoreBatch, "Score: " + (int)score, 20, Gdx.graphics.getHeight() - 20);
        scoreBatch.end();
    }

    private void cleanupOldObjects() {
        float cleanupX = camera.position.x + CLEANUP_DISTANCE;

        // Remove platforms that are too far behind
        for (int i = platforms.size - 1; i >= 0; i--) {
            Platform platform = platforms.get(i);
            if (platform.getRect().x + platform.getRect().width < cleanupX) {
                platforms.removeIndex(i);
            }
        }

        // Remove obstacles that are too far behind
        for (int i = obstacles.size - 1; i >= 0; i--) {
            Obstacle obstacle = obstacles.get(i);
            if (obstacle.getRect().x + obstacle.getRect().width < cleanupX) {
                obstacles.removeIndex(i);
            }
        }
    }

    @Override
    public void resetGame() {
        // Instead of resetting, show game over screen
        game.setScreen(new GameOverScreen(game, score));
    }

    @Override
    public void dispose() {
        super.dispose();
        scoreBatch.dispose();
        font.dispose();
    }
}
