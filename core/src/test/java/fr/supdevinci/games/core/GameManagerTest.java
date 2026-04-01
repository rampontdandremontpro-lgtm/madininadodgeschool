package fr.supdevinci.games.core;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameManagerTest {

    @Test
    void reset_shouldRestoreInitialValues() {
        GameManager manager = new GameManager(960f, 540f);

        manager.reset();

        assertEquals(3, manager.getLives());
        assertEquals(0, manager.getCurrentScore());
        assertEquals(0f, manager.getProgressRatio(), 0.001f);
        assertFalse(manager.isGameOver());
        assertFalse(manager.isVictory());
        assertEquals(60, manager.getTargetScore());
        assertEquals("2nde", manager.getDifficultyLabel());
    }

    @Test
    void difficulty_shouldBe2nde_whenScoreIsBelow20() throws Exception {
        GameManager manager = new GameManager(960f, 540f);

        setPrivateFloat(manager, "survivalTime", 10f);

        assertEquals(10, manager.getCurrentScore());
        assertEquals("2nde", manager.getDifficultyLabel());
        assertEquals(1.0f, manager.getDifficultyMultiplier(), 0.001f);
    }

    @Test
    void difficulty_shouldBe1ere_whenScoreIsBetween20And39() throws Exception {
        GameManager manager = new GameManager(960f, 540f);

        setPrivateFloat(manager, "survivalTime", 30f);

        assertEquals(30, manager.getCurrentScore());
        assertEquals("1ere", manager.getDifficultyLabel());
        assertEquals(1.5f, manager.getDifficultyMultiplier(), 0.001f);
    }

    @Test
    void difficulty_shouldBeTerminale_whenScoreIs40OrMore() throws Exception {
        GameManager manager = new GameManager(960f, 540f);

        setPrivateFloat(manager, "survivalTime", 50f);

        assertEquals(50, manager.getCurrentScore());
        assertEquals("Terminale", manager.getDifficultyLabel());
        assertEquals(2.0f, manager.getDifficultyMultiplier(), 0.001f);
    }

    @Test
    void progressRatio_shouldReachOne_whenTargetIsReached() throws Exception {
        GameManager manager = new GameManager(960f, 540f);

        setPrivateFloat(manager, "survivalTime", 60f);

        assertEquals(60, manager.getCurrentScore());
        assertEquals(1.0f, manager.getProgressRatio(), 0.001f);
        assertTrue(manager.isVictory());
    }

    private void setPrivateFloat(Object target, String fieldName, float value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.setFloat(target, value);
    }
}