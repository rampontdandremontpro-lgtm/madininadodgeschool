package fr.supdevinci.games;

public class GameSession {

    private final GameManager gameManager;

    private GameState gameState;
    private int selectedCharacter;
    private String characterName;
    private float stateTime;

    public GameSession(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameState = GameState.START;
        this.selectedCharacter = GameConfig.CHARACTER_ALIZEE;
        this.characterName = "Alizee";
        this.stateTime = 0f;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getSelectedCharacter() {
        return selectedCharacter;
    }

    public String getCharacterName() {
        return characterName;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void addStateTime(float delta) {
        stateTime += delta;
    }

    public void resetStateTime() {
        stateTime = 0f;
    }

    public void selectAlizee() {
        selectedCharacter = GameConfig.CHARACTER_ALIZEE;
        characterName = "Alizee";
    }

    public void selectJeremie() {
        selectedCharacter = GameConfig.CHARACTER_JEREMIE;
        characterName = "Jeremie";
    }

    public boolean isStart() {
        return gameState == GameState.START;
    }

    public boolean isPlaying() {
        return gameState == GameState.PLAYING;
    }

    public boolean isPaused() {
        return gameState == GameState.PAUSED;
    }

    public boolean isGameOver() {
        return gameState == GameState.GAME_OVER;
    }

    public boolean isVictory() {
        return gameState == GameState.VICTORY;
    }

    public void restartGame() {
        gameManager.reset();
        stateTime = 0f;
        gameState = GameState.PLAYING;
    }

    public void backToMenu() {
        gameManager.reset();
        stateTime = 0f;
        gameState = GameState.START;
    }
}