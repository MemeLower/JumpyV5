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
        float x = rect.x;
        float y = rect.y;
        float w = rect.width;
        float h = rect.height;

        // 1. Draw border (dark red)
        shape.setColor(0.4f, 0, 0, 1);
        shape.rect(x - 2, y - 2, w + 4, h + 4);

        // 2. Draw main body (gradient: top bright, bottom dark)
        // Top half (bright red)
        shape.setColor(1f, 0.3f, 0.3f, 1);
        shape.rect(x, y + h / 2, w, h / 2);
        // Bottom half (dark red)
        shape.setColor(0.7f, 0, 0, 1);
        shape.rect(x, y, w, h / 2);

        // 3. Highlight ellipse at top-left
        shape.setColor(1, 1, 1, 0.35f);
        shape.ellipse(x + w * 0.12f, y + h * 0.65f, w * 0.25f, h * 0.18f);
    }
}
