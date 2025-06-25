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

    /**
     * Zeichnet das Ziel (Flagge) auf dem Bildschirm
     * @param shape ShapeRenderer für das Zeichnen der Formen
     */
    public void draw(ShapeRenderer shape) {
        float poleWidth = rect.width * 0.18f;
        float poleX = rect.x + rect.width * 0.5f - poleWidth * 0.5f;
        float poleY = rect.y;
        float poleHeight = rect.height * 1.2f;
        // Draw pole (white)
        shape.setColor(1, 1, 1, 1);
        shape.rect(poleX, poleY, poleWidth, poleHeight);

        // Draw gold ball at top
        shape.setColor(1, 0.85f, 0.2f, 1);
        shape.circle(poleX + poleWidth / 2, poleY + poleHeight, poleWidth * 1.2f, 20);

        // Draw green base
        shape.setColor(0.2f, 0.7f, 0.2f, 1);
        shape.rect(poleX - poleWidth * 0.7f, poleY - rect.height * 0.18f, poleWidth * 2.4f, rect.height * 0.18f);

        // Draw flag (green triangle) near the top, waving right
        float flagY = poleY + poleHeight * 0.82f;
        float flagW = rect.width * 0.7f;
        float flagH = rect.height * 0.32f;
        shape.setColor(0.2f, 0.7f, 0.2f, 1);
        shape.triangle(
            poleX + poleWidth, flagY,
            poleX + poleWidth + flagW, flagY + flagH * 0.5f,
            poleX + poleWidth, flagY + flagH
        );
    }
} 