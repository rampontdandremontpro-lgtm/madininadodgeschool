package fr.supdevinci.games.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HomeworkTest {

    @Test
    void update_shouldMoveHomeworkDown() {
        Homework hw = new Homework(100f, 300f, 50f, 60f, 200f, Homework.HomeworkType.COPY);

        hw.update(0.5f);

        assertEquals(200f, hw.getY(), 0.001f);
    }

    @Test
    void isOutOfScreen_shouldReturnFalse_whenHomeworkIsStillVisible() {
        Homework hw = new Homework(100f, 10f, 50f, 60f, 200f, Homework.HomeworkType.COPY);

        assertFalse(hw.isOutOfScreen());
    }

    @Test
    void isOutOfScreen_shouldReturnTrue_whenHomeworkHasLeftScreen() {
        Homework hw = new Homework(100f, -100f, 50f, 60f, 200f, Homework.HomeworkType.COPY);

        assertTrue(hw.isOutOfScreen());
    }

    @Test
    void getDamage_shouldReturnOne_forCopy() {
        Homework hw = new Homework(100f, 300f, 50f, 60f, 200f, Homework.HomeworkType.COPY);

        assertEquals(1, hw.getDamage());
    }
}