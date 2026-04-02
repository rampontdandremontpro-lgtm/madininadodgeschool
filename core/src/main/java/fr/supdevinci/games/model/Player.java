package fr.supdevinci.games.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;

/**
 * Représente le joueur.
 * Gère les déplacements, la direction et la hitbox.
 */
public class Player {

    public enum Direction {
        LEFT,
        RIGHT
    }

    private final Rectangle bounds;
    private final Rectangle hitbox;

    private final float defaultSpeed;
    private float speed;

    private Direction direction;
    private boolean moving;

    public Player(float x, float y, float width, float height, float speed) {
        this.bounds = new Rectangle(x, y, width, height);
        this.hitbox = new Rectangle();

        this.defaultSpeed = speed;
        this.speed = speed;

        this.direction = Direction.RIGHT;
        this.moving = false;

        updateHitbox();
    }
    /**
    * Met à jour la position du joueur en fonction des entrées clavier.
    *
    * @param delta Temps écoulé
    * @param worldWidth Largeur du monde (limites)
    */

    public void update(float delta, float worldWidth) {
        float moveX = 0f;
        moving = false;

        boolean moveLeft =
            Gdx.input.isKeyPressed(Input.Keys.LEFT) ||
            Gdx.input.isKeyPressed(Input.Keys.Q) ||
            Gdx.input.isKeyPressed(Input.Keys.A);

        boolean moveRight =
            Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
            Gdx.input.isKeyPressed(Input.Keys.D);

        if (moveLeft && !moveRight) {
            moveX = -speed * delta;
            direction = Direction.LEFT;
            moving = true;
        } else if (moveRight && !moveLeft) {
            moveX = speed * delta;
            direction = Direction.RIGHT;
            moving = true;
        }

        bounds.x += moveX;
        clampToWorld(worldWidth);
        updateHitbox();
    }

    private void clampToWorld(float worldWidth) {
        if (bounds.x < 0f) {
            bounds.x = 0f;
        }

        if (bounds.x + bounds.width > worldWidth) {
            bounds.x = worldWidth - bounds.width;
        }
    }

    private void updateHitbox() {
        hitbox.set(
            bounds.x + 8f,
            bounds.y + 4f,
            bounds.width - 16f,
            bounds.height - 8f
        );
    }

    public void reset(float x, float y) {
        bounds.setPosition(x, y);
        speed = defaultSpeed;
        direction = Direction.RIGHT;
        moving = false;
        updateHitbox();
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public float getX() {
        return bounds.x;
    }

    public float getY() {
        return bounds.y;
    }

    public float getWidth() {
        return bounds.width;
    }

    public float getHeight() {
        return bounds.height;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isMoving() {
        return moving;
    }
}