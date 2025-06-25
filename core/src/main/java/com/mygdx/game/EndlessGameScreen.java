package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class EndlessGameScreen extends GameScreen {
    // LEVEL-GENERIERUNG PARAMETER: Einstellungen für automatische Level-Erstellung
    private static final float WORLD_WIDTH = 1280;
    private static final float WORLD_HEIGHT = 720;
    private static final float MIN_PLATFORM_SPACING = 200;    // Mindestabstand zwischen Plattformen
    private static final float MAX_PLATFORM_SPACING = 300;    // Maximalabstand zwischen Plattformen
    private static final float MIN_PLATFORM_HEIGHT = 150;     // Minimale Plattform-Höhe
    private static final float MAX_PLATFORM_HEIGHT = 200;     // Maximale Plattform-Höhe
    private static final float MIN_PLATFORM_WIDTH = 120;      // Minimale Plattform-Breite
    private static final float MAX_PLATFORM_WIDTH = 200;      // Maximale Plattform-Breite
    private static final float PLATFORM_HEIGHT = 20;          // Dicke der Plattformen
    private static final float OBSTACLE_SIZE = 30;            // Größe der Hindernisse
    private static final float SPAWN_DISTANCE = 800;          // Wann neue Plattformen erstellt werden
    private static final float CLEANUP_DISTANCE = -1000;      // Wann alte Objekte gelöscht werden
    private static final float MAX_HEIGHT_DIFF = 60;          // Maximale Höhendifferenz zwischen Plattformen
    private static final float OBSTACLE_CHANCE = 0.3f;        // Wahrscheinlichkeit für Hindernisse (30%)
    private static final float SCORE_INTERVAL = 0.1f;         // Wie oft Score aktualisiert wird
    private static final float MIN_HEIGHT_DIFF = 10f;         // Minimale Höhendifferenz
    private static final float MAX_SCORE_PER_INTERVAL = 5f;   // Maximaler Score pro Intervall
    private static final float VISIBLE_BUFFER = 100f;         // Sichtbarer Puffer
    private static final float PLAYER_START_HEIGHT = 50f;     // Höhe über Plattform zum Starten

    private float lastPlatformX;
    private float lastObstacleX;
    private float score;
    private float distanceTraveled;
    private float scoreTimer = 0;
    private float lastPlayerX = 0;
    private float highestY = 0;
    private BitmapFont font;
    private SpriteBatch scoreBatch;
    private OrthographicCamera uiCamera;
    private ShapeRenderer uiShapeRenderer; // For score background

    public EndlessGameScreen(MainGame game) {
        super(game, new Array<>(), new Array<>(), null, -1);
        this.lastPlatformX = 0;
        this.lastObstacleX = 0;
        this.score = 0;
        this.distanceTraveled = 0;
        this.lastPlayerX = 0;
        this.font = new BitmapFont();
        this.scoreBatch = new SpriteBatch();
        this.uiCamera = new OrthographicCamera();
        this.uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.uiShapeRenderer = new ShapeRenderer();
        createInitialPlatforms();
    }

    private void createInitialPlatforms() {
        // Create starting platform at a higher position
        Platform startPlatform = new Platform(100, 200, 150, PLATFORM_HEIGHT);
        platforms.add(startPlatform);
        world.createBody(startPlatform.getBodyDef()).createFixture(startPlatform.getFixtureDef());

        // Create initial platforms to fill the screen
        for (int i = 1; i <= 6; i++) {
            createNewPlatform();
        }
    }

    @Override
    public void show() {
        super.show();
        
        // Set up improved font for score display
        font.getData().setScale(2.5f); // Much bigger font
        font.setColor(1, 1, 1, 1); // White color
        
        // Position player above the starting platform
        if (platforms.size > 0) {
            Platform startPlatform = platforms.get(0);
            player.rect.x = startPlatform.getX() + 50; // Center on platform
            player.rect.y = startPlatform.getY() + startPlatform.getRect().height + PLAYER_START_HEIGHT;
            player.yVelocity = 0;
            player.onGround = false;
        }
        
        lastPlayerX = player.getX();
        highestY = player.getY();
    }

    private void createNewPlatform() {
        // PLATTFORM-POSITION: Berechne Position basierend auf letzter Plattform
        float lastX = platforms.size > 0 ? platforms.get(platforms.size - 1).getX() : 0;
        float lastY = platforms.size > 0 ? platforms.get(platforms.size - 1).getY() : 0;
        
        // ZUFÄLLIGER ABSTAND: Neue Plattform mit zufälligem Abstand zur letzten
        float spacing = MathUtils.random(MIN_PLATFORM_SPACING, MAX_PLATFORM_SPACING);
        float newX = lastX + spacing;
        
        // ZUFÄLLIGE HÖHE: Kontrollierte zufällige Höhenänderung für spielbare Level
        float heightDiff = MathUtils.random(-MAX_HEIGHT_DIFF, MAX_HEIGHT_DIFF);
        float newY = lastY + heightDiff;
        
        // HÖHEN-BEGRENZUNG: Halte Plattformen in spielbarem Bereich
        newY = MathUtils.clamp(newY, MIN_PLATFORM_HEIGHT, MAX_PLATFORM_HEIGHT);
        
        // ZUFÄLLIGE BREITE: Verschiedene Plattform-Größen für Abwechslung
        float platformWidth = MathUtils.random(MIN_PLATFORM_WIDTH, MAX_PLATFORM_WIDTH);
        
        // PLATTFORM ERSTELLEN: Neue Plattform zum Spiel hinzufügen
        Platform platform = new Platform(newX, newY, platformWidth, PLATFORM_HEIGHT);
        platforms.add(platform);
        world.createBody(platform.getBodyDef()).createFixture(platform.getFixtureDef());

        // HINDERNIS HINZUFÜGEN: Zufällig Hindernisse auf Plattformen platzieren
        if (MathUtils.random() < OBSTACLE_CHANCE) {
            float obstacleX = newX + MathUtils.random(30, platformWidth - 30);
            Obstacle obstacle = new Obstacle(obstacleX, newY + PLATFORM_HEIGHT, OBSTACLE_SIZE, OBSTACLE_SIZE);
            obstacles.add(obstacle);
        }
    }

    @Override
    public void render(float delta) {
        // Call parent render for basic game functionality
        super.render(delta);

        // Update score based on horizontal distance traveled
        scoreTimer += delta;
        if (scoreTimer >= SCORE_INTERVAL) {
            float currentX = player.getX();
            
            // Only increase score if player has moved right
            if (currentX > lastPlayerX) {
                float distanceDiff = currentX - lastPlayerX;
                score += distanceDiff;
                lastPlayerX = currentX;
            }
            
            scoreTimer = 0;
        }

        // Check if we need to create new platforms
        if (camera.position.x + SPAWN_DISTANCE > platforms.get(platforms.size - 1).getX()) {
            createNewPlatform();
        }

        // Clean up old objects
        cleanupOldObjects();

        // Draw score with UI camera (fixed position)
        String scoreText = "Score: " + (int)score;
        GlyphLayout layout = new GlyphLayout(font, scoreText);
        float textWidth = layout.width;
        float textHeight = layout.height;
        float padding = 20f;
        float bgX = 30;
        float bgY = Gdx.graphics.getHeight() - textHeight - padding * 1.5f;

        // Only draw the text, no background
        scoreBatch.setProjectionMatrix(uiCamera.combined);
        scoreBatch.begin();
        font.setColor(1, 1, 0, 1); // Bright yellow
        font.draw(scoreBatch, scoreText, bgX + padding, bgY + textHeight + (padding / 2));
        scoreBatch.end();
    }

    private void cleanupOldObjects() {
        // AUFRÄUMEN: Entferne Objekte die zu weit hinter dem Spieler sind
        float cleanupX = camera.position.x + CLEANUP_DISTANCE;

        // PLATTFORMEN LÖSCHEN: Entferne alte Plattformen um Speicher zu sparen
        for (int i = platforms.size - 1; i >= 0; i--) {
            Platform platform = platforms.get(i);
            if (platform.getRect().x + platform.getRect().width < cleanupX) {
                platforms.removeIndex(i);
            }
        }

        // HINDERNISSE LÖSCHEN: Entferne alte Hindernisse um Speicher zu sparen
        for (int i = obstacles.size - 1; i >= 0; i--) {
            Obstacle obstacle = obstacles.get(i);
            if (obstacle.getRect().x + obstacle.getRect().width < cleanupX) {
                obstacles.removeIndex(i);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        uiCamera.setToOrtho(false, width, height);
    }

    @Override
    public void resetGame() {
        // Instead of resetting, show game over screen
        game.setScreen(new GameOverScreen(game, score));
    }

    @Override
    public void dispose() {
        super.dispose();
        font.dispose();
        scoreBatch.dispose();
        uiShapeRenderer.dispose();
    }
}
