package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;

public class EndlessGameScreen extends GameScreen {

    private float lastPlatformX = 1000;
    private float score = 0;
    private BitmapFont font;
    private SpriteBatch scoreBatch;

    public EndlessGameScreen(MainGame game, Array<Platform> initialPlatforms) {
        super(game, initialPlatforms, new Array<>(), null);
        font = new BitmapFont();
        scoreBatch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        // Update score based on player's x position
        score = Math.max(score, player.rect.x);

        float farthestX = 0;
        for (Platform p : platforms) {
            if (p.getRect().x + p.getRect().width > farthestX) {
                farthestX = p.getRect().x + p.getRect().width;
            }
        }

        if (player.rect.x > farthestX - 400) {
            generateNewPlatform();
        }

        // Draw score
        scoreBatch.begin();
        font.draw(scoreBatch, "Score: " + String.format("%.0f", score), 20, Gdx.graphics.getHeight() - 20);
        scoreBatch.end();
    }

    @Override
    public void resetGame() {
        // Instead of resetting, show game over screen
        game.setScreen(new GameOverScreen(game, score));
    }

    private void generateNewPlatform() {
        float platformWidth = MathUtils.random(100, 300);
        float platformHeight = 20;
        float platformX = lastPlatformX + MathUtils.random(50, 120); // max gap: 120px
        float platformY = MathUtils.random(100, 300);

        // Create new platform
        Platform newPlatform = new Platform(platformX, platformY, platformWidth, platformHeight);
        platforms.add(newPlatform);

        // Occasionally add an obstacle on the platform
        if (MathUtils.randomBoolean(0.5f)) { // 50% chance
            float obstacleSize = 30;
            float obstacleX = platformX + MathUtils.random(10, platformWidth - obstacleSize - 10);
            float obstacleY = platformY + platformHeight;

            obstacles.add(new Obstacle(obstacleX, obstacleY, obstacleSize, obstacleSize));
        }

        lastPlatformX = platformX + platformWidth;
    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
        scoreBatch.dispose();
    }
}
