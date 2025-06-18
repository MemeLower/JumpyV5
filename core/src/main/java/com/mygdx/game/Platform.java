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
        shape.setColor(0.4f, 0.4f, 1f, 1); // Blau
        shape.rect(rect.x, rect.y, rect.width, rect.height);
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
