package fr.supdevinci.games.core;

import fr.supdevinci.games.config.GameConfig;
import fr.supdevinci.games.state.GameState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameSessionTest {

    @Test
    void constructor_shouldInitializeStartStateAndDefaultCharacter() {
        GameManager manager = new GameManager(960f, 540f);
        GameSession session = new GameSession(manager);

        assertEquals(GameState.START, session.getGameState());
        assertTrue(session.isStart());
        assertFalse(session.isPlaying());
        assertEquals(GameConfig.CHARACTER_ALIZEE, session.getSelectedCharacter());
        assertEquals("Alizee", session.getCharacterName());
        assertEquals(0f, session.getStateTime(), 0.001f);
        assertNotNull(session.getCurrentFlowState());
    }

    @Test
    void selectJeremie_shouldChangeSelectedCharacterAndName() {
        GameManager manager = new GameManager(960f, 540f);
        GameSession session = new GameSession(manager);

        session.selectJeremie();

        assertEquals(GameConfig.CHARACTER_JEREMIE, session.getSelectedCharacter());
        assertEquals("Jeremie", session.getCharacterName());
    }

    @Test
    void selectAlizee_shouldRestoreSelectedCharacterAndName() {
        GameManager manager = new GameManager(960f, 540f);
        GameSession session = new GameSession(manager);

        session.selectJeremie();
        session.selectAlizee();

        assertEquals(GameConfig.CHARACTER_ALIZEE, session.getSelectedCharacter());
        assertEquals("Alizee", session.getCharacterName());
    }

    @Test
    void addStateTime_shouldIncreaseStateTime() {
        GameManager manager = new GameManager(960f, 540f);
        GameSession session = new GameSession(manager);

        session.addStateTime(1.5f);
        session.addStateTime(0.5f);

        assertEquals(2.0f, session.getStateTime(), 0.001f);
    }

    @Test
    void resetStateTime_shouldSetStateTimeToZero() {
        GameManager manager = new GameManager(960f, 540f);
        GameSession session = new GameSession(manager);

        session.addStateTime(3.2f);
        session.resetStateTime();

        assertEquals(0f, session.getStateTime(), 0.001f);
    }

    @Test
    void restartGame_shouldSwitchToPlayingAndResetManager() {
        GameManager manager = new GameManager(960f, 540f);
        GameSession session = new GameSession(manager);

        session.addStateTime(4f);
        session.restartGame();

        assertEquals(GameState.PLAYING, session.getGameState());
        assertTrue(session.isPlaying());
        assertFalse(session.isStart());
        assertEquals(0f, session.getStateTime(), 0.001f);

        assertEquals(3, manager.getLives());
        assertEquals(0, manager.getCurrentScore());
        assertFalse(manager.isGameOver());
        assertFalse(manager.isVictory());
    }

    @Test
    void backToMenu_shouldSwitchToStartAndResetManager() {
        GameManager manager = new GameManager(960f, 540f);
        GameSession session = new GameSession(manager);

        session.restartGame();
        session.addStateTime(2f);
        session.backToMenu();

        assertEquals(GameState.START, session.getGameState());
        assertTrue(session.isStart());
        assertFalse(session.isPlaying());
        assertEquals(0f, session.getStateTime(), 0.001f);

        assertEquals(3, manager.getLives());
        assertEquals(0, manager.getCurrentScore());
    }

    @Test
    void goToPaused_shouldSwitchToPaused() {
        GameManager manager = new GameManager(960f, 540f);
        GameSession session = new GameSession(manager);

        session.goToPaused();

        assertEquals(GameState.PAUSED, session.getGameState());
        assertTrue(session.isPaused());
        assertFalse(session.isPlaying());
    }

    @Test
    void goToPlaying_shouldSwitchToPlaying() {
        GameManager manager = new GameManager(960f, 540f);
        GameSession session = new GameSession(manager);

        session.goToPlaying();

        assertEquals(GameState.PLAYING, session.getGameState());
        assertTrue(session.isPlaying());
        assertFalse(session.isPaused());
    }

    @Test
    void goToGameOver_shouldSwitchToGameOver() {
        GameManager manager = new GameManager(960f, 540f);
        GameSession session = new GameSession(manager);

        session.goToGameOver();

        assertEquals(GameState.GAME_OVER, session.getGameState());
        assertTrue(session.isGameOver());
        assertFalse(session.isVictory());
    }

    @Test
    void goToVictory_shouldSwitchToVictory() {
        GameManager manager = new GameManager(960f, 540f);
        GameSession session = new GameSession(manager);

        session.goToVictory();

        assertEquals(GameState.VICTORY, session.getGameState());
        assertTrue(session.isVictory());
        assertFalse(session.isGameOver());
    }

    @Test
    void changingState_shouldResetStateTime() {
        GameManager manager = new GameManager(960f, 540f);
        GameSession session = new GameSession(manager);

        session.addStateTime(5f);
        session.goToPlaying();

        assertEquals(0f, session.getStateTime(), 0.001f);
    }

    @Test
void addStateTime_withZero_shouldKeepSameValue() {
    GameManager manager = new GameManager(960f, 540f);
    GameSession session = new GameSession(manager);

    session.addStateTime(0f);

    assertEquals(0f, session.getStateTime(), 0.001f);
}

@Test
void goToPaused_calledTwice_shouldStayPaused() {
    GameManager manager = new GameManager(960f, 540f);
    GameSession session = new GameSession(manager);

    session.goToPaused();
    session.goToPaused();

    assertTrue(session.isPaused());
    assertEquals(GameState.PAUSED, session.getGameState());
}

@Test
void goToVictory_shouldResetStateTimeEvenAfterTimeWasAdded() {
    GameManager manager = new GameManager(960f, 540f);
    GameSession session = new GameSession(manager);

    session.addStateTime(12f);
    session.goToVictory();

    assertTrue(session.isVictory());
    assertEquals(0f, session.getStateTime(), 0.001f);
}

@Test
void restartGame_shouldKeepSelectedCharacter() {
    GameManager manager = new GameManager(960f, 540f);
    GameSession session = new GameSession(manager);

    session.selectJeremie();
    session.restartGame();

    assertEquals("Jeremie", session.getCharacterName());
    assertEquals(GameConfig.CHARACTER_JEREMIE, session.getSelectedCharacter());
}
}