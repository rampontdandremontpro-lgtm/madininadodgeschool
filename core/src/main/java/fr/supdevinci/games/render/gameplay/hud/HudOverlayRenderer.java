package fr.supdevinci.games.render.gameplay.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import fr.supdevinci.games.assets.GameAssets;
import fr.supdevinci.games.config.GameConfig;
import fr.supdevinci.games.core.GameLogic;
import fr.supdevinci.games.core.GameSession;

public class HudOverlayRenderer {

    private static final Color TEXT_SUCCESS = new Color(0.32f, 0.96f, 0.48f, 1f);
    private static final Color TEXT_ALERT = new Color(1f, 0.30f, 0.20f, 1f);
    private static final Color TEXT_GOLD = new Color(1f, 0.82f, 0.26f, 1f);

    private final GameAssets assets;
    private final GameSession session;

    public HudOverlayRenderer(GameAssets assets, GameSession session) {
        this.assets = assets;
        this.session = session;
    }

    public void render() {
        drawOverlayShapes();
        drawOverlayTexts();
    }

    private void drawOverlayShapes() {
        ShapeRenderer shape = assets.getShapeRenderer();
        GameLogic gm = session.getGameManager();

        shape.begin(ShapeRenderer.ShapeType.Filled);

        if (session.isPaused()) {
            shape.setColor(new Color(0.03f, 0.07f, 0.15f, 0.52f));
            shape.rect(0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
            drawModalCard(shape, 250f, 180f, 460f, 150f);
        }

        if (session.isGameOver() || session.isVictory()) {
            shape.setColor(new Color(0.03f, 0.07f, 0.15f, 0.58f));
            shape.rect(0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
            drawModalCard(shape, 170f, 140f, 620f, 210f);
        }

        if (session.isPlaying() && gm.getLives() == 1) {
            drawLastLifeBanner(shape);
        }

        if (session.isPlaying() && gm.getLevelTransitionTimer() > 0f) {
            drawLevelTransitionBanner(shape);
        }

        shape.end();
    }

    private void drawOverlayTexts() {
        SpriteBatch batch = assets.getBatch();
        GameLogic gm = session.getGameManager();

        int currentScore = gm.getCurrentScore();
        int bestScore = gm.getBestScore();

        batch.begin();

        if (session.isPlaying() && gm.getLives() == 1) {
            drawShadowedText(assets.getTextFont(), "ATTENTION : DERNIERE VIE", 380f, 520f, TEXT_ALERT);
        }

        if (session.isPlaying() && gm.getLevelTransitionTimer() > 0f) {
            drawCenteredTransitionText(gm.getLevelTransitionMessage());
        }

        if (session.isPaused()) {
            assets.getTitleFont().setColor(new Color(0.05f, 0.11f, 0.27f, 1f));
            assets.getTitleFont().draw(batch, "PAUSE", 390f, 285f);

            assets.getTextFont().setColor(new Color(0.82f, 0.32f, 0.10f, 1f));
            assets.getTextFont().draw(batch, "Appuie sur P pour reprendre", 315f, 235f);
        }

        if (session.isGameOver()) {
            assets.getTitleFont().setColor(new Color(0.82f, 0.12f, 0.17f, 1f));
            assets.getTitleFont().draw(batch, "ANNEE RATEE", 300f, 315f);

            assets.getTextFont().setColor(new Color(0.05f, 0.11f, 0.27f, 1f));
            assets.getTextFont().draw(batch, getFailureMessage(currentScore, session.getCharacterName()), 205f, 274f);
            assets.getTextFont().draw(batch, "Score final : " + currentScore, 350f, 238f);
            assets.getTextFont().draw(batch, "Meilleur score : " + bestScore, 330f, 208f);
            assets.getTextFont().draw(batch, "ESPACE = Rejouer", 360f, 178f);
        }

        if (session.isVictory()) {
            assets.getTitleFont().setColor(TEXT_SUCCESS);
            assets.getTitleFont().draw(batch, "BAC EN POCHE !", 280f, 315f);

            assets.getTextFont().setColor(new Color(0.05f, 0.11f, 0.27f, 1f));
            assets.getTextFont().draw(batch, session.getCharacterName() + " a survecu jusqu'a la fin de Terminale !", 195f, 274f);
            assets.getTextFont().draw(batch, "Score final : " + currentScore, 350f, 202f);
            assets.getTextFont().draw(batch, "ESPACE = Retour Menu", 342f, 172f);
        }

        batch.end();
    }

    private void drawModalCard(ShapeRenderer shape, float x, float y, float width, float height) {
        shape.setColor(new Color(0f, 0f, 0f, 0.26f));
        shape.rect(x + 6f, y - 6f, width, height);

        shape.setColor(new Color(0.88f, 0.91f, 0.98f, 0.95f));
        shape.rect(x - 4f, y - 4f, width + 8f, height + 8f);

        shape.setColor(new Color(0.98f, 0.98f, 0.99f, 0.96f));
        shape.rect(x, y, width, height);
    }

    private void drawLastLifeBanner(ShapeRenderer shape) {
        float x = 330f;
        float y = 500f;
        float width = 300f;
        float height = 28f;

        shape.setColor(new Color(0f, 0f, 0f, 0.18f));
        shape.rect(x + 3f, y - 3f, width, height);

        shape.setColor(new Color(0.90f, 0.20f, 0.20f, 0.85f));
        shape.rect(x - 2f, y - 2f, width + 4f, height + 4f);

        shape.setColor(new Color(0.18f, 0.04f, 0.04f, 0.82f));
        shape.rect(x, y, width, height);
    }

    private void drawLevelTransitionBanner(ShapeRenderer shape) {
        float alpha = Math.min(0.80f, session.getGameManager().getLevelTransitionTimer() / 2f);

        float x = 300f;
        float y = 270f;
        float width = 360f;
        float height = 54f;

        shape.setColor(new Color(0f, 0f, 0f, 0.18f * alpha));
        shape.rect(x + 4f, y - 4f, width, height);

        shape.setColor(new Color(1f, 0.84f, 0.26f, alpha));
        shape.rect(x - 2f, y - 2f, width + 4f, height + 4f);

        shape.setColor(new Color(0.02f, 0.07f, 0.18f, 0.82f * alpha));
        shape.rect(x, y, width, height);
    }

    private void drawCenteredTransitionText(String message) {
        if (message == null || message.isEmpty()) {
            return;
        }

        assets.getGlyphLayout().setText(assets.getTextFont(), message);
        float x = (GameConfig.WORLD_WIDTH - assets.getGlyphLayout().width) / 2f;
        float y = 305f;

        drawShadowedText(assets.getTextFont(), message, x, y, TEXT_GOLD);
    }

    private void drawShadowedText(BitmapFont font, String text, float x, float y, Color color) {
        SpriteBatch batch = assets.getBatch();

        font.setColor(0f, 0f, 0f, 0.45f);
        font.draw(batch, text, x + 1.5f, y - 1.5f);

        font.setColor(color);
        font.draw(batch, text, x, y);
    }

    private String getFailureMessage(int score, String characterName) {
        if (score < 30) return characterName + " n'a pas tenu jusqu'a la fin de la 2nde...";
        if (score < 70) return characterName + " a craque pendant l'annee de 1ere...";
        if (score < 80) return characterName + " n'a meme pas profite de la revision...";
        return characterName + " a chute juste avant le bac final...";
    }
}