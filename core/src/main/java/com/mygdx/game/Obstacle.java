package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Obstacle {
    private Rectangle rect;

    public Obstacle(float x, float y, float width, float height) {
        rect = new Rectangle(x, y, width, height);
    }

    public Rectangle getRect() {
        return rect;
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(1, 0, 0, 1); // Rot
        shape.rect(rect.x, rect.y, rect.width, rect.height);
    }
}
