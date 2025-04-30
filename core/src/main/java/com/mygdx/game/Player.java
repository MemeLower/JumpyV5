package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class Player {
    // Charakter Variablen/Eigenschaften
    Rectangle rect;
    float speed = 500;      //pixel pro sekunde
    float jumpVelocity = 750;
    float yVelocity = 0;
    float gravity = 1500;
    boolean onGround = false;
    MainGame mainGame;

    public Player(MainGame mainGame) {
        this.mainGame = mainGame;
        rect = new Rectangle(150, 200, 30, 50); //startpunkt
    }

    public void update(float delta, Array<Platform> platforms) {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {     //links
            rect.x -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {     //rechts
            rect.x += speed * delta;
        }

        yVelocity -= gravity * delta;       //gravity
        rect.y += yVelocity * delta;

        onGround = false;
        for (Platform platform : platforms) {
            if (yVelocity <= 0 &&
                rect.y > platform.getRect().y + platform.getRect().height - 5 &&
                rect.y + yVelocity * delta <= platform.getRect().y + platform.getRect().height &&
                rect.x + rect.width > platform.getRect().x &&
                rect.x < platform.getRect().x + platform.getRect().width) {
                rect.y = platform.getRect().y + platform.getRect().height;
                yVelocity = 0;
                onGround = true;
                break;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && onGround) {     //springen
            yVelocity = jumpVelocity;
            onGround = false;
        }

        if (rect.y < -100) {        //automatisches reset bei fall
            System.out.println("Player fell off! Resetting...");
            mainGame.resetGame();
        }
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(1, 0, 1, 1);     //farbe des Characters
        shape.rect(rect.x, rect.y, rect.width, rect.height);
    }
}
