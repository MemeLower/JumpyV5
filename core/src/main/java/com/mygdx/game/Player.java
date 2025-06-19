package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {
    Sprite sprite;
    Rectangle rect;
    float speed = 500;
    float jumpVelocity = 750; // 750
    float yVelocity = 0;
    float gravity = 1500;
    boolean onGround = false;
    GameScreen gameScreen;

    enum State { IDLE, RUN, HURT }
    private State state = State.IDLE;

    private Texture idleSheet, runSheet, hurtSheet;
    private Animation<TextureRegion> idleAnim, runAnim, hurtAnim;
    private float animTime = 0f;

    public Player(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        rect = new Rectangle(150, 200, 120, 200);

        // Load sprite sheets
        idleSheet = new Texture(Gdx.files.internal("IDLE.png"));
        runSheet = new Texture(Gdx.files.internal("RUN.png"));
        hurtSheet = new Texture(Gdx.files.internal("HURT.png"));

        // Split into frames (assuming horizontal strip)
        TextureRegion[] idleFrames = new TextureRegion[10];
        TextureRegion[] runFrames = new TextureRegion[16];
        TextureRegion[] hurtFrames = new TextureRegion[4];
        int idleFrameWidth = idleSheet.getWidth() / 10;
        int runFrameWidth = runSheet.getWidth() / 16;
        int hurtFrameWidth = hurtSheet.getWidth() / 4;
        for (int i = 0; i < 10; i++) idleFrames[i] = new TextureRegion(idleSheet, i * idleFrameWidth, 0, idleFrameWidth, idleSheet.getHeight());
        for (int i = 0; i < 16; i++) runFrames[i] = new TextureRegion(runSheet, i * runFrameWidth, 0, runFrameWidth, runSheet.getHeight());
        for (int i = 0; i < 4; i++) hurtFrames[i] = new TextureRegion(hurtSheet, i * hurtFrameWidth, 0, hurtFrameWidth, hurtSheet.getHeight());

        idleAnim = new Animation<>(0.1f, idleFrames);
        runAnim = new Animation<>(0.07f, runFrames);
        hurtAnim = new Animation<>(0.12f, hurtFrames);
    }

    /**
     * Aktualisiert die Spielerposition und Physik
     * @param delta Zeit seit dem letzten Frame
     * @param platforms Liste aller Plattformen für Kollisionserkennung
     */
    public void update(float delta, Array<Platform> platforms) {
        animTime += delta;
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

        // Set state based on movement
        if (!onGround && yVelocity < 0) {
            state = State.HURT; // Example: use HURT for falling, or add a JUMP state
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            state = State.RUN;
        } else {
            state = State.IDLE;
        }
    }

    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame;
        switch (state) {
            case RUN: currentFrame = runAnim.getKeyFrame(animTime, true); break;
            case HURT: currentFrame = hurtAnim.getKeyFrame(animTime, true); break;
            case IDLE:
            default: currentFrame = idleAnim.getKeyFrame(animTime, true); break;
        }
        batch.draw(currentFrame, rect.x, rect.y, rect.width, rect.height);
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

    public void dispose() {
        idleSheet.dispose();
        runSheet.dispose();
        hurtSheet.dispose();
    }
}
