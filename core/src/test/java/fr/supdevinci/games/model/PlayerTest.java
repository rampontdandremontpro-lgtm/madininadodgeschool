package fr.supdevinci.games.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void reset_shouldRestorePositionAndDefaultValues() {
        Player player = new Player(100f, 40f, 50f, 70f, 420f);

        // On modifie l'état
        player.setSpeed(200f);

        // Reset
        player.reset(300f, 80f);

        // Vérifications
        assertEquals(300f, player.getX(), 0.001f);
        assertEquals(80f, player.getY(), 0.001f);
        assertEquals(420f, player.getSpeed(), 0.001f);
        assertEquals(Player.Direction.RIGHT, player.getDirection());
        assertFalse(player.isMoving());
    }

    @Test
    void setSpeed_shouldChangeSpeed() {
        Player player = new Player(100f, 40f, 50f, 70f, 420f);

        player.setSpeed(600f);

        assertEquals(600f, player.getSpeed(), 0.001f);
    }

    @Test
    void hitbox_shouldBeSmallerThanBounds() {
        Player player = new Player(100f, 40f, 50f, 70f, 420f);

        // Vérifie que la hitbox est bien réduite par rapport au sprite
        assertTrue(player.getHitbox().width < player.getBounds().width);
        assertTrue(player.getHitbox().height < player.getBounds().height);
    }

    @Test
    void direction_shouldBeRightAfterReset() {
        Player player = new Player(100f, 40f, 50f, 70f, 420f);

        player.reset(200f, 80f);

        assertEquals(Player.Direction.RIGHT, player.getDirection());
    }
}