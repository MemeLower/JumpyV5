package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class Player {
    Sprite sprite;
    Rectangle rect;
    float speed = 500;
    float jumpVelocity = 750; // 750
    float yVelocity = 0;
    float gravity = 1500;
    boolean onGround = false;
    GameScreen gameScreen;

    public Player(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        rect = new Rectangle(150, 200, 30, 50);
        //sprite = new Sprite(new Texture(Gdx.files.internal("frame_0_delay-0.04s.png")));
    }

    /**
     * Aktualisiert die Spielerposition und Physik
     * @param delta Zeit seit dem letzten Frame
     * @param platforms Liste aller Plattformen für Kollisionserkennung
     */
    public void update(float delta, Array<Platform> platforms) {
        // Horizontale Bewegung: A/D oder Pfeiltasten
        if (Gdx.input.isKeyPressed(Input.Keys.A)) rect.x -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) rect.x += speed * delta;

        // Vertikale Bewegung: Schwerkraft und Geschwindigkeit
        yVelocity -= gravity * delta;  // Schwerkraft wirkt nach unten
        rect.y += yVelocity * delta;   // Position wird aktualisiert
        onGround = false;              // Standardmäßig in der Luft

        // Kollisionserkennung mit Plattformen
        for (Platform platform : platforms) {
            // Prüft ob Spieler auf Plattform landet
            if (yVelocity <= 0 &&                                    // Fallend
                rect.y > platform.getRect().y + platform.getRect().height - 5 &&  // Über Plattform
                rect.y + yVelocity * delta <= platform.getRect().y + platform.getRect().height &&  // Wird landen
                rect.x + rect.width > platform.getRect().x &&        // Horizontale Überlappung
                rect.x < platform.getRect().x + platform.getRect().width) {
                rect.y = platform.getRect().y + platform.getRect().height;  // Position korrigieren
                yVelocity = 0;                                       // Fall stoppen
                onGround = true;                                     // Auf Boden markieren
                break;
            }
        }

        // Springen: Nur möglich wenn auf dem Boden
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && onGround) {
            yVelocity = jumpVelocity;  // Aufwärtsgeschwindigkeit setzen
            onGround = false;          // Nicht mehr auf dem Boden
        }

        // Spieler ist gefallen - Level neu starten
        if (rect.y < -100) {
            System.out.println("Player fell off! Resetting...");
            gameScreen.resetGame();
        }
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(1, 0, 1, 1);
        shape.rect(rect.x, rect.y, rect.width, rect.height);
    }

    public float getX() {
        return rect.x;
    }

    public float getY() {
        return rect.y;
    }

    public float getWidth() {
        return rect.width;
    }

    public float getHeight() {
        return rect.height;
    }
}
