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
import com.badlogic.gdx.physics.box2d.World;

public class EndlessGameScreen extends GameScreen {
    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;
    private static final float MIN_PLATFORM_SPACING = 200;
    private static final float MAX_PLATFORM_SPACING = 300;
    private static final float MIN_PLATFORM_HEIGHT = 200;
    private static final float MAX_PLATFORM_HEIGHT = 500;
    private static final float MIN_PLATFORM_WIDTH = 120;
    private static final float MAX_PLATFORM_WIDTH = 200;
    private static final float PLATFORM_HEIGHT = 20;
    private static final float OBSTACLE_SIZE = 30;
    private static final float SPAWN_DISTANCE = 800;
    private static final float CLEANUP_DISTANCE = -1000;
    private static final float MAX_HEIGHT_DIFF = 100;
    private static final float OBSTACLE_CHANCE = 0.3f;
    private static final float SCORE_INTERVAL = 0.1f;
    private static final float MIN_HEIGHT_DIFF = 10f;
    private static final float MAX_SCORE_PER_INTERVAL = 5f;
    private static final float VISIBLE_BUFFER = 100f;
    private static final float PLAYER_START_HEIGHT = 50f; // Height above platform to start player

    private float lastPlatformX;
    private float lastObstacleX;
    private float score;
    private float distanceTraveled;
    private float scoreTimer = 0;
    private float lastPlayerX = 0;
    private float highestY = 0;
    private BitmapFont font;
    private SpriteBatch scoreBatch;
    private OrthographicCamera uiCamera;

    public EndlessGameScreen(MainGame game) {
        super(game, new Array<>(), new Array<>(), null, -1);
        this.lastPlatformX = 0;
        this.lastObstacleX = 0;
        this.score = 0;
        this.distanceTraveled = 0;
        this.lastPlayerX = 0;
        this.font = new BitmapFont();
        this.scoreBatch = new SpriteBatch();
        this.uiCamera = new OrthographicCamera();
        this.uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        createInitialPlatforms();
    }

    private void createInitialPlatforms() {
        // Create starting platform at a higher position
        Platform startPlatform = new Platform(100, 200, 150, PLATFORM_HEIGHT);
        platforms.add(startPlatform);
        world.createBody(startPlatform.getBodyDef()).createFixture(startPlatform.getFixtureDef());

        // Create initial platforms to fill the screen
        for (int i = 1; i <= 6; i++) {
            createNewPlatform();
        }
    }

    @Override
    public void show() {
        super.show();
        
        // Position player above the starting platform
        if (platforms.size > 0) {
            Platform startPlatform = platforms.get(0);
            player.rect.x = startPlatform.getX() + 50; // Center on platform
            player.rect.y = startPlatform.getY() + startPlatform.getRect().height + PLAYER_START_HEIGHT;
            player.yVelocity = 0;
            player.onGround = false;
        }
        
        lastPlayerX = player.getX();
        highestY = player.getY();
    }

    private void createNewPlatform() {
        float lastX = platforms.size > 0 ? platforms.get(platforms.size - 1).getX() : 0;
        float lastY = platforms.size > 0 ? platforms.get(platforms.size - 1).getY() : 0;
        
        // Calculate new platform position with random spacing
        float spacing = MathUtils.random(MIN_PLATFORM_SPACING, MAX_PLATFORM_SPACING);
        float newX = lastX + spacing;
        
        // Calculate new height with a more controlled random variation
        float heightDiff = MathUtils.random(-MAX_HEIGHT_DIFF, MAX_HEIGHT_DIFF);
        float newY = lastY + heightDiff;
        
        // Ensure the platform stays within the visible area plus buffer
        float minVisibleY = camera.position.y - camera.viewportHeight/2 - VISIBLE_BUFFER;
        float maxVisibleY = camera.position.y + camera.viewportHeight/2 + VISIBLE_BUFFER;
        newY = MathUtils.clamp(newY, 
            Math.max(MIN_PLATFORM_HEIGHT, minVisibleY), 
            Math.min(MAX_PLATFORM_HEIGHT, maxVisibleY));
        
        // Random platform width
        float platformWidth = MathUtils.random(MIN_PLATFORM_WIDTH, MAX_PLATFORM_WIDTH);
        
        // Create the platform
        Platform platform = new Platform(newX, newY, platformWidth, PLATFORM_HEIGHT);
        platforms.add(platform);
        world.createBody(platform.getBodyDef()).createFixture(platform.getFixtureDef());

        // Randomly add an obstacle
        if (MathUtils.random() < OBSTACLE_CHANCE) {
            float obstacleX = newX + MathUtils.random(30, platformWidth - 30);
            Obstacle obstacle = new Obstacle(obstacleX, newY + PLATFORM_HEIGHT, OBSTACLE_SIZE, OBSTACLE_SIZE);
            obstacles.add(obstacle);
        }
    }

    @Override
    public void render(float delta) {
        // Call parent render for basic game functionality
        super.render(delta);

        // Update score based on horizontal distance traveled
        scoreTimer += delta;
        if (scoreTimer >= SCORE_INTERVAL) {
            float currentX = player.getX();
            
            // Only increase score if player has moved right
            if (currentX > lastPlayerX) {
                float distanceDiff = currentX - lastPlayerX;
                score += distanceDiff;
                lastPlayerX = currentX;
            }
            
            scoreTimer = 0;
        }

        // Check if we need to create new platforms
        if (camera.position.x + SPAWN_DISTANCE > platforms.get(platforms.size - 1).getX()) {
            createNewPlatform();
        }

        // Clean up old objects
        cleanupOldObjects();

        // Draw score with UI camera (fixed position)
        scoreBatch.setProjectionMatrix(uiCamera.combined);
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
    public void resize(int width, int height) {
        super.resize(width, height);
        uiCamera.setToOrtho(false, width, height);
    }

    @Override
    public void resetGame() {
        // Instead of resetting, show game over screen
        game.setScreen(new GameOverScreen(game, score));
    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
        scoreBatch.dispose();
    }
}
