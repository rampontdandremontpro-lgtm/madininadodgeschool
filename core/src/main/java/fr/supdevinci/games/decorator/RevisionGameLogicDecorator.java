package fr.supdevinci.games.decorator;

import fr.supdevinci.games.core.GameLogic;

public class RevisionGameLogicDecorator extends GameLogicDecorator {

    public RevisionGameLogicDecorator(GameLogic wrapped) {
        super(wrapped);
    }

    @Override
    public String getDifficultyLabel() {
        if (wrapped.isRevisionMode()) {
            return "Revision";
        }
        return wrapped.getDifficultyLabel();
    }

    @Override
    public float getDifficultyMultiplier() {
        if (wrapped.isRevisionMode()) {
            return 0.75f;
        }
        return wrapped.getDifficultyMultiplier();
    }

    @Override
    public String getLevelTransitionMessage() {
        if (wrapped.isRevisionMode()) {
            return "MODE REVISION";
        }
        return wrapped.getLevelTransitionMessage();
    }
}