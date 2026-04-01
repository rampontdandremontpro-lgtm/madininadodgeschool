package fr.supdevinci.games.core;

public class GamePhaseTimeline {

    private static final float LEVEL_TRANSITION_DURATION = 2.2f;

    private static final GamePhase[] ORDER = {
            GamePhase.SECONDE,
            GamePhase.PREMIERE,
            GamePhase.REVISION,
            GamePhase.TERMINALE
    };

    private float transitionTimer;
    private String transitionMessage;
    private int lastPhaseIndex;

    public GamePhaseTimeline() {
        reset();
    }

    public void reset() {
        transitionTimer = 0f;
        transitionMessage = "";
        lastPhaseIndex = 0;
    }

    public void update(float delta, float survivalTime) {
        if (transitionTimer > 0f) {
            transitionTimer -= delta;
            if (transitionTimer < 0f) {
                transitionTimer = 0f;
                transitionMessage = "";
            }
        }

        int currentPhaseIndex = getPhaseIndex(survivalTime);

        if (currentPhaseIndex > lastPhaseIndex) {
            lastPhaseIndex = currentPhaseIndex;
            transitionTimer = LEVEL_TRANSITION_DURATION;
            transitionMessage = buildTransitionMessage(getCurrentPhase(survivalTime));
        }
    }

    public GamePhase getCurrentPhase(float survivalTime) {
        int elapsed = Math.max(0, (int) survivalTime);
        int total = 0;

        for (GamePhase phase : ORDER) {
            total += phase.getDurationSeconds();
            if (elapsed < total) {
                return phase;
            }
        }

        return GamePhase.FINISHED;
    }

    public int getTargetDuration() {
        int total = 0;
        for (GamePhase phase : ORDER) {
            total += phase.getDurationSeconds();
        }
        return total;
    }

    public float getRevisionTimeRemaining(float survivalTime) {
        GamePhase currentPhase = getCurrentPhase(survivalTime);
        if (!currentPhase.isRevision()) {
            return 0f;
        }

        int revisionStart = getPhaseStart(GamePhase.REVISION);
        int revisionEnd = revisionStart + GamePhase.REVISION.getDurationSeconds();

        return Math.max(0f, revisionEnd - survivalTime);
    }

    public float getTransitionTimer() {
        return transitionTimer;
    }

    public String getTransitionMessage() {
        return transitionMessage;
    }

    private int getPhaseIndex(float survivalTime) {
        GamePhase current = getCurrentPhase(survivalTime);

        for (int i = 0; i < ORDER.length; i++) {
            if (ORDER[i] == current) {
                return i;
            }
        }

        return ORDER.length;
    }

    private int getPhaseStart(GamePhase targetPhase) {
        int total = 0;
        for (GamePhase phase : ORDER) {
            if (phase == targetPhase) {
                return total;
            }
            total += phase.getDurationSeconds();
        }
        return total;
    }

    private String buildTransitionMessage(GamePhase phase) {
        switch (phase) {
            case PREMIERE:
                return "PASSAGE EN 1ERE";
            case REVISION:
                return "MODE REVISION";
            case TERMINALE:
                return "PASSAGE EN TERMINALE";
            default:
                return "";
        }
    }
}