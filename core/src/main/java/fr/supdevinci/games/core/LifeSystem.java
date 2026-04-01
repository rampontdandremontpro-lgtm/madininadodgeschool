package fr.supdevinci.games.core;

import fr.supdevinci.games.model.Homework;

public class LifeSystem {

    private static final int START_LIVES = 3;

    private int lives;
    private int maxLives;

    public LifeSystem() {
        reset();
    }

    public void reset() {
        maxLives = START_LIVES;
        lives = START_LIVES;
    }

    public void applyHomeworkEffect(Homework homework) {
        if (homework.isSnack()) {
            lives = Math.min(lives + 1, maxLives);
            return;
        }

        if (homework.isRareItem()) {
            maxLives = 5;
            lives = 5;
            return;
        }

        lives -= homework.getDamage();

        if (lives < 0) {
            lives = 0;
        }
    }

    public int getLives() {
        return lives;
    }

    public int getMaxLives() {
        return maxLives;
    }

    public boolean isGameOver() {
        return lives <= 0;
    }
}