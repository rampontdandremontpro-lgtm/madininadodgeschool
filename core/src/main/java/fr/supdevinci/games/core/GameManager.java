package fr.supdevinci.games.core;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import fr.supdevinci.games.model.Homework;
import fr.supdevinci.games.model.Player;

public class GameManager {

    private static final int START_LIVES = 3;
    private static final int TARGET_SCORE = 60;
    private static final float HIT_COOLDOWN_MAX = 1f;
    private static final float LEVEL_TRANSITION_DURATION = 2.2f;

    private final float worldWidth;
    private final float worldHeight;

    private final Player player;
    private final Array<Homework> homeworks;

    private float spawnTimer;
    private float spawnDelay;
    private float survivalTime;
    private float hitCooldown;

    private float levelTransitionTimer;

    private int lives;
    private int bestScore;
    private int lastLevelIndex;

    private String levelTransitionMessage;

    public GameManager(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        this.player = new Player(worldWidth / 2f - 25f, 40f, 50f, 70f, 420f);
        this.homeworks = new Array<>();

        reset();
    }

    public void reset() {
        player.reset(worldWidth / 2f - 25f, 40f);
        homeworks.clear();

        spawnTimer = 0f;
        spawnDelay = 1.0f;
        survivalTime = 0f;
        hitCooldown = 0f;

        levelTransitionTimer = 0f;
        levelTransitionMessage = "";
        lastLevelIndex = 0;

        lives = START_LIVES;
    }

    public void update(float delta) {
        if (isGameOver() || isVictory()) {
            return;
        }

        player.update(delta, worldWidth);

        survivalTime += delta;

        if (hitCooldown > 0f) {
            hitCooldown -= delta;
            if (hitCooldown < 0f) {
                hitCooldown = 0f;
            }
        }

        if (levelTransitionTimer > 0f) {
            levelTransitionTimer -= delta;
            if (levelTransitionTimer < 0f) {
                levelTransitionTimer = 0f;
                levelTransitionMessage = "";
            }
        }

        checkLevelTransition();
        updateDifficulty();

        spawnTimer += delta;
        if (spawnTimer >= spawnDelay) {
            spawnWave();
            spawnTimer = 0f;
        }

        updateHomeworks(delta);
        checkCollisions();
    }

    private void checkLevelTransition() {
        int currentLevelIndex = getLevelIndex();

        if (currentLevelIndex > lastLevelIndex) {
            lastLevelIndex = currentLevelIndex;
            levelTransitionTimer = LEVEL_TRANSITION_DURATION;

            if (currentLevelIndex == 1) {
                levelTransitionMessage = "PASSAGE EN 1ERE";
            } else if (currentLevelIndex == 2) {
                levelTransitionMessage = "PASSAGE EN TERMINALE";
            }

            homeworks.clear();
        }
    }

    private void updateDifficulty() {
        int score = getCurrentScore();

        if (score < 20) {
            spawnDelay = 1.0f;      // 2nde
        } else if (score < 40) {
            spawnDelay = 0.75f;     // 1ère
        } else {
            spawnDelay = 0.50f;     // Terminale
        }
    }

    private void spawnWave() {
        spawnHomework();

        int score = getCurrentScore();

        if (score < 20) {
            if (MathUtils.randomBoolean(0.20f)) {
                spawnHomework();
            }
            return;
        }

        if (score < 40) {
            if (MathUtils.randomBoolean(0.35f)) {
                spawnHomework();
            }
            if (MathUtils.randomBoolean(0.15f)) {
                spawnHomework();
            }
            return;
        }

        if (MathUtils.randomBoolean(0.50f)) {
            spawnHomework();
        }
        if (MathUtils.randomBoolean(0.35f)) {
            spawnHomework();
        }
        if (MathUtils.randomBoolean(0.20f)) {
            spawnHomework();
        }
    }

