package fr.supdevinci.games.render.startmenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.supdevinci.games.assets.GameAssets;
import fr.supdevinci.games.config.GameConfig;
import fr.supdevinci.games.core.GameSession;

public class StartMenuTextRenderer {

    private static final float MENU_CENTER_X = GameConfig.WORLD_WIDTH / 2f;
    private static final float LEFT_CARD_X = 150f;
    private static final float RIGHT_CARD_X = 550f;

    private final GameAssets assets;
    private final GameSession session;

    public StartMenuTextRenderer(GameAssets assets, GameSession session) {
        this.assets = assets;
        this.session = session;
    }

    public void render() {
        SpriteBatch batch = assets.getBatch();
        BitmapFont titleFont = assets.getTitleFont();
        BitmapFont textFont = assets.getTextFont();
        BitmapFont smallFont = assets.getSmallFont();
        GlyphLayout glyph = assets.getGlyphLayout();

        batch.begin();

        drawMainTitle(titleFont, glyph, batch);
        drawCenteredText(
                textFont,
                "Choisis ton personnage",
                MENU_CENTER_X,
                430f,
                new Color(1.00f, 0.58f, 0.10f, 1f)
        );

        drawCharacterLabels();
        drawCenteredText(titleFont, "ENTREE = Commencer", MENU_CENTER_X, 72f, Color.WHITE);
        drawCenteredText(smallFont, "Deplace-toi avec Fleches, Q/D ou A/D", MENU_CENTER_X, 30f, Color.WHITE);

        batch.end();
    }

    private void drawMainTitle(BitmapFont titleFont, GlyphLayout glyph, SpriteBatch batch) {
        String word1 = "MADININA";
        String word2 = " DODGE";
        String word3 = " SCHOOL";

        glyph.setText(titleFont, word1);
        float width1 = glyph.width;

        glyph.setText(titleFont, word2);
        float width2 = glyph.width;

        glyph.setText(titleFont, word3);
        float width3 = glyph.width;

        float totalWidth = width1 + width2 + width3;
        float startX = MENU_CENTER_X - totalWidth / 2f;
        float titleY = 480f;

        titleFont.setColor(GameConfig.MENU_RED_TITLE);
        titleFont.draw(batch, word1, startX, titleY);

        titleFont.setColor(GameConfig.MENU_GREEN);
        titleFont.draw(batch, word2, startX + width1, titleY);

        titleFont.setColor(GameConfig.MENU_BLACK);
        titleFont.draw(batch, word3, startX + width1 + width2, titleY);
    }

    private void drawCharacterLabels() {
        if (session.getSelectedCharacter() == GameConfig.CHARACTER_ALIZEE) {
            drawMenuCharacterLabel("1 = Alizee selectionnee", LEFT_CARD_X, GameConfig.CARD_WIDTH, 110f, GameConfig.MENU_RED);
            drawMenuCharacterLabel("2 = Jeremie", RIGHT_CARD_X, GameConfig.CARD_WIDTH, 110f, GameConfig.MENU_RED);
        } else {
            drawMenuCharacterLabel("1 = Alizee", LEFT_CARD_X, GameConfig.CARD_WIDTH, 110f, GameConfig.MENU_RED);
            drawMenuCharacterLabel("2 = Jeremie selectionne", RIGHT_CARD_X, GameConfig.CARD_WIDTH, 110f, GameConfig.MENU_RED);
        }
    }

    private void drawCenteredText(BitmapFont font, String text, float centerX, float y, Color color) {
        GlyphLayout glyph = assets.getGlyphLayout();
        SpriteBatch batch = assets.getBatch();

        glyph.setText(font, text);
        font.setColor(color);
        font.draw(batch, text, centerX - glyph.width / 2f, y);
    }

    private void drawMenuCharacterLabel(String text, float cardX, float cardWidth, float y, Color color) {
        GlyphLayout glyph = assets.getGlyphLayout();
        BitmapFont textFont = assets.getTextFont();
        SpriteBatch batch = assets.getBatch();

        float centerX = cardX + cardWidth / 2f;

        glyph.setText(textFont, text);
        textFont.setColor(new Color(0f, 0f, 0f, 0.45f));
        textFont.draw(batch, text, centerX - glyph.width / 2f + 2f, y - 2f);

        drawCenteredText(textFont, text, centerX, y, color);
    }
}