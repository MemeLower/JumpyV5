package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class EndlessGameScreen extends GameScreen {

    private float lastPlatformX = 1000;

    public EndlessGameScreen(MainGame game, Array<Platform> initialPlatforms) {
        super(game, initialPlatforms, new Array<>());
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        float farthestX = 0;
        for (Platform p : platforms) {
            if (p.getRect().x + p.getRect().width > farthestX) {
                farthestX = p.getRect().x + p.getRect().width;
            }
        }

        if (player.rect.x > farthestX - 400) {
            generateNewPlatform();
        }
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
        if (MathUtils.randomBoolean(0.5f)) { // 30% chance
            float obstacleSize = 30;
            float obstacleX = platformX + MathUtils.random(10, platformWidth - obstacleSize - 10);
            float obstacleY = platformY + platformHeight;

            obstacles.add(new Obstacle(obstacleX, obstacleY, obstacleSize, obstacleSize));
        }

        lastPlatformX = platformX + platformWidth;
    }
}
