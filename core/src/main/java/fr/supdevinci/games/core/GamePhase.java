package fr.supdevinci.games.core;

public enum GamePhase {
    SECONDE("2nde", 30, 0.90f, 1.1f),
    PREMIERE("1ere", 40, 0.60f, 1.7f),
    REVISION("Revision", 10, 1.15f, 0.85f),
    TERMINALE("Terminale", 50, 0.38f, 2.4f),
    FINISHED("Termine", 0, 999f, 1f);

    private final String label;
    private final int durationSeconds;
    private final float spawnDelay;
    private final float difficultyMultiplier;

    GamePhase(String label, int durationSeconds, float spawnDelay, float difficultyMultiplier) {
        this.label = label;
        this.durationSeconds = durationSeconds;
        this.spawnDelay = spawnDelay;
        this.difficultyMultiplier = difficultyMultiplier;
    }

    public String getLabel() {
        return label;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public float getSpawnDelay() {
        return spawnDelay;
    }

    public float getDifficultyMultiplier() {
        return difficultyMultiplier;
    }

    public boolean isRevision() {
        return this == REVISION;
    }
}