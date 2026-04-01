package fr.supdevinci.games.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import fr.supdevinci.games.core.GameSession;

public class VictoryFlowState implements GameFlowState {

    private final GameSession session;

    public VictoryFlowState(GameSession session) {
        this.session = session;
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            session.backToMenu();
        }
    }
}
