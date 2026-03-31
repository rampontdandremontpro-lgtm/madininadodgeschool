package fr.supdevinci.games;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameplayRenderer {

    private static final Color PANEL_FILL = new Color(0.02f, 0.07f, 0.18f, 0.90f);
    private static final Color PANEL_BORDER = new Color(0.80f, 0.84f, 0.92f, 1f);
    private static final Color PANEL_SHADOW = new Color(0f, 0f, 0f, 0.28f);
    private static final Color TEXT_WHITE = new Color(0.96f, 0.97f, 1f, 1f);
    private static final Color TEXT_GOLD = new Color(1f, 0.82f, 0.26f, 1f);
    private static final Color TEXT_SOFT = new Color(0.86f, 0.90f, 0.98f, 1f);

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
            drawPanel(shape, 8f, 405f, 355f, 122f);
            drawPanel(shape, 8f, 350f, 355f, 34f);   
            drawPanel(shape, 695f, 425f, 255f, 105f); 
            drawPanel(shape, 8f, 10f, 230f, 30f);     

            drawProgressBarBackground(shape, 13f, 358f, 345f, 16f);
            drawDifficultyBarBackground(shape, 725f, 435f, 185f, 10f);
        }

        if (session.isPaused()) {
            shape.setColor(new Color(0.03f, 0.07f, 0.15f, 0.60f));
            shape.rect(0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

            drawModalCard(shape, 250f, 180f, 460f, 150f);
        }

        if (session.isGameOver() || session.isVictory()) {
            shape.setColor(new Color(0.03f, 0.07f, 0.15f, 0.62f));
            shape.rect(0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

            drawModalCard(shape, 170f, 140f, 620f, 210f);
        }

        if (session.isPlaying() && gm.isPlayerInvulnerable()) {
            float alpha = 0.04f + 0.10f * gm.getHitCooldownRatio();
            shape.setColor(new Color(1f, 0f, 0f, alpha));
            shape.rect(0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        }

        if (session.isPlaying() && gm.getLives() == 1) {
            float alpha = 0.13f + 0.05f * (float) Math.sin(session.getStateTime() * 7f);
            shape.setColor(new Color(0.85f, 0.10f, 0.10f, alpha));
            shape.rect(315f, 505f, 330f, 18f);
        }

        shape.end();

        if (session.isPlaying() || session.isPaused()) {
            drawProgressBarFill(15f, 360f, 340f, 12f);
            drawDifficultyBarFill(727f, 436f, 181f, 8f);
        }
    }

    private void drawPanel(ShapeRenderer shape, float x, float y, float width, float height) {
        shape.setColor(PANEL_SHADOW);
        shape.rect(x + 4f, y - 4f, width, height);

        shape.setColor(PANEL_BORDER);
        shape.rect(x - 2f, y - 2f, width + 4f, height + 4f);

        shape.setColor(PANEL_FILL);
        shape.rect(x, y, width, height);
    }

    private void drawModalCard(ShapeRenderer shape, float x, float y, float width, float height) {
        shape.setColor(new Color(0f, 0f, 0f, 0.28f));
        shape.rect(x + 6f, y - 6f, width, height);

        shape.setColor(PANEL_BORDER);
        shape.rect(x - 4f, y - 4f, width + 8f, height + 8f);

        shape.setColor(new Color(0.98f, 0.98f, 0.99f, 0.96f));
        shape.rect(x, y, width, height);
    }

    private void drawProgressBarBackground(ShapeRenderer shape, float x, float y, float width, float height) {
        shape.setColor(new Color(0.08f, 0.12f, 0.22f, 1f));
        shape.rect(x, y, width, height);
    }

    private void drawProgressBarFill(float x, float y, float width, float height) {
        ShapeRenderer shape = assets.getShapeRenderer();
        float progress = session.getGameManager().getProgressRatio();

        int segments = 10;
        float gap = 2f;
        float segmentWidth = (width - ((segments - 1) * gap)) / segments;
        int filledSegments = Math.round(progress * segments);

        shape.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < segments; i++) {
            float segmentX = x + i * (segmentWidth + gap);

            if (i < filledSegments) {
                float ratio = (i + 1f) / segments;
                shape.setColor(getProgressColor(ratio));
            } else {
                shape.setColor(new Color(0.22f, 0.30f, 0.45f, 0.70f));
            }

            shape.rect(segmentX, y, segmentWidth, height);
        }

        shape.end();
    }

    private Color getProgressColor(float ratio) {
        if (ratio < 0.34f) {
            return new Color(1.00f, 0.80f, 0.12f, 1f);
        }
        if (ratio < 0.67f) {
            return new Color(0.36f, 0.80f, 1.00f, 1f);
        }
        return new Color(0.32f, 0.96f, 0.48f, 1f);
    }

    private void drawDifficultyBarBackground(ShapeRenderer shape, float x, float y, float width, float height) {
        shape.setColor(new Color(0.78f, 0.82f, 0.92f, 0.96f));
        shape.rect(x, y, width, height);
    }

    private void drawDifficultyBarFill(float x, float y, float width, float height) {
        float value = Math.min(1f, (session.getGameManager().getDifficultyMultiplier() - 1f) / 2.2f);

        ShapeRenderer shape = assets.getShapeRenderer();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(new Color(0.93f, 0.20f, 0.18f, 1f));
        shape.rect(x, y, width * value, height);
        shape.end();
    }

    private void drawObjects() {
        SpriteBatch batch = assets.getBatch();
        GameManager gm = session.getGameManager();
        Player player = gm.getPlayer();

        batch.begin();
        drawPlayerSprite(player);
        drawNameAbovePlayer(player);

        for (Homework hw : gm.getHomeworks()) {
            drawHomeworkTexture(batch, hw);
        }

        batch.end();
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

        assets.getSmallFont().setColor(0f, 0f, 0f, 0.45f);
        assets.getSmallFont().draw(assets.getBatch(), label, textX + 1.5f, textY - 1.5f);

        assets.getSmallFont().setColor(TEXT_WHITE);
        assets.getSmallFont().draw(assets.getBatch(), label, textX, textY);
    }

    private void drawHomeworkTexture(SpriteBatch batch, Homework hw) {
    Texture texture = getHomeworkTexture(hw);

    float scaleX = 1f;
    float scaleY = 1f;

    switch (hw.getType()) {
        case COPY:
            scaleX = 1.65f;
            scaleY = 1.65f;
            break;

        case NOTEBOOK:
            scaleX = 1.55f;
            scaleY = 1.55f;
            break;

        case TEST:
            scaleX = 1.80f;
            scaleY = 1.80f;
            break;

        case GIANT_PEN:
            scaleX = 1.95f;
            scaleY = 1.55f;
            break;
    }

    float drawWidth = hw.getWidth() * scaleX;
    float drawHeight = hw.getHeight() * scaleY;
    float drawX = hw.getX() - (drawWidth - hw.getWidth()) / 2f;
    float drawY = hw.getY() - (drawHeight - hw.getHeight()) / 2f;

    batch.setColor(Color.WHITE);
    batch.draw(texture, drawX, drawY, drawWidth, drawHeight);
}

    private Texture getHomeworkTexture(Homework hw) {
        switch (hw.getType()) {
            case NOTEBOOK:
                return assets.getNotebookTexture();
            case TEST:
                return assets.getTestTexture();
            case GIANT_PEN:
                return assets.getGiantPenTexture();
            case COPY:
            default:
                return assets.getCopyTexture();
        }
    }

    private void drawTexts() {
        SpriteBatch batch = assets.getBatch();
        GameManager gm = session.getGameManager();

        int currentScore = gm.getCurrentScore();
        int bestScore = gm.getBestScore();
        int lives = gm.getLives();

        batch.begin();

        if (session.isPlaying()) {
            drawHudTexts(currentScore, bestScore);
            drawHearts(lives);

            if (lives == 1) {
                float pulse = 0.75f + 0.25f * (float) Math.sin(session.getStateTime() * 8f);
                assets.getTextFont().setColor(1f, 0.22f, 0.18f, pulse);
                drawShadowedText(assets.getTextFont(), "ATTENTION : DERNIERE VIE", 328f, 518f, assets.getTextFont().getColor());
            }
        }

        if (session.isPaused()) {
            drawHudTexts(currentScore, bestScore);
            drawHearts(lives);

            assets.getTitleFont().setColor(new Color(0.05f, 0.11f, 0.27f, 1f));
            assets.getTitleFont().draw(batch, "PAUSE", 390f, 285f);

            assets.getTextFont().setColor(new Color(0.82f, 0.32f, 0.10f, 1f));
            assets.getTextFont().draw(batch, "Appuie sur P pour reprendre", 315f, 235f);
        }

        if (session.isGameOver()) {
            assets.getTitleFont().setColor(new Color(0.82f, 0.12f, 0.17f, 1f));
            assets.getTitleFont().draw(batch, "GAME OVER", 325f, 315f);

            assets.getTextFont().setColor(new Color(0.05f, 0.11f, 0.27f, 1f));
            assets.getTextFont().draw(batch, session.getCharacterName() + " a ete rattrape par les devoirs...", 248f, 274f);
            assets.getTextFont().draw(batch, "Score final : " + currentScore, 350f, 238f);
            assets.getTextFont().draw(batch, "Meilleur score : " + bestScore, 330f, 208f);
            assets.getTextFont().draw(batch, "ESPACE = Rejouer", 360f, 178f);
        }

        if (session.isVictory()) {
            assets.getTitleFont().setColor(new Color(0.10f, 0.56f, 0.22f, 1f));
            assets.getTitleFont().draw(batch, "VICTOIRE MORALE !", 242f, 315f);

            assets.getTextFont().setColor(new Color(0.05f, 0.11f, 0.27f, 1f));
            assets.getTextFont().draw(batch, session.getCharacterName() + " a survecu au chaos scolaire !", 248f, 274f);
            assets.getTextFont().draw(batch, "Score final : " + currentScore, 350f, 202f);
            assets.getTextFont().draw(batch, "ESPACE = Retour Menu", 342f, 172f);
        }

        batch.end();
    }

    private void drawHudTexts(int currentScore, int bestScore) {
    drawShadowedText(assets.getTextFont(), "Personnage : " + session.getCharacterName(), 22f, 520f, TEXT_WHITE);
    drawShadowedText(assets.getTextFont(), "Score : " + currentScore, 22f, 490f, TEXT_WHITE);
    drawShadowedText(assets.getTextFont(), "Meilleur score : " + bestScore, 22f, 460f, TEXT_WHITE);
    drawShadowedText(
            assets.getTextFont(),
            "Objectif : " + session.getGameManager().getTargetScore(),
            22f,
            430f,
            TEXT_WHITE
    );

    drawShadowedText(
            assets.getTextFont(),
            "Difficulte : " + session.getGameManager().getDifficultyLabel(),
            722f,
            520f,
            TEXT_GOLD
    );

    drawShadowedText(assets.getSmallFont(), "Vies", 722f, 500f, TEXT_WHITE);
    drawShadowedText(assets.getSmallFont(), "Intensite", 722f, 465f, TEXT_SOFT);
    drawShadowedText(assets.getTextFont(), "P = Pause | ESC = Menu", 20f, 32f, TEXT_GOLD);
}

    private void drawHearts(int lives) {
        SpriteBatch batch = assets.getBatch();
        Texture heartFull = assets.getHeartFullTexture();
        Texture heartEmpty = assets.getHeartEmptyTexture();

        float startX = 724f;
        float y = 460f;
        float size = 30f;
        float spacing = 14f;

        for (int i = 0; i < 3; i++) {
            Texture texture = i < lives ? heartFull : heartEmpty;
            batch.draw(texture, startX + i * (size + spacing), y, size, size);
        }
    }

    private void drawShadowedText(BitmapFont font, String text, float x, float y, Color color) {
        SpriteBatch batch = assets.getBatch();

        font.setColor(0f, 0f, 0f, 0.45f);
        font.draw(batch, text, x + 1.5f, y - 1.5f);

        font.setColor(color);
        font.draw(batch, text, x, y);
    }
}