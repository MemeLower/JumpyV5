package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Goal {
    private Rectangle rect;
    private boolean reached;

    public Goal(float x, float y, float width, float height) {
        rect = new Rectangle(x, y, width, height);
        reached = false;
    }

    public Rectangle getRect() {
        return rect;
    }

    public boolean isReached() {
        return reached;
    }

    public void setReached(boolean reached) {
        this.reached = reached;
    }

    public void draw(ShapeRenderer shape) {
        // Draw a golden flag-like shape
        shape.setColor(1, 0.84f, 0, 1); // Gold color
        shape.rect(rect.x, rect.y, rect.width, rect.height);
        
        // Draw the flag pole
        shape.setColor(0.5f, 0.5f, 0.5f, 1); // Gray color
        shape.rect(rect.x + rect.width/2 - 2, rect.y, 4, rect.height);
    }
} 