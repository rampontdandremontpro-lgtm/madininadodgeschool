package fr.supdevinci.games.core;

import fr.supdevinci.games.config.GameConfig;
import fr.supdevinci.games.state.*;

/**
 * Gère l'état global du jeu (menu, jeu, pause, victoire, game over).
 * Utilise le pattern State pour gérer les transitions.
 */
public class GameSession {

    private final GameLogic gameManager;

    private final GameFlowState startFlowState;
    private final GameFlowState playingFlowState;
    private final GameFlowState pausedFlowState;
    private final GameFlowState gameOverFlowState;
    private final GameFlowState victoryFlowState;

    private GameFlowState currentFlowState;
    private GameState gameState;

    private int selectedCharacter;
    private String characterName;
    private float stateTime;

    public GameSession(GameLogic gameManager) {
        this.gameManager = gameManager;

        this.selectedCharacter = GameConfig.CHARACTER_ALIZEE;
        this.characterName = "Alizee";
        this.stateTime = 0f;

        this.startFlowState = new StartFlowState(this);
        this.playingFlowState = new PlayingFlowState(this);
        this.pausedFlowState = new PausedFlowState(this);
        this.gameOverFlowState = new GameOverFlowState(this);
        this.victoryFlowState = new VictoryFlowState(this);

        this.gameState = GameState.START;
        this.currentFlowState = startFlowState;
    }

    public GameLogic getGameManager() {
        return gameManager;
    }

    public GameState getGameState() {
        return gameState;
    }

    public GameFlowState getCurrentFlowState() {
        return currentFlowState;
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
        changeState(GameState.PLAYING);
    }

    public void backToMenu() {
        gameManager.reset();
        changeState(GameState.START);
    }

    public void goToPlaying() {
        changeState(GameState.PLAYING);
    }

    public void goToPaused() {
        changeState(GameState.PAUSED);
    }

    public void goToGameOver() {
        changeState(GameState.GAME_OVER);
    }

    public void goToVictory() {
        changeState(GameState.VICTORY);
    }
/**
 * Change l'état du jeu.
 *
 * @param newState Nouvel état du jeu
 */
    private void changeState(GameState newState) {
        gameState = newState;
        stateTime = 0f;

        switch (newState) {
            case START:
                currentFlowState = startFlowState;
                break;
            case PLAYING:
                currentFlowState = playingFlowState;
                break;
            case PAUSED:
                currentFlowState = pausedFlowState;
                break;
            case GAME_OVER:
                currentFlowState = gameOverFlowState;
                break;
            case VICTORY:
                currentFlowState = victoryFlowState;
                break;
        }
    }
}