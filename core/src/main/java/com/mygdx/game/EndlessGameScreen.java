package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class EndlessGameScreen extends GameScreen {
    private float lastPlatformX = 1000;
    private float score;
    private String username = "Player";

    public EndlessGameScreen(MainGame game, Array<Platform> initialPlatforms, String username) {
        super(game, initialPlatforms, new Array<>());
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
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

        score = Math.max(score, player.rect.x);
    }

    private void generateNewPlatform() {
        float platformWidth = MathUtils.random(120, 300);
        float platformHeight = 20;
        float platformX = lastPlatformX + MathUtils.random(50, 120);
        float platformY = MathUtils.random(100, 400);

        platforms.add(new Platform(platformX, platformY, platformWidth, platformHeight));

        // Add obstacle
        if (MathUtils.randomBoolean(0.3f)) {
            float obsSize = 30;
            float obsX = platformX + MathUtils.random(10, platformWidth - obsSize - 10);
            float obsY = platformY + platformHeight;
            obstacles.add(new Obstacle(obsX, obsY, obsSize, obsSize));
        }

        lastPlatformX = platformX + platformWidth;
    }

    @Override
    public void resetGame() {
        super.resetGame();
        game.saveHighScore(new ScoreEntry(username, score));
        game.setScreen(new LeaderboardScreen(game));
    }
}
