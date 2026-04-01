package fr.supdevinci.games.model;

import com.badlogic.gdx.math.Rectangle;

public class Homework {

    public enum HomeworkType {
        COPY,
        NOTEBOOK,
        TEST,
        GIANT_PEN
    }

    private final Rectangle bounds;
    private final Rectangle hitbox;

    private float speed;
    private final HomeworkType type;

    public Homework(float x, float y, float width, float height, float speed, HomeworkType type) {
        this.bounds = new Rectangle(x, y, width, height);
        this.hitbox = new Rectangle();
        this.speed = speed;
        this.type = type;

        updateHitbox();
    }

    public void update(float delta) {
        bounds.y -= speed * delta;
        updateHitbox();
    }

    private void updateHitbox() {
        switch (type) {
            case GIANT_PEN:
                hitbox.set(
                    bounds.x + bounds.width * 0.20f,
                    bounds.y + 4f,
                    bounds.width * 0.60f,
                    bounds.height - 8f
                );
                break;

            case TEST:
                hitbox.set(
                    bounds.x + 5f,
                    bounds.y + 5f,
                    bounds.width - 10f,
                    bounds.height - 10f
                );
                break;

            case NOTEBOOK:
                hitbox.set(
                    bounds.x + 4f,
                    bounds.y + 4f,
                    bounds.width - 8f,
                    bounds.height - 8f
                );
                break;

            case COPY:
            default:
                hitbox.set(
                    bounds.x + 4f,
                    bounds.y + 4f,
                    bounds.width - 8f,
                    bounds.height - 8f
                );
                break;
        }
    }

    public boolean isOutOfScreen() {
        return bounds.y + bounds.height < 0f;
    }

    public int getDamage() {
        switch (type) {
            case GIANT_PEN:
                return 1;
            case TEST:
                return 1;
            case NOTEBOOK:
                return 1;
            case COPY:
            default:
                return 1;
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public HomeworkType getType() {
        return type;
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
}