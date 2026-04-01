package fr.supdevinci.games.render.gameplay.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.supdevinci.games.assets.GameAssets;
import fr.supdevinci.games.core.GameLogic;
import fr.supdevinci.games.core.GameSession;

public class HudTextRenderer {

    private static final Color TEXT_WHITE = new Color(0.96f, 0.97f, 1f, 1f);
    private static final Color TEXT_GOLD = new Color(1f, 0.82f, 0.26f, 1f);
    private static final Color TEXT_SOFT = new Color(0.86f, 0.90f, 0.98f, 1f);

    private final GameAssets assets;
    private final GameSession session;

    public HudTextRenderer(GameAssets assets, GameSession session) {
        this.assets = assets;
        this.session = session;
    }

    public void render() {
        if (!(session.isPlaying() || session.isPaused())) {
            return;
        }

        SpriteBatch batch = assets.getBatch();
        GameLogic gm = session.getGameManager();

        int currentScore = gm.getCurrentScore();
        int bestScore = gm.getBestScore();
        int lives = gm.getLives();

        batch.begin();

        drawHudTexts(currentScore, bestScore);
        drawHearts(lives);

        batch.end();
    }

    private void drawHudTexts(int currentScore, int bestScore) {
        drawShadowedText(assets.getTextFont(), "Personnage : " + session.getCharacterName(), 22f, 520f, TEXT_WHITE);
        drawShadowedText(assets.getTextFont(), "Score : " + currentScore + "s", 22f, 490f, TEXT_WHITE);
        drawShadowedText(assets.getTextFont(), "Meilleur score : " + bestScore + "s", 22f, 460f, TEXT_WHITE);

        drawShadowedText(assets.getTextFont(), "Niveau : " + session.getGameManager().getDifficultyLabel(), 722f, 520f, TEXT_GOLD);
        drawShadowedText(assets.getSmallFont(), "Vies", 722f, 500f, TEXT_WHITE);

        String intensityLabel = session.getGameManager().isRevisionMode()
                ? "Revision : " + (int) Math.ceil(session.getGameManager().getRevisionModeTimer()) + "s"
                : "Temps : " + currentScore + "s / " + session.getGameManager().getTargetScore() + "s";

        drawShadowedText(assets.getSmallFont(), intensityLabel, 722f, 465f, TEXT_SOFT);
        drawShadowedText(assets.getTextFont(), "P = Pause | ESC = Menu", 20f, 32f, TEXT_GOLD);
    }

    private void drawHearts(int lives) {
        SpriteBatch batch = assets.getBatch();
        Texture heartFull = assets.getHeartFullTexture();
        Texture heartEmpty = assets.getHeartEmptyTexture();

        int maxLives = session.getGameManager().getMaxLives();

        float startX = 724f;
        float y = 468f;
        float size = 20f;
        float spacing = 12f;

        for (int i = 0; i < maxLives; i++) {
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