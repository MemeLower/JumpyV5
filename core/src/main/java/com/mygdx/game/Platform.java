package com.mygdx.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Platform {
    private Rectangle rect;
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;

    public Platform(float x, float y, float width, float height) {
        rect = new Rectangle(x, y, width, height);
        
        // Create Box2D body definition
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x + width/2, y + height/2);
        
        // Create Box2D fixture definition
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2, height/2);
        
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 0.0f;
    }

    public void draw(ShapeRenderer shape) {
        float x = rect.x;
        float y = rect.y;
        float w = rect.width;
        float h = rect.height;

        // 1. Draw shadow
        shape.setColor(0, 0, 0, 0.25f);
        shape.rect(x + 4, y - 6, w, h);

        // 2. Draw border (darker blue)
        shape.setColor(0.15f, 0.15f, 0.5f, 1);
        shape.rect(x - 2, y - 2, w + 4, h + 4);

        // 3. Draw main body (gradient: top lighter, bottom darker)
        // Top half (lighter blue)
        shape.setColor(0.6f, 0.7f, 1f, 1);
        shape.rect(x, y + h / 2, w, h / 2);
        // Bottom half (darker blue)
        shape.setColor(0.3f, 0.4f, 0.8f, 1);
        shape.rect(x, y, w, h / 2);

        // 4. Highlight stripe at the top
        shape.setColor(1, 1, 1, 0.25f);
        shape.rect(x + 4, y + h - 4, w - 8, 4);
    }

    public Rectangle getRect() {
        return rect;
    }

    public float getX() {
        return rect.x;
    }

    public float getY() {
        return rect.y;
    }

    public BodyDef getBodyDef() {
        return bodyDef;
    }

    public FixtureDef getFixtureDef() {
        return fixtureDef;
    }
}
