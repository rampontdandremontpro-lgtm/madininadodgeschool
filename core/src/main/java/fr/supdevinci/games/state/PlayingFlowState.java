package fr.supdevinci.games.state;

import fr.supdevinci.games.core.GameSession;

public class PlayingFlowState implements GameFlowState {

    private final GameSession session;

    public PlayingFlowState(GameSession session) {
        this.session = session;
    }

    @Override
    public void update(float delta) {
        session.getGameManager().update(delta);

        if (session.getGameManager().isVictory()) {
            session.goToVictory();
        } else if (session.getGameManager().isGameOver()) {
            session.goToGameOver();
        }
    }
}