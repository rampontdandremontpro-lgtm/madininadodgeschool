package fr.supdevinci.games;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameAssets {

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private BitmapFont titleFont;
    private BitmapFont textFont;
    private BitmapFont smallFont;

    private GlyphLayout glyphLayout;

    private Texture backgroundSchoolTexture;
    private Texture backgroundMenuTexture;
    private Texture backgroundCharactersTexture;

    private Texture characterAlizeeTexture;
    private Texture characterJeremieTexture;

    private TextureRegion alizeeRegion;
    private TextureRegion jeremieRegion;

    public void load() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        glyphLayout = new GlyphLayout();

        createFonts();
        loadTextures();
    }

    private void createFonts() {
        titleFont = new BitmapFont();
        titleFont.getData().setScale(2.3f);

        textFont = new BitmapFont();
        textFont.getData().setScale(1.3f);

        smallFont = new BitmapFont();
        smallFont.getData().setScale(1.08f);
    }

    private void loadTextures() {
        backgroundSchoolTexture = new Texture("background_school.png");
        backgroundMenuTexture = new Texture("background_menu.png");
        backgroundCharactersTexture = new Texture("background_characters.png");
        // Si besoin, remplace par "background_charaters.png"

        characterAlizeeTexture = new Texture("characterAlizee.png");
        characterJeremieTexture = new Texture("characterJeremie.png");

        backgroundSchoolTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        backgroundMenuTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        backgroundCharactersTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        characterAlizeeTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        characterJeremieTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        alizeeRegion = new TextureRegion(characterAlizeeTexture);
        jeremieRegion = new TextureRegion(characterJeremieTexture);
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public BitmapFont getTitleFont() {
        return titleFont;
    }

    public BitmapFont getTextFont() {
        return textFont;
    }

    public BitmapFont getSmallFont() {
        return smallFont;
    }

    public GlyphLayout getGlyphLayout() {
        return glyphLayout;
    }

    public Texture getBackgroundSchoolTexture() {
        return backgroundSchoolTexture;
    }

    public Texture getBackgroundMenuTexture() {
        return backgroundMenuTexture;
    }

    public Texture getBackgroundCharactersTexture() {
        return backgroundCharactersTexture;
    }

    public TextureRegion getCharacterRegion(int characterId) {
        return characterId == GameConfig.CHARACTER_ALIZEE ? alizeeRegion : jeremieRegion;
    }

    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        titleFont.dispose();
        textFont.dispose();
        smallFont.dispose();

        if (backgroundSchoolTexture != null) backgroundSchoolTexture.dispose();
        if (backgroundMenuTexture != null) backgroundMenuTexture.dispose();
        if (backgroundCharactersTexture != null) backgroundCharactersTexture.dispose();
        if (characterAlizeeTexture != null) characterAlizeeTexture.dispose();
        if (characterJeremieTexture != null) characterJeremieTexture.dispose();
    }
}