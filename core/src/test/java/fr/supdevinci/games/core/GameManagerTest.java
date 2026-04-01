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
        assertEquals(3, manager.getMaxLives());
        assertEquals(0, manager.getCurrentScore());
        assertEquals(0f, manager.getProgressRatio(), 0.001f);
        assertFalse(manager.isGameOver());
        assertFalse(manager.isVictory());
        assertEquals(130, manager.getTargetScore());
        assertEquals("2nde", manager.getDifficultyLabel());
        assertFalse(manager.isRevisionMode());
    }

    @Test
    void difficulty_shouldBe2nde_whenTimeIsBelow30() throws Exception {
        GameManager manager = new GameManager(960f, 540f);

        setPrivateFloat(manager, "survivalTime", 10f);

        assertEquals(10, manager.getCurrentScore());
        assertEquals("2nde", manager.getDifficultyLabel());
        assertEquals(1.1f, manager.getDifficultyMultiplier(), 0.001f);
        assertFalse(manager.isRevisionMode());
    }

    @Test
    void difficulty_shouldStillBe2nde_whenTimeIsExactly29() throws Exception {
        GameManager manager = new GameManager(960f, 540f);

        setPrivateFloat(manager, "survivalTime", 29f);

        assertEquals(29, manager.getCurrentScore());
        assertEquals("2nde", manager.getDifficultyLabel());
        assertFalse(manager.isRevisionMode());
    }

    @Test
    void difficulty_shouldBecome1ere_whenTimeIsExactly30() throws Exception {
        GameManager manager = new GameManager(960f, 540f);

        setPrivateFloat(manager, "survivalTime", 30f);

        assertEquals(30, manager.getCurrentScore());
        assertEquals("1ere", manager.getDifficultyLabel());
        assertEquals(1.7f, manager.getDifficultyMultiplier(), 0.001f);
        assertFalse(manager.isRevisionMode());
    }

    @Test
    void difficulty_shouldStay1ere_whenTimeIsExactly69() throws Exception {
        GameManager manager = new GameManager(960f, 540f);

        setPrivateFloat(manager, "survivalTime", 69f);

        assertEquals(69, manager.getCurrentScore());
        assertEquals("1ere", manager.getDifficultyLabel());
        assertFalse(manager.isRevisionMode());
    }

    @Test
    void difficulty_shouldBecomeRevision_whenTimeIsExactly70() throws Exception {
        GameManager manager = new GameManager(960f, 540f);

        setPrivateFloat(manager, "survivalTime", 70f);

        assertEquals(70, manager.getCurrentScore());
        assertEquals("Revision", manager.getDifficultyLabel());
        assertEquals(0.85f, manager.getDifficultyMultiplier(), 0.001f);
        assertTrue(manager.isRevisionMode());
    }

    @Test
    void revision_shouldStillBeActive_whenTimeIsExactly79() throws Exception {
        GameManager manager = new GameManager(960f, 540f);

        setPrivateFloat(manager, "survivalTime", 79f);

        assertEquals(79, manager.getCurrentScore());
        assertEquals("Revision", manager.getDifficultyLabel());
        assertTrue(manager.isRevisionMode());
    }

    @Test
    void difficulty_shouldBecomeTerminale_whenTimeIsExactly80() throws Exception {
        GameManager manager = new GameManager(960f, 540f);

        setPrivateFloat(manager, "survivalTime", 80f);

        assertEquals(80, manager.getCurrentScore());
        assertEquals("Terminale", manager.getDifficultyLabel());
        assertEquals(2.4f, manager.getDifficultyMultiplier(), 0.001f);
        assertFalse(manager.isRevisionMode());
    }

    @Test
    void progressRatio_shouldBeZero_whenTimeIsZero() {
        GameManager manager = new GameManager(960f, 540f);

        assertEquals(0f, manager.getProgressRatio(), 0.001f);
    }

    @Test
    void progressRatio_shouldBeHalf_whenTimeIsSixtyFive() throws Exception {
        GameManager manager = new GameManager(960f, 540f);

        setPrivateFloat(manager, "survivalTime", 65f);

        assertEquals(65, manager.getCurrentScore());
        assertEquals(0.5f, manager.getProgressRatio(), 0.001f);
    }

    @Test
    void isVictory_shouldBeFalse_whenTimeIsBelowTarget() throws Exception {
        GameManager manager = new GameManager(960f, 540f);

        setPrivateFloat(manager, "survivalTime", 129f);

        assertFalse(manager.isVictory());
    }

    @Test
    void isVictory_shouldBeTrue_whenTimeIsExactlyTarget() throws Exception {
        GameManager manager = new GameManager(960f, 540f);

        setPrivateFloat(manager, "survivalTime", 130f);

        assertTrue(manager.isVictory());
        assertEquals(1.0f, manager.getProgressRatio(), 0.001f);
    }

    @Test
    void revisionModeTimer_shouldReturnTenAtRevisionStart() throws Exception {
        GameManager manager = new GameManager(960f, 540f);

        setPrivateFloat(manager, "survivalTime", 70f);

        assertTrue(manager.isRevisionMode());
        assertEquals(10f, manager.getRevisionModeTimer(), 0.001f);
        assertEquals(10f, manager.getRevisionModeDuration(), 0.001f);
    }

    @Test
    void revisionModeTimer_shouldReturnFive_whenFiveSecondsRemain() throws Exception {
        GameManager manager = new GameManager(960f, 540f);

        setPrivateFloat(manager, "survivalTime", 75f);

        assertTrue(manager.isRevisionMode());
        assertEquals(5f, manager.getRevisionModeTimer(), 0.001f);
    }

    @Test
    void negativeSurvivalTime_shouldClampScoreAndProgressToZero() throws Exception {
        GameManager manager = new GameManager(960f, 540f);

        setPrivateFloat(manager, "survivalTime", -10f);

        assertEquals(0, manager.getCurrentScore());
        assertEquals(0f, manager.getProgressRatio(), 0.001f);
        assertEquals("2nde", manager.getDifficultyLabel());
    }

    private void setPrivateFloat(Object target, String fieldName, float value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.setFloat(target, value);
    }
}