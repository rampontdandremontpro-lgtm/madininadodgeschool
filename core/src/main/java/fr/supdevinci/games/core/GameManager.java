package fr.supdevinci.games.core;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import fr.supdevinci.games.model.Homework;
import fr.supdevinci.games.model.Player;

public class GameManager implements GameLogic {

    private static final int START_LIVES = 3;

    private static final float HIT_COOLDOWN_MAX = 1f;
    private static final float LEVEL_TRANSITION_DURATION = 2.2f;

    private static final int SECONDE_DURATION = 30;
    private static final int PREMIERE_DURATION = 40;
    private static final int REVISION_DURATION = 10;
    private static final int TERMINALE_DURATION = 50;

    private static final int PREMIERE_START = SECONDE_DURATION; // 30
    private static final int REVISION_START = SECONDE_DURATION + PREMIERE_DURATION; // 70
    private static final int TERMINALE_START = SECONDE_DURATION + PREMIERE_DURATION + REVISION_DURATION; // 80
    private static final int TOTAL_RUN_DURATION = SECONDE_DURATION + PREMIERE_DURATION + REVISION_DURATION + TERMINALE_DURATION; // 130

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
    private int maxLives;
    private int bestScore;

    private int lastPhaseIndex;
    private String levelTransitionMessage;

    public GameManager(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        this.player = new Player(worldWidth / 2f - 25f, 40f, 50f, 70f, 420f);
        this.homeworks = new Array<>();

        reset();
    }

    @Override
    public void reset() {
        player.reset(worldWidth / 2f - 25f, 40f);
        homeworks.clear();

        spawnTimer = 0f;
        spawnDelay = 1.0f;
        survivalTime = 0f;
        hitCooldown = 0f;
        levelTransitionTimer = 0f;

        levelTransitionMessage = "";
        lastPhaseIndex = 0;

        maxLives = START_LIVES;
        lives = START_LIVES;
    }

    @Override
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

        checkPhaseTransition();
        updateDifficulty();

        spawnTimer += delta;
        if (spawnTimer >= spawnDelay) {
            spawnWave();
            spawnTimer = 0f;
        }

