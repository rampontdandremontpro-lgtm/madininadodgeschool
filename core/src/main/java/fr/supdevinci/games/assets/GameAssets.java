package fr.supdevinci.games.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import fr.supdevinci.games.model.Homework;

import java.util.EnumMap;
import java.util.Map;

public class GameAssets {

    private final Map<BackgroundAsset, Texture> backgrounds = new EnumMap<>(BackgroundAsset.class);
    private final Map<Homework.HomeworkType, Texture> homeworkTextures = new EnumMap<>(Homework.HomeworkType.class);

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private BitmapFont titleFont;
    private BitmapFont textFont;
    private BitmapFont smallFont;

    private GlyphLayout glyphLayout;

    private Texture characterAlizeeTexture;
    private Texture characterJeremieTexture;
    private Texture heartFullTexture;
    private Texture heartEmptyTexture;

    private TextureRegion alizeeRegion;
    private TextureRegion jeremieRegion;

    public void load() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        titleFont = new BitmapFont();
        titleFont.getData().setScale(2.0f);

        textFont = new BitmapFont();
        textFont.getData().setScale(1.15f);

        smallFont = new BitmapFont();
        smallFont.getData().setScale(0.9f);

        glyphLayout = new GlyphLayout();

        loadBackgrounds();
        loadCharacters();
        loadHomeworkTextures();
        loadHeartTextures();
    }

    private void loadBackgrounds() {
        for (BackgroundAsset asset : BackgroundAsset.values()) {
            backgrounds.put(asset, loadTexture(asset.getFileName()));
        }
    }

    private void loadCharacters() {
        characterAlizeeTexture = loadTexture("characterAlizee.png");
        characterJeremieTexture = loadTexture("characterJeremie.png");

        alizeeRegion = new TextureRegion(characterAlizeeTexture);
        jeremieRegion = new TextureRegion(characterJeremieTexture);
    }

    private void loadHomeworkTextures() {
        homeworkTextures.put(Homework.HomeworkType.COPY, loadTexture("copy.png"));
        homeworkTextures.put(Homework.HomeworkType.NOTEBOOK, loadTexture("notebook.png"));
        homeworkTextures.put(Homework.HomeworkType.TEST, loadTexture("test.png"));
        homeworkTextures.put(Homework.HomeworkType.GIANT_PEN, loadTexture("giantPen.png"));
        homeworkTextures.put(Homework.HomeworkType.POMME_CANNELLE, loadTexture("pomme_cannelle.png"));
        homeworkTextures.put(Homework.HomeworkType.PATE_GOYAVE, loadTexture("pate_goyave.png"));
        homeworkTextures.put(Homework.HomeworkType.CARESSE_RARE, loadTexture("caresse_rare.png"));
    }

    private void loadHeartTextures() {
        heartFullTexture = loadTexture("heart_plein.png");
        heartEmptyTexture = loadTexture("heart_vide.png");
    }

    private Texture loadTexture(String fileName) {
        Texture texture = new Texture(fileName);
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        return texture;
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

    public Texture getBackground(BackgroundAsset asset) {
        return backgrounds.get(asset);
    }

    public Texture getHomeworkTexture(Homework.HomeworkType type) {
        return homeworkTextures.get(type);
    }

    public TextureRegion getCharacterRegion(int selectedCharacter) {
        return selectedCharacter == 1 ? alizeeRegion : jeremieRegion;
    }

    public Texture getHeartFullTexture() {
        return heartFullTexture;
    }

    public Texture getHeartEmptyTexture() {
        return heartEmptyTexture;
    }

    public Texture getBackgroundMenuTexture() {
        return getBackground(BackgroundAsset.MENU);
    }

    public Texture getBackgroundCharactersTexture() {
        return getBackground(BackgroundAsset.CHARACTERS);
    }

    public Texture getBackgroundSchoolTexture() {
        return getBackground(BackgroundAsset.SCHOOL);
    }

    public Texture getBackgroundTerrainTexture() {
        return getBackground(BackgroundAsset.TERRAIN);
    }

    public Texture getBackgroundClassroomTexture() {
        return getBackground(BackgroundAsset.CLASSROOM);
    }

    public Texture getBackgroundRevisionTexture() {
        return getBackground(BackgroundAsset.REVISION);
    }

    public Texture getCopyTexture() {
        return getHomeworkTexture(Homework.HomeworkType.COPY);
    }

    public Texture getNotebookTexture() {
        return getHomeworkTexture(Homework.HomeworkType.NOTEBOOK);
    }

    public Texture getTestTexture() {
        return getHomeworkTexture(Homework.HomeworkType.TEST);
    }

    public Texture getGiantPenTexture() {
        return getHomeworkTexture(Homework.HomeworkType.GIANT_PEN);
    }

    public Texture getPommeCanelleTexture() {
        return getHomeworkTexture(Homework.HomeworkType.POMME_CANNELLE);
    }

    public Texture getPateGoyaveTexture() {
        return getHomeworkTexture(Homework.HomeworkType.PATE_GOYAVE);
    }

    public Texture getCaresseRareTexture() {
        return getHomeworkTexture(Homework.HomeworkType.CARESSE_RARE);
    }

    public void dispose() {
        if (batch != null) batch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();

        if (titleFont != null) titleFont.dispose();
        if (textFont != null) textFont.dispose();
        if (smallFont != null) smallFont.dispose();

        for (Texture texture : backgrounds.values()) {
            if (texture != null) texture.dispose();
        }

        for (Texture texture : homeworkTextures.values()) {
            if (texture != null) texture.dispose();
        }

        if (characterAlizeeTexture != null) characterAlizeeTexture.dispose();
        if (characterJeremieTexture != null) characterJeremieTexture.dispose();

        if (heartFullTexture != null) heartFullTexture.dispose();
        if (heartEmptyTexture != null) heartEmptyTexture.dispose();
    }
}