package fr.supdevinci.games.assets;

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

    private Texture backgroundMenuTexture;
    private Texture backgroundCharactersTexture;

    private Texture backgroundSchoolTexture;
    private Texture backgroundTerrainTexture;
    private Texture backgroundClassroomTexture;

    private Texture characterAlizeeTexture;
    private Texture characterJeremieTexture;

    private TextureRegion alizeeRegion;
    private TextureRegion jeremieRegion;

    private Texture copyTexture;
    private Texture notebookTexture;
    private Texture testTexture;
    private Texture giantPenTexture;

    private Texture pommeCanelleTexture;
    private Texture pateGoyaveTexture;
    private Texture caresseRareTexture;

    private Texture heartFullTexture;
    private Texture heartEmptyTexture;

    private Texture backgroundRevisionTexture;

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

        backgroundMenuTexture = new Texture("background_menu.png");
        backgroundCharactersTexture = new Texture("background_characters.png");

        backgroundSchoolTexture = new Texture("background_school.png");
        backgroundTerrainTexture = new Texture("background_terrain.png");
        backgroundClassroomTexture = new Texture("background_classroom.png");

        characterAlizeeTexture = new Texture("characterAlizee.png");
        characterJeremieTexture = new Texture("characterJeremie.png");

        copyTexture = new Texture("copy.png");
        notebookTexture = new Texture("notebook.png");
        testTexture = new Texture("test.png");
        giantPenTexture = new Texture("giantPen.png");

        heartFullTexture = new Texture("heart_plein.png");
        heartEmptyTexture = new Texture("heart_vide.png");

        pommeCanelleTexture = new Texture("pomme_cannelle.png");
        pateGoyaveTexture = new Texture("pate_goyave.png");
        caresseRareTexture = new Texture("caresse_rare.png");

        backgroundRevisionTexture = new Texture("background_revision.png");

        setNearestFilter(backgroundMenuTexture);
        setNearestFilter(backgroundCharactersTexture);
        setNearestFilter(backgroundSchoolTexture);
        setNearestFilter(backgroundTerrainTexture);
        setNearestFilter(backgroundClassroomTexture);

        setNearestFilter(characterAlizeeTexture);
        setNearestFilter(characterJeremieTexture);

        setNearestFilter(copyTexture);
        setNearestFilter(notebookTexture);
        setNearestFilter(testTexture);
        setNearestFilter(giantPenTexture);

        setNearestFilter(heartFullTexture);
        setNearestFilter(heartEmptyTexture);

        pommeCanelleTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        pateGoyaveTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        caresseRareTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        backgroundRevisionTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        alizeeRegion = new TextureRegion(characterAlizeeTexture);
        jeremieRegion = new TextureRegion(characterJeremieTexture);
    }

    private void setNearestFilter(Texture texture) {
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
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

    public Texture getBackgroundMenuTexture() {
        return backgroundMenuTexture;
    }

    public Texture getBackgroundCharactersTexture() {
        return backgroundCharactersTexture;
    }

    public Texture getBackgroundSchoolTexture() {
        return backgroundSchoolTexture;
    }

    public Texture getBackgroundTerrainTexture() {
        return backgroundTerrainTexture;
    }

    public Texture getBackgroundClassroomTexture() {
        return backgroundClassroomTexture;
    }

    public Texture getPommeCanelleTexture() {
    return pommeCanelleTexture;
}

public Texture getPateGoyaveTexture() {
    return pateGoyaveTexture;
}

public Texture getCaresseRareTexture() {
    return caresseRareTexture;
}

public Texture getBackgroundRevisionTexture() {
    return backgroundRevisionTexture;
}

    public Texture getBackgroundByLevel(int score) {
        if (score < 20) {
            return backgroundSchoolTexture;
        } else if (score < 40) {
            return backgroundTerrainTexture;
        } else {
            return backgroundClassroomTexture;
        }
    }

    public TextureRegion getCharacterRegion(int selectedCharacter) {
        return selectedCharacter == 1 ? alizeeRegion : jeremieRegion;
    }

    public Texture getCopyTexture() {
        return copyTexture;
    }

    public Texture getNotebookTexture() {
        return notebookTexture;
    }

    public Texture getTestTexture() {
        return testTexture;
    }

    public Texture getGiantPenTexture() {
        return giantPenTexture;
    }

    public Texture getHeartFullTexture() {
        return heartFullTexture;
    }

    public Texture getHeartEmptyTexture() {
        return heartEmptyTexture;
    }

    public void dispose() {
        if (batch != null) batch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();

        if (titleFont != null) titleFont.dispose();
        if (textFont != null) textFont.dispose();
        if (smallFont != null) smallFont.dispose();

        if (backgroundMenuTexture != null) backgroundMenuTexture.dispose();
        if (backgroundCharactersTexture != null) backgroundCharactersTexture.dispose();

        if (backgroundSchoolTexture != null) backgroundSchoolTexture.dispose();
        if (backgroundTerrainTexture != null) backgroundTerrainTexture.dispose();
        if (backgroundClassroomTexture != null) backgroundClassroomTexture.dispose();

        if (characterAlizeeTexture != null) characterAlizeeTexture.dispose();
        if (characterJeremieTexture != null) characterJeremieTexture.dispose();

        if (copyTexture != null) copyTexture.dispose();
        if (notebookTexture != null) notebookTexture.dispose();
        if (testTexture != null) testTexture.dispose();
        if (giantPenTexture != null) giantPenTexture.dispose();

        if (heartFullTexture != null) heartFullTexture.dispose();
        if (heartEmptyTexture != null) heartEmptyTexture.dispose();

        if (pommeCanelleTexture != null) pommeCanelleTexture.dispose();
if (pateGoyaveTexture != null) pateGoyaveTexture.dispose();
if (caresseRareTexture != null) caresseRareTexture.dispose();
if (backgroundRevisionTexture != null) backgroundRevisionTexture.dispose();
    }
}