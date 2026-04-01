package fr.supdevinci.games.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import fr.supdevinci.games.assets.GameAssets;
import fr.supdevinci.games.config.GameConfig;
import fr.supdevinci.games.core.GameSession;

public class StartMenuRenderer {

    private final GameAssets assets;
    private final GameSession session;

    public StartMenuRenderer(GameAssets assets, GameSession session) {
        this.assets = assets;
        this.session = session;
    }

    public void render() {
        drawBackground();
        drawCardFrames();
        drawCharacterCards();
        drawTexts();
    }

    private void drawBackground() {
        SpriteBatch batch = assets.getBatch();
        Texture background = assets.getBackgroundMenuTexture();

        batch.begin();
        batch.setColor(Color.WHITE);
        batch.draw(background, 0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        batch.end();
    }

    private void drawCardFrames() {
        ShapeRenderer shape = assets.getShapeRenderer();

        shape.begin(ShapeRenderer.ShapeType.Filled);
        drawCharacterCardFrame(
            150f, 130f, GameConfig.CARD_WIDTH, GameConfig.CARD_HEIGHT,
            session.getSelectedCharacter() == GameConfig.CHARACTER_ALIZEE
        );
        drawCharacterCardFrame(
            550f, 130f, GameConfig.CARD_WIDTH, GameConfig.CARD_HEIGHT,
            session.getSelectedCharacter() == GameConfig.CHARACTER_JEREMIE
        );
        shape.end();
    }

    private void drawCharacterCardFrame(float x, float y, float width, float height, boolean selected) {
        ShapeRenderer shape = assets.getShapeRenderer();

        shape.setColor(new Color(0f, 0f, 0f, 0.30f));
        shape.rect(x + 5f, y - 5f, width, height);

        if (selected) {
            shape.setColor(GameConfig.MENU_RED);
            shape.rect(x - 7f, y - 7f, width + 14f, height + 14f);

            shape.setColor(new Color(1f, 0.87f, 0.72f, 1f));
            shape.rect(x - 3f, y - 3f, width + 6f, height + 6f);
        } else {
            shape.setColor(new Color(0.43f, 0.55f, 0.95f, 1f));
            shape.rect(x - 4f, y - 4f, width + 8f, height + 8f);
        }
    }

    private void drawCharacterCards() {
        SpriteBatch batch = assets.getBatch();

        batch.begin();
        drawCharacterPreview(150f, 130f, GameConfig.CHARACTER_ALIZEE);
        drawCharacterPreview(550f, 130f, GameConfig.CHARACTER_JEREMIE);
        batch.end();
    }

    private void drawCharacterPreview(float cardX, float cardY, int characterId) {
        SpriteBatch batch = assets.getBatch();
        Texture backgroundCharacters = assets.getBackgroundCharactersTexture();
        TextureRegion region = assets.getCharacterRegion(characterId);

        batch.setColor(Color.WHITE);
        batch.draw(backgroundCharacters, cardX, cardY, GameConfig.CARD_WIDTH, GameConfig.CARD_HEIGHT);

        float drawY = cardY + 18f;

        if (characterId == GameConfig.CHARACTER_ALIZEE) {
            float drawX = cardX + 58f;
            batch.draw(region, drawX, drawY, 145f, 195f);
        } else {
            float drawX = cardX + 66f;
            batch.draw(region, drawX, drawY, 132f, 195f);
        }
    }

    private void drawTexts() {
        SpriteBatch batch = assets.getBatch();
        BitmapFont titleFont = assets.getTitleFont();
        BitmapFont textFont = assets.getTextFont();
        BitmapFont smallFont = assets.getSmallFont();
        GlyphLayout glyph = assets.getGlyphLayout();

        float menuCenterX = GameConfig.WORLD_WIDTH / 2f;
        float leftCardX = 150f;
        float rightCardX = 550f;

        batch.begin();

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
        float startX = menuCenterX - totalWidth / 2f;
        float titleY = 480f;

        titleFont.setColor(GameConfig.MENU_RED_TITLE);
        titleFont.draw(batch, word1, startX, titleY);

        titleFont.setColor(GameConfig.MENU_GREEN);
        titleFont.draw(batch, word2, startX + width1, titleY);

        titleFont.setColor(GameConfig.MENU_BLACK);
        titleFont.draw(batch, word3, startX + width1 + width2, titleY);

        drawCenteredText(
            textFont,
            "Choisis ton personnage",
            menuCenterX,
            430f,
            new Color(1.00f, 0.58f, 0.10f, 1f)
        );

        if (session.getSelectedCharacter() == GameConfig.CHARACTER_ALIZEE) {
            drawMenuCharacterLabel("1 = Alizee selectionnee", leftCardX, GameConfig.CARD_WIDTH, 110f, GameConfig.MENU_RED);
            drawMenuCharacterLabel("2 = Jeremie", rightCardX, GameConfig.CARD_WIDTH, 110f, GameConfig.MENU_RED);
        } else {
            drawMenuCharacterLabel("1 = Alizee", leftCardX, GameConfig.CARD_WIDTH, 110f, GameConfig.MENU_RED);
            drawMenuCharacterLabel("2 = Jeremie selectionne", rightCardX, GameConfig.CARD_WIDTH, 110f, GameConfig.MENU_RED);
        }

        drawCenteredText(titleFont, "ENTREE = Commencer", menuCenterX, 72f, Color.WHITE);
        drawCenteredText(smallFont, "Deplace-toi avec Fleches, Q/D ou A/D", menuCenterX, 30f, Color.WHITE);

        batch.end();
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