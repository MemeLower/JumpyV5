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
    float walkSpeed = 250; // Slower walking speed
    float runSpeed = 500;  // Current speed for running
    float jumpVelocity = 600; // Lower jump height
    float yVelocity = 0;
    float gravity = 1500;
    boolean onGround = false;
    boolean facingRight = true;
    GameScreen gameScreen;
    
    // Character scaling
    private static final float CHARACTER_SCALE = 2.0f; // Reduced from 3.0f to 2.0f
    private static final int SPRITE_WIDTH = 48; // Original sprite width
    private static final int SPRITE_HEIGHT = 48; // Original sprite height
    
    // Hitbox adjustments - make collision box smaller and align bottom
    private static final float HITBOX_WIDTH_RATIO = 0.35f; // Much narrower
    private static final float HITBOX_HEIGHT_RATIO = 0.6f; // Much shorter

    enum State { 
        IDLE, 
        WALK, 
        RUN, 
        JUMP, 
        FALL, 
        LAND,
        HURT,
        DEATH
    }
    private State state = State.IDLE;
    private State previousState = State.IDLE;

    // Animation textures
    private Texture idleSheet, walkSheet, runSheet, jumpSheet, fallSheet, landSheet, hurtSheet, deathSheet;
    
    // Animations
    private Animation<TextureRegion> idleAnim, walkAnim, runAnim, jumpAnim, fallAnim, landAnim, hurtAnim, deathAnim;
    private float animTime = 0f;
    
    // Animation frame counts (adjust based on your actual spritesheets)
    private static final int IDLE_FRAMES = 8;
    private static final int WALK_FRAMES = 8;
    private static final int RUN_FRAMES = 8;
    private static final int JUMP_FRAMES = 4;
    private static final int FALL_FRAMES = 2;
    private static final int LAND_FRAMES = 4;
    private static final int HURT_FRAMES = 4;
    private static final int DEATH_FRAMES = 8;
    
    // Animation frame duration (in seconds)
    private static final float IDLE_DURATION = 0.12f;
    private static final float WALK_DURATION = 0.1f;
    private static final float RUN_DURATION = 0.08f;
    private static final float JUMP_DURATION = 0.15f;
    private static final float FALL_DURATION = 0.2f;
    private static final float LAND_DURATION = 0.1f;
    private static final float HURT_DURATION = 0.15f;
    private static final float DEATH_DURATION = 0.2f;

    private static final int FEET_OFFSET_PIXELS = 8; // Fine-tuned for perfect feet alignment

    private float deathTimer = 0f;
    private static final float DEATH_ANIMATION_DURATION = 2.0f; // Increased for testing
    private boolean isDead = false;
    private boolean justFell = false;
    private boolean justDiedToObstacle = false;

    public Player(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        
        // Calculate visual and collision dimensions
        float visualWidth = SPRITE_WIDTH * CHARACTER_SCALE;
        float visualHeight = SPRITE_HEIGHT * CHARACTER_SCALE;
        float hitboxWidth = visualWidth * HITBOX_WIDTH_RATIO;
        float hitboxHeight = visualHeight * HITBOX_HEIGHT_RATIO;
        
        // Align hitbox bottom with visual sprite bottom
        float hitboxX = 150 + (visualWidth - hitboxWidth) / 2;
        float hitboxY = 200; // Y is at the bottom of the sprite
        
        // Set rectangle size based on hitbox dimensions
        rect = new Rectangle(hitboxX, hitboxY, hitboxWidth, hitboxHeight);

        loadAnimations();
    }
    
    private void loadAnimations() {
        try {
            // Load sprite sheets
            idleSheet = new Texture(Gdx.files.internal("character animations/Idle/Player Idle 48x48.png"));
            walkSheet = new Texture(Gdx.files.internal("character animations/Walk/PlayerWalk 48x48.png"));
            runSheet = new Texture(Gdx.files.internal("character animations/Run/player run 48x48.png"));
            jumpSheet = new Texture(Gdx.files.internal("character animations/Jump/player jump 48x48.png"));
            landSheet = new Texture(Gdx.files.internal("character animations/Land/player land 48x48.png"));
            hurtSheet = new Texture(Gdx.files.internal("character animations/Hurt-Damaged/Player Hurt 48x48.png"));
            deathSheet = new Texture(Gdx.files.internal("character animations/Death/Player Death 48x48.png"));
            
            // Use fall animation from jump sheet for now, or create a separate fall animation
            fallSheet = jumpSheet; // Temporary - you might want to create a dedicated fall animation
            
            // Split into frames with automatic frame count detection
            TextureRegion[] idleFrames = splitSpritesheet(idleSheet, 48, 48);
            TextureRegion[] walkFrames = splitSpritesheet(walkSheet, 48, 48);
            TextureRegion[] runFrames = splitSpritesheet(runSheet, 48, 48);
            TextureRegion[] jumpFrames = splitSpritesheet(jumpSheet, 48, 48);
            TextureRegion[] fallFrames = splitSpritesheet(fallSheet, 48, 48);
            TextureRegion[] landFrames = splitSpritesheet(landSheet, 48, 48);
            TextureRegion[] hurtFrames = splitSpritesheet(hurtSheet, 48, 48);
            TextureRegion[] deathFrames = splitSpritesheet(deathSheet, 48, 48); // Death uses 48x48
            System.out.println("Death animation frames loaded: " + deathFrames.length);

            // Create animations
            idleAnim = new Animation<>(IDLE_DURATION, idleFrames);
            walkAnim = new Animation<>(WALK_DURATION, walkFrames);
            runAnim = new Animation<>(RUN_DURATION, runFrames);
            jumpAnim = new Animation<>(JUMP_DURATION, jumpFrames);
            fallAnim = new Animation<>(FALL_DURATION, fallFrames);
            landAnim = new Animation<>(LAND_DURATION, landFrames);
            hurtAnim = new Animation<>(HURT_DURATION, hurtFrames);
            deathAnim = new Animation<>(DEATH_DURATION, deathFrames);
            
        } catch (Exception e) {
            System.err.println("Error loading animations: " + e.getMessage());
            // Fallback to old animations if new ones fail to load
            loadFallbackAnimations();
        }
    }
    
    /**
     * Automatically splits a spritesheet into frames based on frame dimensions
     * @param sheet The spritesheet texture
     * @param frameWidth Width of each frame
     * @param frameHeight Height of each frame
     * @return Array of TextureRegions representing each frame
     */
    private TextureRegion[] splitSpritesheet(Texture sheet, int frameWidth, int frameHeight) {
        int framesPerRow = sheet.getWidth() / frameWidth;
        int framesPerCol = sheet.getHeight() / frameHeight;
        int totalFrames = framesPerRow * framesPerCol;
        
        TextureRegion[][] temp = TextureRegion.split(sheet, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[totalFrames];
        
        int index = 0;
        for (int row = 0; row < framesPerCol; row++) {
            for (int col = 0; col < framesPerRow; col++) {
                frames[index++] = temp[row][col];
            }
        }
        
        return frames;
    }
    
    private void loadFallbackAnimations() {
        // Fallback to original animations
        idleSheet = new Texture(Gdx.files.internal("IDLE.png"));
        runSheet = new Texture(Gdx.files.internal("RUN.png"));
        hurtSheet = new Texture(Gdx.files.internal("HURT.png"));
        
        TextureRegion[] idleFrames = TextureRegion.split(idleSheet, idleSheet.getWidth() / 10, idleSheet.getHeight())[0];
        TextureRegion[] runFrames = TextureRegion.split(runSheet, runSheet.getWidth() / 16, runSheet.getHeight())[0];
        TextureRegion[] hurtFrames = TextureRegion.split(hurtSheet, hurtSheet.getWidth() / 4, hurtSheet.getHeight())[0];

        idleAnim = new Animation<>(0.12f, idleFrames);
        runAnim = new Animation<>(0.08f, runFrames);
        hurtAnim = new Animation<>(0.15f, hurtFrames);
        
        // Set other animations to idle as fallback
        walkAnim = idleAnim;
        jumpAnim = idleAnim;
        fallAnim = idleAnim;
        landAnim = idleAnim;
        deathAnim = idleAnim;
    }

    public void update(float delta, Array<Platform> platforms) {
        animTime += delta;
        previousState = state;

        // If dead, only update death animation and timer
        if (isDead) {
            deathTimer += delta;
            if (deathTimer >= DEATH_ANIMATION_DURATION) {
                gameScreen.resetGame();
            }
            return;
        }

        boolean movingLeft = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean movingRight = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean running = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
        if (movingLeft) facingRight = false;
        if (movingRight) facingRight = true;
        float visualWidth = SPRITE_WIDTH * CHARACTER_SCALE;
        float visualHeight = SPRITE_HEIGHT * CHARACTER_SCALE;
        float hitboxWidth = visualWidth * HITBOX_WIDTH_RATIO;
        float hitboxHeight = visualHeight * HITBOX_HEIGHT_RATIO;
        float visualX = rect.x - (visualWidth - hitboxWidth) / 2;
        float visualY = rect.y;
        float moveSpeed = running ? runSpeed : walkSpeed;
        if (movingLeft) visualX -= moveSpeed * delta;
        if (movingRight) visualX += moveSpeed * delta;
        rect.x = visualX + (visualWidth - hitboxWidth) / 2;
        yVelocity -= gravity * delta;
        rect.y += yVelocity * delta;
        onGround = false;
        for (Platform platform : platforms) {
            if (yVelocity <= 0 && 
                rect.y > platform.getRect().y + platform.getRect().height - 15 &&
                rect.y + yVelocity * delta <= platform.getRect().y + platform.getRect().height &&
                rect.x + rect.width > platform.getRect().x + 5 &&
                rect.x < platform.getRect().x + platform.getRect().width - 5) {
                rect.y = platform.getRect().y + platform.getRect().height;
                yVelocity = 0;
                onGround = true;
                break;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && onGround) {
            yVelocity = jumpVelocity;
            onGround = false;
        }
        // If fallen off screen, trigger death
        if (rect.y < -100) {
            triggerDeath();
        }
        updateState(movingLeft, movingRight, running);
    }
    
    private void updateState(boolean movingLeft, boolean movingRight, boolean running) {
        if (!onGround) {
            if (yVelocity > 0) {
                state = State.JUMP;
            } else {
                state = State.FALL;
            }
        } else {
            if (movingLeft || movingRight) {
                if (running) {
                    state = State.RUN;
                } else {
                    state = State.WALK;
                }
            } else {
                state = State.IDLE;
            }
        }
        if (state != previousState) {
            animTime = 0f;
        }
    }

    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = getCurrentAnimationFrame();
        
        // Calculate visual and collision dimensions
        float visualWidth = SPRITE_WIDTH * CHARACTER_SCALE;
        float visualHeight = SPRITE_HEIGHT * CHARACTER_SCALE;
        float hitboxWidth = visualWidth * HITBOX_WIDTH_RATIO;
        float hitboxHeight = visualHeight * HITBOX_HEIGHT_RATIO;
        
        // Align visual sprite so its feet match the hitbox bottom
        float visualX = rect.x - (visualWidth - hitboxWidth) / 2;
        float visualY = rect.y - FEET_OFFSET_PIXELS * CHARACTER_SCALE;
        
        // Draw the frame with scaling at visual position
        if (facingRight) {
            batch.draw(currentFrame, visualX, visualY, visualWidth, visualHeight);
        } else {
            batch.draw(currentFrame, visualX + visualWidth, visualY, -visualWidth, visualHeight);
        }
        drawHitbox(batch);
    }
    
    /**
     * Draws the collision hitbox for testing purposes
     */
    private void drawHitbox(SpriteBatch batch) {
        // Note: This method will be called from within the SpriteBatch rendering
        // The actual hitbox drawing should be done separately in the GameScreen render method
        // For now, we'll just store the hitbox info for the GameScreen to draw
    }
    
    /**
     * Draws the visual hitbox - should be called from GameScreen after SpriteBatch.end()
     */
    public void drawHitbox(ShapeRenderer shapeRenderer) {
        if (shapeRenderer != null) {
            shapeRenderer.setColor(1, 0, 0, 1); // Red color
            shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
    }
    
    private TextureRegion getCurrentAnimationFrame() {
        Animation<TextureRegion> anim = null;
        boolean loop = true;
        float time = animTime;
        switch (state) {
            case IDLE: anim = idleAnim; loop = true; break;
            case WALK: anim = walkAnim; loop = true; break;
            case RUN: anim = runAnim; loop = true; break;
            case JUMP: anim = jumpAnim; loop = false; break;
            case FALL: anim = fallAnim; loop = true; break;
            case LAND: anim = landAnim; loop = false; break;
            case HURT: anim = hurtAnim; loop = true; break;
            case DEATH:
                anim = deathAnim;
                loop = false;
                float deathDuration = (deathAnim != null) ? deathAnim.getAnimationDuration() : 0f;
                time = Math.min(animTime, Math.max(0f, deathDuration - 0.0001f));
                break;
            default: anim = idleAnim; loop = true; break;
        }
        // Defensive: fallback if anim is null or has no frames
        if (anim == null || anim.getKeyFrames() == null || anim.getKeyFrames().length == 0) {
            return idleAnim.getKeyFrame(0, true);
        }
        return anim.getKeyFrame(time, loop);
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
    
    public boolean isFacingRight() {
        return facingRight;
    }
    
    public State getState() {
        return state;
    }

    public void dispose() {
        if (idleSheet != null) idleSheet.dispose();
        if (walkSheet != null) walkSheet.dispose();
        if (runSheet != null) runSheet.dispose();
        if (jumpSheet != null) jumpSheet.dispose();
        if (fallSheet != null && fallSheet != jumpSheet) fallSheet.dispose();
        if (landSheet != null) landSheet.dispose();
        if (hurtSheet != null) hurtSheet.dispose();
        if (deathSheet != null) deathSheet.dispose();
    }

    public void triggerDeath() {
        if (!isDead) {
            System.out.println("Playing death animation");
            state = State.DEATH;
            animTime = 0f;
            deathTimer = 0f;
            isDead = true;
            if (rect.y < -100) {
                justFell = true;
            } else {
                justDiedToObstacle = true;
            }
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public void resetState() {
        isDead = false;
        state = State.IDLE;
        deathTimer = 0f;
        animTime = 0f;
    }

    public boolean justFell() {
        return justFell;
    }
    public void clearJustFell() {
        justFell = false;
    }

    public boolean justDiedToObstacle() {
        return justDiedToObstacle;
    }
    public void clearJustDiedToObstacle() {
        justDiedToObstacle = false;
    }
}
