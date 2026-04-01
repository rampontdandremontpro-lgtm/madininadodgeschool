package fr.supdevinci.games.core;

import com.badlogic.gdx.utils.Array;
import fr.supdevinci.games.model.Homework;
import fr.supdevinci.games.model.Player;

public interface GameLogic {

    void reset();

    void update(float delta);

    Player getPlayer();

    Array<Homework> getHomeworks();

    int getLives();

    int getCurrentScore();

    int getBestScore();

    int getMaxLives();

    boolean isRevisionMode();
    
    float getRevisionModeTimer();

    float getRevisionModeDuration();

    boolean isGameOver();

    boolean isVictory();

    boolean isPlayerInvulnerable();

    float getHitCooldownRatio();

    float getProgressRatio();

    int getTargetScore();

    float getDifficultyMultiplier();

    String getDifficultyLabel();

    float getLevelTransitionTimer();

    String getLevelTransitionMessage();

}