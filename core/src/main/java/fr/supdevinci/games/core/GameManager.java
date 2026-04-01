package fr.supdevinci.games.core;

import com.badlogic.gdx.utils.Array;
import fr.supdevinci.games.model.Homework;
import fr.supdevinci.games.model.Player;

public class GameManager implements GameLogic {

    private static final float HIT_COOLDOWN_MAX = 1f;

    private final float worldWidth;
    private final float worldHeight;

    private final Player player;
    private final Array<Homework> homeworks;
    private final HomeworkFactory homeworkFactory;
    private final GamePhaseTimeline phaseTimeline;
    private final WaveSpawner waveSpawner;
    private final LifeSystem lifeSystem;

    private float spawnTimer;
    private float survivalTime;
    private float hitCooldown;

    private int bestScore;

    public GameManager(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        this.player = new Player(worldWidth / 2f - 25f, 40f, 50f, 70f, 420f);
        this.homeworks = new Array<>();
        this.homeworkFactory = new HomeworkFactory();
        this.phaseTimeline = new GamePhaseTimeline();
        this.waveSpawner = new WaveSpawner(homeworkFactory, worldWidth, worldHeight);
        this.lifeSystem = new LifeSystem();

        reset();
    }

    @Override
    public void reset() {
        player.reset(worldWidth / 2f - 25f, 40f);
        homeworks.clear();

        spawnTimer = 0f;
        survivalTime = 0f;
        hitCooldown = 0f;

        phaseTimeline.reset();
        lifeSystem.reset();
    }

    @Override
    public void update(float delta) {
        if (isGameOver() || isVictory()) {
            return;
        }

        player.update(delta, worldWidth);
        survivalTime += delta;

        updateHitCooldown(delta);
        phaseTimeline.update(delta, survivalTime);

        spawnTimer += delta;
        if (spawnTimer >= getCurrentPhase().getSpawnDelay()) {
            waveSpawner.spawnWave(getCurrentPhase(), homeworks);
            spawnTimer = 0f;
        }

        updateHomeworks(delta);
        checkCollisions();
    }

    private void updateHitCooldown(float delta) {
        if (hitCooldown > 0f) {
            hitCooldown -= delta;
            if (hitCooldown < 0f) {
                hitCooldown = 0f;
            }
        }
    }

    private void updateHomeworks(float delta) {
        for (int i = homeworks.size - 1; i >= 0; i--) {
            Homework hw = homeworks.get(i);
            hw.update(delta);

            if (hw.isOutOfScreen()) {
                homeworks.removeIndex(i);
            }
        }
    }

    private void checkCollisions() {
        if (hitCooldown > 0f) {
            return;
        }

        for (int i = homeworks.size - 1; i >= 0; i--) {
            Homework hw = homeworks.get(i);

            if (hw.getHitbox().overlaps(player.getHitbox())) {
                boolean harmful = !hw.isSnack() && !hw.isRareItem();

                lifeSystem.applyHomeworkEffect(hw);
                homeworks.removeIndex(i);

                if (harmful) {
                    hitCooldown = HIT_COOLDOWN_MAX;
                }

                if (isGameOver()) {
                    updateBestScore();
                }

                return;
            }
        }
    }

    private void updateBestScore() {
        int currentScore = getCurrentScore();
        if (currentScore > bestScore) {
            bestScore = currentScore;
        }
    }

    private GamePhase getCurrentPhase() {
        return phaseTimeline.getCurrentPhase(survivalTime);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Array<Homework> getHomeworks() {
        return homeworks;
    }

    @Override
    public int getLives() {
        return lifeSystem.getLives();
    }

    @Override
    public int getMaxLives() {
        return lifeSystem.getMaxLives();
    }

    @Override
    public int getCurrentScore() {
        return Math.max(0, (int) survivalTime);
    }

    @Override
    public int getBestScore() {
        return bestScore;
    }

    @Override
    public boolean isGameOver() {
        return lifeSystem.isGameOver();
    }

    @Override
    public boolean isVictory() {
        if (getCurrentScore() >= phaseTimeline.getTargetDuration()) {
            updateBestScore();
            return true;
        }
        return false;
    }

    @Override
    public boolean isPlayerInvulnerable() {
        return hitCooldown > 0f;
    }

    @Override
    public float getHitCooldownRatio() {
        return hitCooldown / HIT_COOLDOWN_MAX;
    }

    @Override
    public float getProgressRatio() {
        return Math.max(0f, Math.min(1f, getCurrentScore() / (float) phaseTimeline.getTargetDuration()));
    }

    @Override
    public int getTargetScore() {
        return phaseTimeline.getTargetDuration();
    }

    @Override
    public float getDifficultyMultiplier() {
        return getCurrentPhase().getDifficultyMultiplier();
    }

    @Override
    public String getDifficultyLabel() {
        return getCurrentPhase().getLabel();
    }

    @Override
    public float getLevelTransitionTimer() {
        return phaseTimeline.getTransitionTimer();
    }

    @Override
    public String getLevelTransitionMessage() {
        return phaseTimeline.getTransitionMessage();
    }

    @Override
    public boolean isRevisionMode() {
        return getCurrentPhase().isRevision();
    }

    @Override
    public float getRevisionModeTimer() {
        return phaseTimeline.getRevisionTimeRemaining(survivalTime);
    }

    @Override
    public float getRevisionModeDuration() {
        return GamePhase.REVISION.getDurationSeconds();
    }
}