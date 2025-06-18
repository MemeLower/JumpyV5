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
        // Zeichne die Flagge in Gold
        shape.setColor(1, 0.84f, 0, 1);  // Goldene Farbe (RGBA)
        shape.rect(rect.x, rect.y, rect.width, rect.height);  // Rechteckige Flagge
        
        // Zeichne den Flaggenmast in Grau
        shape.setColor(0.5f, 0.5f, 0.5f, 1);  // Graue Farbe
        shape.rect(rect.x + rect.width/2 - 2, rect.y, 4, rect.height);  // Dünner Mast in der Mitte
    }
} 