        updateHomeworks(delta);
        checkCollisions();
    }

    private void checkPhaseTransition() {
        int currentPhaseIndex = getPhaseIndex();

        if (currentPhaseIndex > lastPhaseIndex) {
            lastPhaseIndex = currentPhaseIndex;
            levelTransitionTimer = LEVEL_TRANSITION_DURATION;

            if (currentPhaseIndex == 1) {
                levelTransitionMessage = "PASSAGE EN 1ERE";
            } else if (currentPhaseIndex == 2) {
                levelTransitionMessage = "MODE REVISION";
            } else if (currentPhaseIndex == 3) {
                levelTransitionMessage = "PASSAGE EN TERMINALE";
            }

            homeworks.clear();
        }
    }

    private void updateDifficulty() {
        Phase phase = getCurrentPhase();

        switch (phase) {
            case SECONDE:
                spawnDelay = 0.90f;
                break;
            case PREMIERE:
                spawnDelay = 0.60f;
                break;
            case REVISION:
                spawnDelay = 1.15f;
                break;
            case TERMINALE:
                spawnDelay = 0.38f;
                break;
            case FINISHED:
            default:
                spawnDelay = 999f;
                break;
        }
    }

    private void spawnWave() {
        Phase phase = getCurrentPhase();

        if (phase == Phase.REVISION) {
            spawnRevisionWave();
            return;
        }

        spawnHomework();

        switch (phase) {
            case SECONDE:
                if (MathUtils.randomBoolean(0.30f)) {
                    spawnHomework();
                }
                break;

            case PREMIERE:
                if (MathUtils.randomBoolean(0.55f)) {
                    spawnHomework();
                }
                if (MathUtils.randomBoolean(0.25f)) {
                    spawnHomework();
                }
                break;

            case TERMINALE:
                if (MathUtils.randomBoolean(0.75f)) {
                    spawnHomework();
                }
                if (MathUtils.randomBoolean(0.50f)) {
                    spawnHomework();
                }
                if (MathUtils.randomBoolean(0.30f)) {
                    spawnHomework();
                }
                break;

            default:
                break;
        }
    }

    private void spawnRevisionWave() {
        spawnSnackOrRare();

        if (MathUtils.randomBoolean(0.55f)) {
            spawnSnackOrRare();
        }

        if (MathUtils.randomBoolean(0.20f)) {
            spawnSnackOrRare();
        }
    }

    private void spawnSnackOrRare() {
        int roll = MathUtils.random(0, 99);
        Homework.HomeworkType type;

        if (roll < 45) {
            type = Homework.HomeworkType.POMME_CANNELLE;
        } else if (roll < 90) {
            type = Homework.HomeworkType.PATE_GOYAVE;
        } else {
            type = Homework.HomeworkType.CARESSE_RARE;
        }

        spawnHomeworkByType(type);
    }

    private void spawnHomework() {
        Homework.HomeworkType type = randomTypeByPhase();
        spawnHomeworkByType(type);
    }

    private void spawnHomeworkByType(Homework.HomeworkType type) {
        float width;
        float height;
        float speed;

        Phase phase = getCurrentPhase();

        if (type == Homework.HomeworkType.POMME_CANNELLE) {
            width = 62f;
            height = 62f;
            speed = 180f;
        } else if (type == Homework.HomeworkType.PATE_GOYAVE) {
            width = 64f;
            height = 52f;
            speed = 180f;
        } else if (type == Homework.HomeworkType.CARESSE_RARE) {
            width = 48f;
            height = 72f;
            speed = 170f;
        } else if (phase == Phase.SECONDE) {
            switch (type) {
                case NOTEBOOK:
                    width = 60f;
                    height = 70f;
                    speed = 225f;
                    break;
                case TEST:
                    width = 48f;
                    height = 58f;
                    speed = 265f;
                    break;
                case GIANT_PEN:
                    width = 28f;
                    height = 120f;
                    speed = 250f;
                    break;
                case COPY:
                default:
                    width = 58f;
                    height = 68f;
                    speed = 235f;
                    break;
            }
        } else if (phase == Phase.PREMIERE) {
            switch (type) {
                case NOTEBOOK:
                    width = 66f;
                    height = 76f;
                    speed = 285f;
                    break;
                case TEST:
                    width = 52f;
                    height = 62f;
                    speed = 335f;
                    break;
                case GIANT_PEN:
                    width = 32f;
                    height = 134f;
                    speed = 320f;
                    break;
                case COPY:
                default:
                    width = 62f;
                    height = 72f;
                    speed = 290f;
                    break;
            }
        } else {
            switch (type) {
                case NOTEBOOK:
                    width = 70f;
                    height = 80f;
                    speed = 340f;
                    break;
                case TEST:
                    width = 54f;
                    height = 64f;
                    speed = 410f;
                    break;
                case GIANT_PEN:
                    width = 34f;
                    height = 145f;
                    speed = 395f;
                    break;
                case COPY:
                default:
                    width = 64f;
                    height = 74f;
                    speed = 350f;
                    break;
            }
        }

        float x = MathUtils.random(0f, worldWidth - width);
        float y = worldHeight;

        homeworks.add(new Homework(x, y, width, height, speed, type));
    }

    private Homework.HomeworkType randomTypeByPhase() {
        int roll = MathUtils.random(0, 99);
        Phase phase = getCurrentPhase();

        if (phase == Phase.SECONDE) {
            if (roll < 45) return Homework.HomeworkType.COPY;
            if (roll < 80) return Homework.HomeworkType.NOTEBOOK;
            if (roll < 95) return Homework.HomeworkType.TEST;
            return Homework.HomeworkType.GIANT_PEN;
        }

        if (phase == Phase.PREMIERE) {
            if (roll < 24) return Homework.HomeworkType.COPY;
            if (roll < 46) return Homework.HomeworkType.NOTEBOOK;
            if (roll < 73) return Homework.HomeworkType.TEST;
            return Homework.HomeworkType.GIANT_PEN;
        }

        if (roll < 18) return Homework.HomeworkType.COPY;
        if (roll < 36) return Homework.HomeworkType.NOTEBOOK;
        if (roll < 63) return Homework.HomeworkType.TEST;
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
                if (hw.isSnack()) {
                    lives = Math.min(lives + 1, maxLives);
                    homeworks.removeIndex(i);
                    return;
                }

                if (hw.isRareItem()) {
                    maxLives = 5;
                    lives = 5;
                    homeworks.removeIndex(i);
                    return;
                }

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

    private int getPhaseIndex() {
        switch (getCurrentPhase()) {
            case SECONDE:
                return 0;
            case PREMIERE:
                return 1;
            case REVISION:
                return 2;
            case TERMINALE:
                return 3;
            case FINISHED:
            default:
                return 4;
        }
    }

    private Phase getCurrentPhase() {
        int score = getCurrentScore();

        if (score < PREMIERE_START) {
            return Phase.SECONDE;
        }
        if (score < REVISION_START) {
            return Phase.PREMIERE;
        }
        if (score < TERMINALE_START) {
            return Phase.REVISION;
        }
        if (score < TOTAL_RUN_DURATION) {
            return Phase.TERMINALE;
        }
        return Phase.FINISHED;
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
        return lives;
    }

    @Override
    public int getMaxLives() {
        return maxLives;
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
        return lives <= 0;
    }

    @Override
    public boolean isVictory() {
        if (getCurrentScore() >= TOTAL_RUN_DURATION) {
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
        return Math.max(0f, Math.min(1f, getCurrentScore() / (float) TOTAL_RUN_DURATION));
    }

    @Override
    public int getTargetScore() {
        return TOTAL_RUN_DURATION;
    }

    @Override
    public float getDifficultyMultiplier() {
        switch (getCurrentPhase()) {
            case SECONDE:
                return 1.1f;
            case PREMIERE:
                return 1.7f;
            case REVISION:
                return 0.85f;
            case TERMINALE:
                return 2.4f;
            case FINISHED:
            default:
                return 1f;
        }
    }

    @Override
    public String getDifficultyLabel() {
        switch (getCurrentPhase()) {
            case SECONDE:
                return "2nde";
            case PREMIERE:
                return "1ere";
            case REVISION:
                return "Revision";
            case TERMINALE:
                return "Terminale";
            case FINISHED:
            default:
                return "Termine";
        }
    }

    @Override
    public float getLevelTransitionTimer() {
        return levelTransitionTimer;
    }

    @Override
    public String getLevelTransitionMessage() {
        return levelTransitionMessage;
    }

    @Override
    public boolean isRevisionMode() {
        return getCurrentPhase() == Phase.REVISION;
    }

    @Override
    public float getRevisionModeTimer() {
        if (!isRevisionMode()) {
            return 0f;
        }

        return Math.max(0f, TERMINALE_START - survivalTime);
    }

    @Override
    public float getRevisionModeDuration() {
        return REVISION_DURATION;
    }

    private enum Phase {
        SECONDE,
        PREMIERE,
        REVISION,
        TERMINALE,
        FINISHED
    }
}