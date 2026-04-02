package fr.supdevinci.games.model;

import com.badlogic.gdx.math.Rectangle;

/**
 * Représente un objet qui tombe (devoir, stylo, bonus…).
 */
public class Homework {

    public enum HomeworkType {
        COPY,
        NOTEBOOK,
        TEST,
        GIANT_PEN,
        POMME_CANNELLE,
        PATE_GOYAVE,
        CARESSE_RARE
    }

    private final Rectangle bounds;
    private final Rectangle hitbox;

    private final float speed;
    private final HomeworkType type;

    public Homework(float x, float y, float width, float height, float speed, HomeworkType type) {
        this.bounds = new Rectangle(x, y, width, height);
        this.hitbox = createHitbox(x, y, width, height, type);
        this.speed = speed;
        this.type = type;
    }

    private Rectangle createHitbox(float x, float y, float width, float height, HomeworkType type) {
        switch (type) {
            case GIANT_PEN:
                return new Rectangle(x + width * 0.20f, y + height * 0.08f, width * 0.60f, height * 0.84f);

            case TEST:
                return new Rectangle(x + 5f, y + 5f, width - 10f, height - 10f);

            case POMME_CANNELLE:
            case PATE_GOYAVE:
                return new Rectangle(x + width * 0.12f, y + height * 0.12f, width * 0.76f, height * 0.76f);

            case CARESSE_RARE:
                return new Rectangle(x + width * 0.10f, y + height * 0.10f, width * 0.80f, height * 0.80f);

            case COPY:
            case NOTEBOOK:
            default:
                return new Rectangle(x + 4f, y + 4f, width - 8f, height - 8f);
        }
    }

    /**
 * Met à jour la position de l'objet (chute).
 *
 * @param delta Temps écoulé
 */
    public void update(float delta) {
        bounds.y -= speed * delta;
        hitbox.y -= speed * delta;
    }

    public boolean isOutOfScreen() {
        return bounds.y + bounds.height < 0f;
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

    public HomeworkType getType() {
        return type;
    }

    public int getDamage() {
        if (type == HomeworkType.GIANT_PEN) {
            return 2;
        }

        if (isSnack() || isRareItem()) {
            return 0;
        }

        return 1;
    }

    public boolean isSnack() {
        return type == HomeworkType.POMME_CANNELLE || type == HomeworkType.PATE_GOYAVE;
    }

    public boolean isRareItem() {
        return type == HomeworkType.CARESSE_RARE;
    }
}