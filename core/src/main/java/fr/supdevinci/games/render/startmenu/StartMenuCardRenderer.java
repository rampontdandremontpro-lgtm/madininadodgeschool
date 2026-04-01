package fr.supdevinci.games.render.startmenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import fr.supdevinci.games.assets.GameAssets;
import fr.supdevinci.games.config.GameConfig;
import fr.supdevinci.games.core.GameSession;

public class StartMenuCardRenderer {

    private static final float LEFT_CARD_X = 150f;
    private static final float RIGHT_CARD_X = 550f;
    private static final float CARD_Y = 130f;

    private final GameAssets assets;
    private final GameSession session;

    public StartMenuCardRenderer(GameAssets assets, GameSession session) {
        this.assets = assets;
        this.session = session;
    }

    public void render() {
        drawCardFrames();
        drawCharacterCards();
    }

    private void drawCardFrames() {
        ShapeRenderer shape = assets.getShapeRenderer();

        shape.begin(ShapeRenderer.ShapeType.Filled);

        drawCharacterCardFrame(
                LEFT_CARD_X,
                CARD_Y,
                GameConfig.CARD_WIDTH,
                GameConfig.CARD_HEIGHT,
                session.getSelectedCharacter() == GameConfig.CHARACTER_ALIZEE
        );

        drawCharacterCardFrame(
                RIGHT_CARD_X,
                CARD_Y,
                GameConfig.CARD_WIDTH,
                GameConfig.CARD_HEIGHT,
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
        drawCharacterPreview(LEFT_CARD_X, CARD_Y, GameConfig.CHARACTER_ALIZEE);
        drawCharacterPreview(RIGHT_CARD_X, CARD_Y, GameConfig.CHARACTER_JEREMIE);
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
}