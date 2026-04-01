package fr.supdevinci.games.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import fr.supdevinci.games.core.GameSession;

public class StartFlowState implements GameFlowState {

    private final GameSession session;

    public StartFlowState(GameSession session) {
        this.session = session;
    }

    @Override
    public void update(float delta) {
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