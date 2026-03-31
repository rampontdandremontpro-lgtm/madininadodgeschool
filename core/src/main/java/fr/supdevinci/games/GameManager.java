package fr.supdevinci.games;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class GameManager {

    private static final int START_LIVES = 3;
    private static final int VICTORY_SCORE = 60;

    private static final float START_SPAWN_DELAY = 1.05f;
    private static final float MIN_SPAWN_DELAY = 0.32f;

    private static final float HIT_COOLDOWN_DURATION = 1.0f;

    private final float worldWidth;
    private final float worldHeight;

    private final Player player;
    private final Array<Homework> homeworks;

    private float spawnTimer;
    private float spawnDelay;
    private float survivalTime;
    private float difficultyMultiplier;

    private float hitCooldownTimer;
    private float totalPlayTime;

    private int lives;
    private int bestScore;

    private boolean gameOver;
    private boolean victory;

    public GameManager(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        this.player = new Player(worldWidth / 2f - 25f, 40f, 50f, 70f, 460f);
        this.homeworks = new Array<>();

        reset();
    }

    public void reset() {
        player.reset(worldWidth / 2f - 25f, 40f);
        homeworks.clear();

        spawnTimer = 0f;
        spawnDelay = START_SPAWN_DELAY;
        survivalTime = 0f;
        difficultyMultiplier = 1f;

        hitCooldownTimer = 0f;
        totalPlayTime = 0f;

        lives = START_LIVES;
        gameOver = false;
        victory = false;
    }

    public void update(float delta) {
        if (gameOver || victory) {
            return;
        }

        totalPlayTime += delta;
        survivalTime += delta;

        updateHitCooldown(delta);
        updateDifficulty();

        player.update(delta, worldWidth);

        spawnTimer += delta;
        if (spawnTimer >= spawnDelay) {
            spawnWave();
            spawnTimer = 0f;
        }

        updateHomeworks(delta);
        checkCollisions();
        checkVictory();
    }

    private void updateHitCooldown(float delta) {
        if (hitCooldownTimer > 0f) {
            hitCooldownTimer -= delta;
            if (hitCooldownTimer < 0f) {
                hitCooldownTimer = 0f;
            }
        }
    }

    private void updateDifficulty() {
        int score = getCurrentScore();

        difficultyMultiplier = 1f + (score * 0.04f);
        spawnDelay = Math.max(MIN_SPAWN_DELAY, START_SPAWN_DELAY - score * 0.012f);
    }

    private void spawnWave() {
        spawnHomework();

        int score = getCurrentScore();

        if (score >= 12 && MathUtils.randomBoolean(0.25f)) {
            spawnHomework();
        }

        if (score >= 24 && MathUtils.randomBoolean(0.35f)) {
            spawnHomework();
        }

        if (score >= 36 && MathUtils.randomBoolean(0.45f)) {
            spawnHomework();
        }

        if (score >= 50 && MathUtils.randomBoolean(0.55f)) {
            spawnHomework();
        }
    }

    private void spawnHomework() {
        Homework.HomeworkType type = randomType();

        float width;
        float height;
        float speed;

        switch (type) {
            case NOTEBOOK:
                width = MathUtils.random(46f, 72f);
                height = MathUtils.random(52f, 82f);
                speed = MathUtils.random(220f, 290f) * difficultyMultiplier;
                break;

            case TEST:
                width = MathUtils.random(38f, 62f);
                height = MathUtils.random(44f, 68f);
                speed = MathUtils.random(270f, 360f) * difficultyMultiplier;
                break;

            case GIANT_PEN:
                width = MathUtils.random(22f, 34f);
                height = MathUtils.random(110f, 165f);
                speed = MathUtils.random(320f, 420f) * difficultyMultiplier;
                break;

            case COPY:
            default:
                width = MathUtils.random(42f, 66f);
                height = MathUtils.random(48f, 72f);
                speed = MathUtils.random(230f, 310f) * difficultyMultiplier;
                break;
        }

        float x = MathUtils.random(0f, worldWidth - width);
        float y = worldHeight + MathUtils.random(0f, 40f);

        homeworks.add(new Homework(x, y, width, height, speed, type));
    }

    private Homework.HomeworkType randomType() {
        int score = getCurrentScore();
        int roll = MathUtils.random(0, 99);

        if (score < 20) {
            if (roll < 42) return Homework.HomeworkType.COPY;
            if (roll < 74) return Homework.HomeworkType.NOTEBOOK;
            if (roll < 92) return Homework.HomeworkType.TEST;
            return Homework.HomeworkType.GIANT_PEN;
        }

        if (score < 40) {
            if (roll < 32) return Homework.HomeworkType.COPY;
            if (roll < 60) return Homework.HomeworkType.NOTEBOOK;
            if (roll < 84) return Homework.HomeworkType.TEST;
            return Homework.HomeworkType.GIANT_PEN;
        }

        if (roll < 25) return Homework.HomeworkType.COPY;
        if (roll < 50) return Homework.HomeworkType.NOTEBOOK;
        if (roll < 76) return Homework.HomeworkType.TEST;
        return Homework.HomeworkType.GIANT_PEN;
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
        if (hitCooldownTimer > 0f) {
            return;
        }

        for (int i = homeworks.size - 1; i >= 0; i--) {
            Homework hw = homeworks.get(i);

            if (hw.getHitbox().overlaps(player.getHitbox())) {
                lives -= hw.getDamage();
                if (lives < 0) {
                    lives = 0;
                }

                homeworks.removeIndex(i);
                hitCooldownTimer = HIT_COOLDOWN_DURATION;

                if (lives <= 0) {
                    gameOver = true;
                    updateBestScore();
                }

                return;
            }
        }
    }

    private void checkVictory() {
        if (getCurrentScore() >= VICTORY_SCORE) {
            victory = true;
            updateBestScore();
        }
    }

    private void updateBestScore() {
        int currentScore = getCurrentScore();
        if (currentScore > bestScore) {
            bestScore = currentScore;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Array<Homework> getHomeworks() {
        return homeworks;
    }

    public int getLives() {
        return lives;
    }

    public int getCurrentScore() {
        return (int) survivalTime;
    }

    public int getBestScore() {
        return bestScore;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isVictory() {
        return victory;
    }

    public float getSpawnDelay() {
        return spawnDelay;
    }

    public float getDifficultyMultiplier() {
        return difficultyMultiplier;
    }

    public boolean isPlayerInvulnerable() {
        return hitCooldownTimer > 0f;
    }

    public float getHitCooldownRatio() {
        return hitCooldownTimer / HIT_COOLDOWN_DURATION;
    }

    public float getProgressRatio() {
        return Math.min(1f, getCurrentScore() / (float) VICTORY_SCORE);
    }

    public String getDifficultyLabel() {
        int score = getCurrentScore();

        if (score < 15) return "Calme";
        if (score < 30) return "Tendu";
        if (score < 45) return "Chaotique";
        return "Infernal";
    }

    public int getTargetScore() {
        return VICTORY_SCORE;
    }

    public float getTotalPlayTime() {
        return totalPlayTime;
    }
}