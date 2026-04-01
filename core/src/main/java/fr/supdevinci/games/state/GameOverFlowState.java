package fr.supdevinci.games.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import fr.supdevinci.games.core.GameSession;

public class GameOverFlowState implements GameFlowState {

    private final GameSession session;

    public GameOverFlowState(GameSession session) {
        this.session = session;
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            session.restartGame();
        }
    }
}
