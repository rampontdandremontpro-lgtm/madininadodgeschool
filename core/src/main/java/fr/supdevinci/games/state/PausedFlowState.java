package fr.supdevinci.games.state;

import fr.supdevinci.games.core.GameSession;

public class PausedFlowState implements GameFlowState {

    public PausedFlowState(GameSession session) {
    }

    @Override
    public void update(float delta) {
        // Rien pendant la pause
    }
}