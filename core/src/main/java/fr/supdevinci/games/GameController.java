package fr.supdevinci.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class GameController {

    private final GameSession session;

    public GameController(GameSession session) {
        this.session = session;
    }

    public void update(float delta) {
        handleGlobalInputs();

        switch (session.getGameState()) {
            case START:
                updateStartState();
                break;

            case PLAYING:
                session.getGameManager().update(delta);

                if (session.getGameManager().isVictory()) {
                    session.setGameState(GameState.VICTORY);
                } else if (session.getGameManager().isGameOver()) {
                    session.setGameState(GameState.GAME_OVER);
                }
                break;

            case PAUSED:
                break;

            case GAME_OVER:
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    session.restartGame();
                }
                break;

            case VICTORY:
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    session.backToMenu();
                }
                break;
        }
    }

    private void handleGlobalInputs() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            session.backToMenu();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            if (session.isPlaying()) {
                session.setGameState(GameState.PAUSED);
            } else if (session.isPaused()) {
                session.setGameState(GameState.PLAYING);
            }
        }
    }

    private void updateStartState() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            session.selectAlizee();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            session.selectJeremie();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            session.restartGame();
        }
    }
}