    private void spawnHomework() {
        Homework.HomeworkType type = randomTypeByLevel();

        float width;
        float height;
        float speed;

        int score = getCurrentScore();

        if (score < 20) {
            switch (type) {
                case NOTEBOOK:
                    width = 60f;
                    height = 70f;
                    speed = 210f;
                    break;

                case TEST:
                    width = 48f;
                    height = 58f;
                    speed = 250f;
                    break;

                case GIANT_PEN:
                    width = 28f;
                    height = 120f;
                    speed = 240f;
                    break;

                case COPY:
                default:
                    width = 58f;
                    height = 68f;
                    speed = 220f;
                    break;
            }
        } else if (score < 40) {
            switch (type) {
                case NOTEBOOK:
                    width = 64f;
                    height = 74f;
                    speed = 250f;
                    break;

                case TEST:
                    width = 50f;
                    height = 60f;
                    speed = 300f;
                    break;

                case GIANT_PEN:
                    width = 30f;
                    height = 130f;
                    speed = 290f;
                    break;

                case COPY:
                default:
                    width = 60f;
                    height = 70f;
                    speed = 260f;
                    break;
            }
        } else {
            switch (type) {
                case NOTEBOOK:
                    width = 66f;
                    height = 76f;
                    speed = 300f;
                    break;

                case TEST:
                    width = 52f;
                    height = 62f;
                    speed = 360f;
                    break;

                case GIANT_PEN:
                    width = 32f;
                    height = 140f;
                    speed = 350f;
                    break;

                case COPY:
                default:
                    width = 62f;
                    height = 72f;
                    speed = 310f;
                    break;
            }
        }

        float x = MathUtils.random(0f, worldWidth - width);
        float y = worldHeight;

        homeworks.add(new Homework(x, y, width, height, speed, type));
    }

    private Homework.HomeworkType randomTypeByLevel() {
        int roll = MathUtils.random(0, 99);
        int score = getCurrentScore();

        if (score < 20) {
            if (roll < 45) return Homework.HomeworkType.COPY;
            if (roll < 80) return Homework.HomeworkType.NOTEBOOK;
            if (roll < 95) return Homework.HomeworkType.TEST;
            return Homework.HomeworkType.GIANT_PEN;
        }

        if (score < 40) {
            if (roll < 35) return Homework.HomeworkType.COPY;
            if (roll < 65) return Homework.HomeworkType.NOTEBOOK;
            if (roll < 88) return Homework.HomeworkType.TEST;
            return Homework.HomeworkType.GIANT_PEN;
        }

        if (roll < 25) return Homework.HomeworkType.COPY;
        if (roll < 50) return Homework.HomeworkType.NOTEBOOK;
        if (roll < 78) return Homework.HomeworkType.TEST;
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
        if (hitCooldown > 0f) {
            return;
        }

        for (int i = homeworks.size - 1; i >= 0; i--) {
            Homework hw = homeworks.get(i);

            if (hw.getHitbox().overlaps(player.getHitbox())) {
                if (hw.getType() == Homework.HomeworkType.GIANT_PEN) {
                    lives -= 2;
                } else {
                    lives -= 1;
                }

                if (lives < 0) {
                    lives = 0;
                }

                homeworks.removeIndex(i);
                hitCooldown = HIT_COOLDOWN_MAX;

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

    private int getLevelIndex() {
        int score = getCurrentScore();

        if (score < 20) return 0;
        if (score < 40) return 1;
        return 2;
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
        return lives <= 0;
    }

    public boolean isVictory() {
        if (getCurrentScore() >= TARGET_SCORE) {
            updateBestScore();
            return true;
        }
        return false;
    }

    public boolean isPlayerInvulnerable() {
        return hitCooldown > 0f;
    }

    public float getHitCooldownRatio() {
        return hitCooldown / HIT_COOLDOWN_MAX;
    }

    public float getProgressRatio() {
        return Math.min(1f, getCurrentScore() / (float) TARGET_SCORE);
    }

    public int getTargetScore() {
        return TARGET_SCORE;
    }

    public float getDifficultyMultiplier() {
        int score = getCurrentScore();

        if (score < 20) {
            return 1.0f;
        } else if (score < 40) {
            return 1.5f;
        } else {
            return 2.0f;
        }
    }

    public String getDifficultyLabel() {
        int score = getCurrentScore();

        if (score < 20) {
            return "2nde";
        } else if (score < 40) {
            return "1ere";
        } else {
            return "Terminale";
        }
    }

    public float getLevelTransitionTimer() {
        return levelTransitionTimer;
    }

    public String getLevelTransitionMessage() {
        return levelTransitionMessage;
    }
}