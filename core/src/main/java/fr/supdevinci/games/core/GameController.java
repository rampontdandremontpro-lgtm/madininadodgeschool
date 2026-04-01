package fr.supdevinci.games.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class GameController {

    private final GameSession session;

    public GameController(GameSession session) {
        this.session = session;
    }

    public void update(float delta) {
        handleGlobalInputs();
        session.getCurrentFlowState().update(delta);
    }

    private void handleGlobalInputs() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            session.backToMenu();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            if (session.isPlaying()) {
                session.goToPaused();
            } else if (session.isPaused()) {
                session.goToPlaying();
            }
        }
    }
}