package fr.supdevinci.games.decorator;

import com.badlogic.gdx.utils.Array;
import fr.supdevinci.games.core.GameLogic;
import fr.supdevinci.games.model.Homework;
import fr.supdevinci.games.model.Player;

public abstract class GameLogicDecorator implements GameLogic {

    protected final GameLogic wrapped;

    protected GameLogicDecorator(GameLogic wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void reset() {
        wrapped.reset();
    }

    @Override
    public void update(float delta) {
        wrapped.update(delta);
    }

    @Override
    public Player getPlayer() {
        return wrapped.getPlayer();
    }

    @Override
    public Array<Homework> getHomeworks() {
        return wrapped.getHomeworks();
    }

    @Override
    public int getLives() {
        return wrapped.getLives();
    }

    @Override
    public int getMaxLives() {
        return wrapped.getMaxLives();
    }

    @Override
    public int getCurrentScore() {
        return wrapped.getCurrentScore();
    }

    @Override
    public int getBestScore() {
        return wrapped.getBestScore();
    }

    @Override
    public boolean isGameOver() {
        return wrapped.isGameOver();
    }

    @Override
    public boolean isVictory() {
        return wrapped.isVictory();
    }

    @Override
    public boolean isPlayerInvulnerable() {
        return wrapped.isPlayerInvulnerable();
    }

    @Override
    public float getHitCooldownRatio() {
        return wrapped.getHitCooldownRatio();
    }

    @Override
    public float getProgressRatio() {
        return wrapped.getProgressRatio();
    }

    @Override
    public int getTargetScore() {
        return wrapped.getTargetScore();
    }

    @Override
    public float getDifficultyMultiplier() {
        return wrapped.getDifficultyMultiplier();
    }

    @Override
    public String getDifficultyLabel() {
        return wrapped.getDifficultyLabel();
    }

    @Override
    public float getLevelTransitionTimer() {
        return wrapped.getLevelTransitionTimer();
    }

    @Override
    public String getLevelTransitionMessage() {
        return wrapped.getLevelTransitionMessage();
    }

    @Override
    public boolean isRevisionMode() {
        return wrapped.isRevisionMode();
    }

    @Override
    public float getRevisionModeTimer() {
        return wrapped.getRevisionModeTimer();
    }

    @Override
    public float getRevisionModeDuration() {
        return wrapped.getRevisionModeDuration();
    }
}