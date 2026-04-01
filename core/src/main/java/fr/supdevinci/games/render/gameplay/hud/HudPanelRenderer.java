package fr.supdevinci.games.render.gameplay.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import fr.supdevinci.games.assets.GameAssets;
import fr.supdevinci.games.core.GameLogic;
import fr.supdevinci.games.core.GameSession;

public class HudPanelRenderer {

    private static final Color PANEL_FILL = new Color(0.02f, 0.07f, 0.18f, 0.62f);
    private static final Color PANEL_BORDER = new Color(0.80f, 0.84f, 0.92f, 0.82f);
    private static final Color PANEL_SHADOW = new Color(0f, 0f, 0f, 0.22f);

    private final GameAssets assets;
    private final GameSession session;

    public HudPanelRenderer(GameAssets assets, GameSession session) {
        this.assets = assets;
        this.session = session;
    }

    public void render() {
        ShapeRenderer shape = assets.getShapeRenderer();

        shape.begin(ShapeRenderer.ShapeType.Filled);

        if (session.isPlaying() || session.isPaused()) {
            drawPanel(shape, 8f, 425f, 355f, 105f);
            drawPanel(shape, 8f, 370f, 355f, 34f);
            drawPanel(shape, 695f, 425f, 255f, 105f);
            drawPanel(shape, 8f, 10f, 230f, 30f);

            drawProgressBarBackground(shape, 13f, 380f, 345f, 16f);
            drawDifficultyBarBackground(shape, 725f, 435f, 185f, 10f);
        }

        shape.end();

        if (session.isPlaying() || session.isPaused()) {
            drawProgressBarFill(15f, 380f, 340f, 12f);
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

    private void drawProgressBarBackground(ShapeRenderer shape, float x, float y, float width, float height) {
        shape.setColor(new Color(0.08f, 0.12f, 0.22f, 0.92f));
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
                shape.setColor(new Color(0.22f, 0.30f, 0.45f, 0.55f));
            }

            shape.rect(segmentX, y, segmentWidth, height);
        }

        shape.end();
    }

    private Color getProgressColor(float ratio) {
        if (ratio < 0.34f) return new Color(1.00f, 0.80f, 0.12f, 1f);
        if (ratio < 0.67f) return new Color(0.36f, 0.80f, 1.00f, 1f);
        return new Color(0.32f, 0.96f, 0.48f, 1f);
    }

    private void drawDifficultyBarBackground(ShapeRenderer shape, float x, float y, float width, float height) {
        shape.setColor(new Color(0.78f, 0.82f, 0.92f, 0.92f));
        shape.rect(x, y, width, height);
    }

    private void drawDifficultyBarFill(float x, float y, float width, float height) {
        GameLogic gm = session.getGameManager();
        float value = Math.min(1f, (gm.getDifficultyMultiplier() - 1f) / 2.2f);

        ShapeRenderer shape = assets.getShapeRenderer();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(new Color(0.93f, 0.20f, 0.18f, 0.95f));
        shape.rect(x, y, width * value, height);
        shape.end();
    }
}