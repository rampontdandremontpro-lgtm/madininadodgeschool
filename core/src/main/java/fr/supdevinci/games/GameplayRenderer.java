package fr.supdevinci.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameplayRenderer {

    private final GameAssets assets;
    private final GameSession session;

    public GameplayRenderer(GameAssets assets, GameSession session) {
        this.assets = assets;
        this.session = session;
    }

    public void render() {
        drawBackground();
        drawOverlays();
        drawObjects();
        drawTexts();
    }

    private void drawBackground() {
        SpriteBatch batch = assets.getBatch();
        Texture background = assets.getBackgroundSchoolTexture();

        batch.begin();
        batch.setColor(Color.WHITE);
        batch.draw(background, 0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        if (session.isPlaying()) {
            float pulse = 0.03f + 0.02f * (float) Math.sin(session.getStateTime() * 2.2f);
            batch.setColor(0f, 0f, 0f, pulse);
            batch.draw(background, 0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        }

        batch.setColor(Color.WHITE);
        batch.end();
    }

    private void drawOverlays() {
        ShapeRenderer shape = assets.getShapeRenderer();
        GameManager gm = session.getGameManager();

        shape.begin(ShapeRenderer.ShapeType.Filled);

        if (session.isPlaying() || session.isPaused()) {
            shape.setColor(new Color(0.03f, 0.07f, 0.15f, 0.76f));
            shape.rect(15, 365, 360, 160);

            shape.setColor(new Color(0.03f, 0.07f, 0.15f, 0.76f));
            shape.rect(710, 420, 235, 105);

            drawProgressBarBackground(18, 338, 354, 16);
            drawDifficultyBarBackground(728, 438, 190, 12);
            drawLivesBoxes(728, 470);
        }

        if (session.isPaused()) {
            shape.setColor(new Color(0.03f, 0.07f, 0.15f, 0.60f));
            shape.rect(0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

            shape.setColor(new Color(0.98f, 0.98f, 0.99f, 0.94f));
            shape.rect(250, 180, 460, 150);
        }

        if (session.isGameOver() || session.isVictory()) {
            shape.setColor(new Color(0.03f, 0.07f, 0.15f, 0.62f));
            shape.rect(0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

            shape.setColor(new Color(0.98f, 0.98f, 0.99f, 0.94f));
            shape.rect(170, 140, 620, 210);
        }

        if (session.isPlaying() && gm.isPlayerInvulnerable()) {
            float alpha = 0.04f + 0.10f * gm.getHitCooldownRatio();
            shape.setColor(new Color(1f, 0f, 0f, alpha));
            shape.rect(0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        }

        if (session.isPlaying() && gm.getLives() == 1) {
            float alpha = 0.14f + 0.05f * (float) Math.sin(session.getStateTime() * 7f);
            shape.setColor(new Color(0.80f, 0.10f, 0.10f, alpha));
            shape.rect(300, 500, 360, 24);
        }

        shape.end();

        if (session.isPlaying() || session.isPaused()) {
            drawProgressBarFill(20, 340, 350, 12);
            drawDifficultyBarFill(730, 440, 186, 8);
        }
    }

    private void drawProgressBarBackground(float x, float y, float width, float height) {
        ShapeRenderer shape = assets.getShapeRenderer();
        shape.setColor(new Color(0.80f, 0.84f, 0.90f, 0.95f));
        shape.rect(x, y, width, height);
    }

    private void drawProgressBarFill(float x, float y, float width, float height) {
        float progress = session.getGameManager().getProgressRatio();

        ShapeRenderer shape = assets.getShapeRenderer();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(new Color(1.00f, 0.66f, 0.18f, 1f));
        shape.rect(x, y, width * progress, height);
        shape.end();
    }

    private void drawDifficultyBarBackground(float x, float y, float width, float height) {
        ShapeRenderer shape = assets.getShapeRenderer();
        shape.setColor(new Color(0.75f, 0.79f, 0.88f, 0.95f));
        shape.rect(x, y, width, height);
    }

    private void drawDifficultyBarFill(float x, float y, float width, float height) {
        float value = Math.min(1f, (session.getGameManager().getDifficultyMultiplier() - 1f) / 2.2f);

        ShapeRenderer shape = assets.getShapeRenderer();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(new Color(0.91f, 0.20f, 0.16f, 1f));
        shape.rect(x, y, width * value, height);
        shape.end();
    }

    private void drawLivesBoxes(float x, float y) {
        ShapeRenderer shape = assets.getShapeRenderer();
        int lives = session.getGameManager().getLives();

        for (int i = 0; i < 3; i++) {
            if (i < lives) {
                shape.setColor(new Color(0.92f, 0.18f, 0.18f, 1f));
            } else {
                shape.setColor(new Color(0.42f, 0.45f, 0.52f, 0.85f));
            }

            shape.rect(x + i * 28f, y, 20f, 20f);
        }
    }

    private void drawObjects() {
        SpriteBatch batch = assets.getBatch();
        GameManager gm = session.getGameManager();
        Player player = gm.getPlayer();

        batch.begin();
        drawPlayerSprite(player);
        drawNameAbovePlayer(player);
        batch.end();

        ShapeRenderer shape = assets.getShapeRenderer();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        for (Homework hw : gm.getHomeworks()) {
            drawHomework(hw);
        }
        shape.end();
    }

    private void drawPlayerSprite(Player player) {
        if (session.getGameManager().isPlayerInvulnerable()) {
            boolean visible = ((int) (session.getStateTime() * 14f)) % 2 == 0;
            if (!visible) {
                return;
            }
        }

        SpriteBatch batch = assets.getBatch();
        TextureRegion region = assets.getCharacterRegion(session.getSelectedCharacter());

        float x = player.getX() - GameConfig.PLAYER_DRAW_OFFSET_X;
        float y = player.getY();

        if (player.isMoving()) {
            y += (float) Math.sin(session.getStateTime() * 14f) * 2f;
        }

        batch.setColor(Color.WHITE);
        if (player.getDirection() == Player.Direction.LEFT) {
            batch.draw(region, x + GameConfig.PLAYER_DRAW_WIDTH, y, -GameConfig.PLAYER_DRAW_WIDTH, GameConfig.PLAYER_DRAW_HEIGHT);
        } else {
            batch.draw(region, x, y, GameConfig.PLAYER_DRAW_WIDTH, GameConfig.PLAYER_DRAW_HEIGHT);
        }
    }

    private void drawNameAbovePlayer(Player player) {
        String label = session.getCharacterName();

        assets.getGlyphLayout().setText(assets.getSmallFont(), label);
        float textX = player.getX() + (player.getWidth() / 2f) - (assets.getGlyphLayout().width / 2f);
        float textY = player.getY() + GameConfig.PLAYER_DRAW_HEIGHT + 18f;

        assets.getSmallFont().setColor(Color.WHITE);
        assets.getSmallFont().draw(assets.getBatch(), label, textX, textY);
    }

    private void drawHomework(Homework hw) {
        switch (hw.getType()) {
            case NOTEBOOK:
                drawNotebook(hw);
                break;
            case TEST:
                drawTest(hw);
                break;
            case GIANT_PEN:
                drawGiantPen(hw);
                break;
            case COPY:
            default:
                drawCopy(hw);
                break;
        }
    }

    private void drawCopy(Homework hw) {
        ShapeRenderer shape = assets.getShapeRenderer();

        shape.setColor(new Color(0.99f, 0.99f, 0.99f, 1f));
        shape.rect(hw.getX(), hw.getY(), hw.getWidth(), hw.getHeight());

        shape.setColor(new Color(0.82f, 0.12f, 0.17f, 1f));
        shape.rect(hw.getX() + 5f, hw.getY() + hw.getHeight() - 10f, hw.getWidth() - 10f, 5f);

        shape.setColor(new Color(0.75f, 0.82f, 0.95f, 1f));
        shape.rect(hw.getX() + 5f, hw.getY() + 12f, hw.getWidth() - 10f, 2f);
        shape.rect(hw.getX() + 5f, hw.getY() + 20f, hw.getWidth() - 10f, 2f);
        shape.rect(hw.getX() + 5f, hw.getY() + 28f, hw.getWidth() - 10f, 2f);
    }

    private void drawNotebook(Homework hw) {
        ShapeRenderer shape = assets.getShapeRenderer();

        shape.setColor(new Color(0.18f, 0.42f, 0.88f, 1f));
        shape.rect(hw.getX(), hw.getY(), hw.getWidth(), hw.getHeight());

        shape.setColor(new Color(0.96f, 0.96f, 0.98f, 1f));
        shape.rect(hw.getX() + 8f, hw.getY() + 6f, hw.getWidth() - 12f, hw.getHeight() - 12f);

        shape.setColor(new Color(0.78f, 0.12f, 0.18f, 1f));
        shape.rect(hw.getX() + 4f, hw.getY(), 4f, hw.getHeight());
    }

    private void drawTest(Homework hw) {
        ShapeRenderer shape = assets.getShapeRenderer();

        shape.setColor(new Color(1f, 0.97f, 0.97f, 1f));
        shape.rect(hw.getX(), hw.getY(), hw.getWidth(), hw.getHeight());

        shape.setColor(new Color(0.85f, 0.05f, 0.12f, 1f));
        shape.rect(hw.getX() + 7f, hw.getY() + hw.getHeight() - 12f, hw.getWidth() - 14f, 6f);

        shape.setColor(new Color(0.85f, 0.05f, 0.12f, 1f));
        shape.rect(hw.getX() + hw.getWidth() - 18f, hw.getY() + 10f, 10f, 10f);

        shape.setColor(new Color(0.75f, 0.82f, 0.95f, 1f));
        shape.rect(hw.getX() + 6f, hw.getY() + 18f, hw.getWidth() - 22f, 2f);
        shape.rect(hw.getX() + 6f, hw.getY() + 28f, hw.getWidth() - 22f, 2f);
    }

    private void drawGiantPen(Homework hw) {
        ShapeRenderer shape = assets.getShapeRenderer();

        float x = hw.getX();
        float y = hw.getY();
        float w = hw.getWidth();
        float h = hw.getHeight();

        shape.setColor(new Color(0.12f, 0.32f, 0.88f, 1f));
        shape.rect(x, y + 12f, w, h - 24f);

        shape.setColor(new Color(0.95f, 0.92f, 0.82f, 1f));
        shape.rect(x + 3f, y + 24f, w - 6f, h - 48f);

        shape.setColor(new Color(0.05f, 0.10f, 0.35f, 1f));
        shape.rect(x, y + h - 16f, w, 16f);

        shape.setColor(new Color(0.78f, 0.80f, 0.84f, 1f));
        shape.rect(x, y + h - 24f, w, 8f);

        shape.setColor(new Color(0.94f, 0.78f, 0.52f, 1f));
        shape.triangle(
            x, y + 12f,
            x + w, y + 12f,
            x + w / 2f, y
        );

        shape.setColor(new Color(0.15f, 0.15f, 0.15f, 1f));
        shape.triangle(
            x + w * 0.35f, y + 6f,
            x + w * 0.65f, y + 6f,
            x + w / 2f, y - 6f
        );
    }

    private void drawTexts() {
        SpriteBatch batch = assets.getBatch();
        GameManager gm = session.getGameManager();

        int currentScore = gm.getCurrentScore();
        int bestScore = gm.getBestScore();
        int lives = gm.getLives();

        batch.begin();

        if (session.isPlaying()) {
            drawHudTexts(currentScore, bestScore, lives);

            assets.getSmallFont().setColor(new Color(1f, 0.88f, 0.46f, 1f));
            assets.getSmallFont().draw(batch, "Progression vers la victoire", 18, 334);

            assets.getSmallFont().setColor(new Color(0.92f, 0.94f, 0.98f, 1f));
            assets.getSmallFont().draw(batch, "P = Pause | ESC = Menu", 18, 316);

            if (lives == 1) {
                float pulse = 0.75f + 0.25f * (float) Math.sin(session.getStateTime() * 8f);
                assets.getTextFont().setColor(1f, 0.22f, 0.18f, pulse);
                assets.getTextFont().draw(batch, "ATTENTION : DERNIERE VIE", 325, 518);
            }
        }

        if (session.isPaused()) {
            drawHudTexts(currentScore, bestScore, lives);

            assets.getTitleFont().setColor(new Color(0.05f, 0.11f, 0.27f, 1f));
            assets.getTitleFont().draw(batch, "PAUSE", 390, 285);

            assets.getTextFont().setColor(new Color(0.82f, 0.32f, 0.10f, 1f));
            assets.getTextFont().draw(batch, "Appuie sur P pour reprendre", 315, 235);
        }

        if (session.isGameOver()) {
            assets.getTitleFont().setColor(new Color(0.82f, 0.12f, 0.17f, 1f));
            assets.getTitleFont().draw(batch, "GAME OVER", 325, 315);

            assets.getTextFont().setColor(new Color(0.05f, 0.11f, 0.27f, 1f));
            assets.getTextFont().draw(batch, session.getCharacterName() + " a ete rattrape par les devoirs...", 248, 274);
            assets.getTextFont().draw(batch, "Score final : " + currentScore, 350, 238);
            assets.getTextFont().draw(batch, "Meilleur score : " + bestScore, 330, 208);
            assets.getTextFont().draw(batch, "ESPACE = Rejouer", 360, 178);
        }

        if (session.isVictory()) {
            assets.getTitleFont().setColor(new Color(0.10f, 0.56f, 0.22f, 1f));
            assets.getTitleFont().draw(batch, "VICTOIRE MORALE !", 242, 315);

            assets.getTextFont().setColor(new Color(0.05f, 0.11f, 0.27f, 1f));
            assets.getTextFont().draw(batch, session.getCharacterName() + " a survecu au chaos scolaire !", 248, 274);
            assets.getTextFont().draw(batch, "Score final : " + currentScore, 350, 202);
            assets.getTextFont().draw(batch, "ESPACE = Retour Menu", 342, 172);
        }

        batch.end();
    }

    private void drawHudTexts(int currentScore, int bestScore, int lives) {
        SpriteBatch batch = assets.getBatch();

        assets.getTextFont().setColor(Color.WHITE);
        assets.getTextFont().draw(batch, "Personnage : " + session.getCharacterName(), 28, 510);
        assets.getTextFont().draw(batch, "Score : " + currentScore, 28, 480);
        assets.getTextFont().draw(batch, "Meilleur score : " + bestScore, 28, 450);
        assets.getTextFont().draw(batch, "Vies : " + lives, 28, 420);
        assets.getTextFont().draw(batch, "Objectif : " + session.getGameManager().getTargetScore(), 28, 390);

        assets.getTextFont().setColor(new Color(1f, 0.84f, 0.38f, 1f));
        assets.getTextFont().draw(batch, "Difficulte : " + session.getGameManager().getDifficultyLabel(), 728, 515);

        assets.getSmallFont().setColor(Color.WHITE);
        assets.getSmallFont().draw(batch, "Intensite", 728, 435);
        assets.getSmallFont().draw(batch, "Vies", 728, 466);
    }
}