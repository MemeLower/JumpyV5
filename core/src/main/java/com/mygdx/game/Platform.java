package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Platform {
    private Rectangle rect;

    public Platform(float x, float y, float width, float height) {
        rect = new Rectangle(x, y, width, height);
    }

    public Rectangle getRect() {
        return rect;
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(0.4f, 0.4f, 1f, 1); // Blue
        shape.rect(rect.x, rect.y, rect.width, rect.height);
    }
}
