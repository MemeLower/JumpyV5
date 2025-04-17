package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class Player {
    Rectangle rect;
    float speed = 200; // Horizontal speed
    float jumpVelocity = 800; // Jump strength
    float yVelocity = 0; // Vertical velocity
    float gravity = 1500; // Gravity strength
    boolean onGround = false;
    MainGame mainGame; // Reference to MainGame

    public Player(MainGame mainGame) {
        this.mainGame = mainGame; // Store the MainGame reference
        rect = new Rectangle(150, 200, 30, 50); // Start above the platform
    }

    public void update(float delta, Array<Platform> platforms) {
        // Horizontal movement
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            rect.x -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            rect.x += speed * delta;
        }

        // Gravity
        yVelocity -= gravity * delta; // Apply gravity
        rect.y += yVelocity * delta;

        // Collision with platforms
        onGround = false; // Assume not on ground initially
        for (Platform platform : platforms) {
            if (yVelocity <= 0 && // Falling downward
                rect.y > platform.getRect().y + platform.getRect().height - 5 && // Near the platform's top
                rect.y + yVelocity * delta <= platform.getRect().y + platform.getRect().height && // Feet touch the platform
                rect.x + rect.width > platform.getRect().x && // Horizontally overlapping
                rect.x < platform.getRect().x + platform.getRect().width) {
                rect.y = platform.getRect().y + platform.getRect().height; // Snap to platform
                yVelocity = 0; // Stop falling
                onGround = true; // Allow jumping again
                break; // Stop checking other platforms
            }
        }

        // Jump
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && onGround) {
            yVelocity = jumpVelocity;
            onGround = false;
        }

        // Fall detection
        if (rect.y < -100) { // Player falls below the screen
            System.out.println("Player fell off! Resetting...");
            mainGame.resetGame(); // Call resetGame() from MainGame
        }
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(0, 1, 0, 1); // Green
        shape.rect(rect.x, rect.y, rect.width, rect.height);
    }
}
