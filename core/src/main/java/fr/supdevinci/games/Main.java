package fr.supdevinci.games;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends ApplicationAdapter {

    private static final float WORLD_WIDTH = 960f;
    private static final float WORLD_HEIGHT = 540f;

    private static final int CHARACTER_ALIZEE = 1;
    private static final int CHARACTER_JEREMIE = 2;

    private static final float PLAYER_DRAW_WIDTH = 130f;
    private static final float PLAYER_DRAW_HEIGHT = 200f;
    private static final float PLAYER_DRAW_OFFSET_X = 38f;

    private static final float CARD_WIDTH = 260f;
    private static final float CARD_HEIGHT = 250f;
    private static final float PREVIEW_WIDTH = 130f;
    private static final float PREVIEW_HEIGHT = 200f;

    private static final Color MENU_RED = new Color(167f / 255f, 0f / 255f, 30f / 255f, 1f);      // #A7001E
private static final Color MENU_RED_TITLE = new Color(194f / 255f, 7f / 255f, 4f / 255f, 1f); // #C20704
private static final Color MENU_GREEN = new Color(103f / 255f, 148f / 255f, 54f / 255f, 1f);  // #679436
private static final Color MENU_BLACK = new Color(0f, 0f, 0f, 1f);                              // #000000

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private BitmapFont titleFont;
    private BitmapFont textFont;
    private BitmapFont smallFont;

    private GlyphLayout glyphLayout;

    private OrthographicCamera camera;
    private Viewport viewport;

    private GameManager gameManager;
    private GameState gameState;

    private int selectedCharacter;
    private String characterName;

    private Texture backgroundSchoolTexture;
    private Texture backgroundMenuTexture;
    private Texture backgroundCharactersTexture;

    private Texture characterAlizeeTexture;
    private Texture characterJeremieTexture;

    private TextureRegion alizeeRegion;
    private TextureRegion jeremieRegion;

    private float stateTime;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        glyphLayout = new GlyphLayout();

        createFonts();
        createCamera();

        gameManager = new GameManager(WORLD_WIDTH, WORLD_HEIGHT);
        gameState = GameState.START;

        selectedCharacter = CHARACTER_ALIZEE;
        characterName = "Alizee";

        loadTextures();
    }

    private void createFonts() {
    titleFont = new BitmapFont();
    titleFont.getData().setScale(2.3f);

    textFont = new BitmapFont();
    textFont.getData().setScale(1.3f);

    smallFont = new BitmapFont();
    smallFont.getData().setScale(1.02f);
}

    private void createCamera() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply();

        camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0f);
        camera.update();
    }

    private void loadTextures() {
        backgroundSchoolTexture = new Texture("background_school.png");
        backgroundMenuTexture = new Texture("background_menu.png");
        backgroundCharactersTexture = new Texture("background_characters.png");

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

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        stateTime += delta;

        update(delta);

        ScreenUtils.clear(0f, 0f, 0f, 1f);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        drawBackground();
        drawOverlays();
        drawGameObjects();
        drawTexts();
    }

    private void update(float delta) {
        handleGlobalInputs();

        switch (gameState) {
            case START:
                updateStartState();
                break;

            case PLAYING:
                gameManager.update(delta);

                if (gameManager.isVictory()) {
                    gameState = GameState.VICTORY;
                } else if (gameManager.isGameOver()) {
                    gameState = GameState.GAME_OVER;
                }
                break;

            case PAUSED:
                break;

            case GAME_OVER:
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    restartGame();
                }
                break;

            case VICTORY:
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    backToMenu();
                }
                break;
        }
    }

    private void handleGlobalInputs() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            backToMenu();
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            if (gameState == GameState.PLAYING) {
                gameState = GameState.PAUSED;
            } else if (gameState == GameState.PAUSED) {
                gameState = GameState.PLAYING;
            }
        }
    }

    private void updateStartState() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            selectedCharacter = CHARACTER_ALIZEE;
            characterName = "Alizee";
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            selectedCharacter = CHARACTER_JEREMIE;
            characterName = "Jeremie";
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            restartGame();
        }
    }

    private void restartGame() {
        gameManager.reset();
        stateTime = 0f;
        gameState = GameState.PLAYING;
    }

    private void backToMenu() {
        gameManager.reset();
        stateTime = 0f;
        gameState = GameState.START;
    }

    private void drawBackground() {
        batch.begin();
        batch.setColor(Color.WHITE);

        if (gameState == GameState.START) {
            batch.draw(backgroundMenuTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        } else {
            batch.draw(backgroundSchoolTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

            if (gameState == GameState.PLAYING) {
                float pulse = 0.03f + 0.02f * (float) Math.sin(stateTime * 2.2f);
                batch.setColor(0f, 0f, 0f, pulse);
                batch.draw(backgroundSchoolTexture, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
            }
        }

        batch.setColor(Color.WHITE);
        batch.end();
    }

    private void drawOverlays() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if (gameState == GameState.START) {
            drawCharacterCardFrame(150f, 130f, CARD_WIDTH, CARD_HEIGHT, selectedCharacter == CHARACTER_ALIZEE);
            drawCharacterCardFrame(550f, 130f, CARD_WIDTH, CARD_HEIGHT, selectedCharacter == CHARACTER_JEREMIE);
        }

        if (gameState == GameState.PLAYING || gameState == GameState.PAUSED) {
            shapeRenderer.setColor(new Color(0.03f, 0.07f, 0.15f, 0.76f));
            shapeRenderer.rect(15, 365, 360, 160);

            shapeRenderer.setColor(new Color(0.03f, 0.07f, 0.15f, 0.76f));
            shapeRenderer.rect(710, 420, 235, 105);

            drawProgressBarBackground(18, 338, 354, 16);
            drawDifficultyBarBackground(728, 438, 190, 12);
            drawLivesBoxes(728, 470);
        }

        if (gameState == GameState.PAUSED) {
            shapeRenderer.setColor(new Color(0.03f, 0.07f, 0.15f, 0.60f));
            shapeRenderer.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);

            shapeRenderer.setColor(new Color(0.98f, 0.98f, 0.99f, 0.94f));
            shapeRenderer.rect(250, 180, 460, 150);
        }

        if (gameState == GameState.GAME_OVER || gameState == GameState.VICTORY) {
            shapeRenderer.setColor(new Color(0.03f, 0.07f, 0.15f, 0.62f));
            shapeRenderer.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);

            shapeRenderer.setColor(new Color(0.98f, 0.98f, 0.99f, 0.94f));
            shapeRenderer.rect(170, 140, 620, 210);
        }

        if (gameState == GameState.PLAYING && gameManager.isPlayerInvulnerable()) {
            float alpha = 0.04f + 0.10f * gameManager.getHitCooldownRatio();
            shapeRenderer.setColor(new Color(1f, 0f, 0f, alpha));
            shapeRenderer.rect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        }

        if (gameState == GameState.PLAYING && gameManager.getLives() == 1) {
            float alpha = 0.14f + 0.05f * (float) Math.sin(stateTime * 7f);
            shapeRenderer.setColor(new Color(0.80f, 0.10f, 0.10f, alpha));
            shapeRenderer.rect(300, 500, 360, 24);
        }

        shapeRenderer.end();

        if (gameState == GameState.PLAYING || gameState == GameState.PAUSED) {
            drawProgressBarFill(20, 340, 350, 12);
            drawDifficultyBarFill(730, 440, 186, 8);
        }
    }

    private void drawCharacterCardFrame(float x, float y, float width, float height, boolean selected) {
    // ombre
    shapeRenderer.setColor(new Color(0f, 0f, 0f, 0.30f));
    shapeRenderer.rect(x + 5f, y - 5f, width, height);

    if (selected) {
        // contour extérieur rouge foncé
        shapeRenderer.setColor(MENU_RED);
        shapeRenderer.rect(x - 7f, y - 7f, width + 14f, height + 14f);

        // contour intérieur clair
        shapeRenderer.setColor(new Color(1f, 0.87f, 0.72f, 1f));
        shapeRenderer.rect(x - 3f, y - 3f, width + 6f, height + 6f);
    } else {
        shapeRenderer.setColor(new Color(0.43f, 0.55f, 0.95f, 1f));
        shapeRenderer.rect(x - 4f, y - 4f, width + 8f, height + 8f);
    }
}

    private void drawProgressBarBackground(float x, float y, float width, float height) {
        shapeRenderer.setColor(new Color(0.80f, 0.84f, 0.90f, 0.95f));
        shapeRenderer.rect(x, y, width, height);
    }

    private void drawProgressBarFill(float x, float y, float width, float height) {
        float progress = gameManager.getProgressRatio();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(1.00f, 0.66f, 0.18f, 1f));
        shapeRenderer.rect(x, y, width * progress, height);
        shapeRenderer.end();
    }

    private void drawDifficultyBarBackground(float x, float y, float width, float height) {
        shapeRenderer.setColor(new Color(0.75f, 0.79f, 0.88f, 0.95f));
        shapeRenderer.rect(x, y, width, height);
    }

    private void drawDifficultyBarFill(float x, float y, float width, float height) {
        float value = Math.min(1f, (gameManager.getDifficultyMultiplier() - 1f) / 2.2f);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(new Color(0.91f, 0.20f, 0.16f, 1f));
        shapeRenderer.rect(x, y, width * value, height);
        shapeRenderer.end();
    }

    private void drawLivesBoxes(float x, float y) {
        int lives = gameManager.getLives();

        for (int i = 0; i < 3; i++) {
            if (i < lives) {
                shapeRenderer.setColor(new Color(0.92f, 0.18f, 0.18f, 1f));
            } else {
                shapeRenderer.setColor(new Color(0.42f, 0.45f, 0.52f, 0.85f));
            }

            shapeRenderer.rect(x + i * 28f, y, 20f, 20f);
        }
    }

    private void drawGameObjects() {
        batch.begin();

        if (gameState == GameState.START) {
            drawCharacterPreview(150f, 130f, CHARACTER_ALIZEE);
            drawCharacterPreview(550f, 130f, CHARACTER_JEREMIE);
            batch.end();
            return;
        }

        Player player = gameManager.getPlayer();
        drawPlayerSprite(player);
        drawNameAbovePlayer(player);

        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Homework hw : gameManager.getHomeworks()) {
            drawHomework(hw);
        }
        shapeRenderer.end();
    }

    private void drawCharacterPreview(float cardX, float cardY, int characterId) {
        TextureRegion region = getCharacterRegion(characterId);

        float bgX = cardX;
        float bgY = cardY;

        batch.setColor(Color.WHITE);
        batch.draw(backgroundCharactersTexture, bgX, bgY, CARD_WIDTH, CARD_HEIGHT);

        float drawX;
        float drawY = cardY + 18f;

        if (characterId == CHARACTER_ALIZEE) {
            drawX = cardX + 58f;
            batch.draw(region, drawX, drawY, 145f, 195f);
        } else {
            drawX = cardX + 66f;
            batch.draw(region, drawX, drawY, 132f, 195f);
        }
    }

    private void drawPlayerSprite(Player player) {
        if (gameManager.isPlayerInvulnerable()) {
            boolean visible = ((int) (stateTime * 14f)) % 2 == 0;
            if (!visible) {
                return;
            }
        }

        TextureRegion region = getCharacterRegion(selectedCharacter);

        float x = player.getX() - PLAYER_DRAW_OFFSET_X;
        float y = player.getY();

        if (player.isMoving()) {
            y += (float) Math.sin(stateTime * 14f) * 2f;
        }

        if (player.getDirection() == Player.Direction.LEFT) {
            batch.setColor(Color.WHITE);
            batch.draw(region, x + PLAYER_DRAW_WIDTH, y, -PLAYER_DRAW_WIDTH, PLAYER_DRAW_HEIGHT);
        } else {
            batch.setColor(Color.WHITE);
            batch.draw(region, x, y, PLAYER_DRAW_WIDTH, PLAYER_DRAW_HEIGHT);
        }
    }

    private TextureRegion getCharacterRegion(int characterId) {
        return characterId == CHARACTER_ALIZEE ? alizeeRegion : jeremieRegion;
    }

    private void drawNameAbovePlayer(Player player) {
        String label = characterName;

        glyphLayout.setText(smallFont, label);
        float textX = player.getX() + (player.getWidth() / 2f) - (glyphLayout.width / 2f);
        float textY = player.getY() + PLAYER_DRAW_HEIGHT + 18f;

        smallFont.setColor(Color.WHITE);
        smallFont.draw(batch, label, textX, textY);
    }

    private void drawHomework(Homework hw) {
        switch (hw.getType()) {
            case NOTEBOOK:
                drawNotebook(hw);
                break;
            case TEST:
                drawTest(hw);
                break;
            case GIANT_PEN:
                drawGiantPen(hw);
                break;
            case COPY:
            default:
                drawCopy(hw);
                break;
        }
    }

    private void drawCopy(Homework hw) {
        shapeRenderer.setColor(new Color(0.99f, 0.99f, 0.99f, 1f));
        shapeRenderer.rect(hw.getX(), hw.getY(), hw.getWidth(), hw.getHeight());

        shapeRenderer.setColor(new Color(0.82f, 0.12f, 0.17f, 1f));
        shapeRenderer.rect(hw.getX() + 5f, hw.getY() + hw.getHeight() - 10f, hw.getWidth() - 10f, 5f);

        shapeRenderer.setColor(new Color(0.75f, 0.82f, 0.95f, 1f));
        shapeRenderer.rect(hw.getX() + 5f, hw.getY() + 12f, hw.getWidth() - 10f, 2f);
        shapeRenderer.rect(hw.getX() + 5f, hw.getY() + 20f, hw.getWidth() - 10f, 2f);
        shapeRenderer.rect(hw.getX() + 5f, hw.getY() + 28f, hw.getWidth() - 10f, 2f);
    }

    private void drawNotebook(Homework hw) {
        shapeRenderer.setColor(new Color(0.18f, 0.42f, 0.88f, 1f));
        shapeRenderer.rect(hw.getX(), hw.getY(), hw.getWidth(), hw.getHeight());

        shapeRenderer.setColor(new Color(0.96f, 0.96f, 0.98f, 1f));
        shapeRenderer.rect(hw.getX() + 8f, hw.getY() + 6f, hw.getWidth() - 12f, hw.getHeight() - 12f);

        shapeRenderer.setColor(new Color(0.78f, 0.12f, 0.18f, 1f));
        shapeRenderer.rect(hw.getX() + 4f, hw.getY(), 4f, hw.getHeight());
    }

    private void drawTest(Homework hw) {
        shapeRenderer.setColor(new Color(1f, 0.97f, 0.97f, 1f));
        shapeRenderer.rect(hw.getX(), hw.getY(), hw.getWidth(), hw.getHeight());

        shapeRenderer.setColor(new Color(0.85f, 0.05f, 0.12f, 1f));
        shapeRenderer.rect(hw.getX() + 7f, hw.getY() + hw.getHeight() - 12f, hw.getWidth() - 14f, 6f);

        shapeRenderer.setColor(new Color(0.85f, 0.05f, 0.12f, 1f));
        shapeRenderer.rect(hw.getX() + hw.getWidth() - 18f, hw.getY() + 10f, 10f, 10f);

        shapeRenderer.setColor(new Color(0.75f, 0.82f, 0.95f, 1f));
        shapeRenderer.rect(hw.getX() + 6f, hw.getY() + 18f, hw.getWidth() - 22f, 2f);
        shapeRenderer.rect(hw.getX() + 6f, hw.getY() + 28f, hw.getWidth() - 22f, 2f);
    }

    private void drawGiantPen(Homework hw) {
        float x = hw.getX();
        float y = hw.getY();
        float w = hw.getWidth();
        float h = hw.getHeight();

        shapeRenderer.setColor(new Color(0.12f, 0.32f, 0.88f, 1f));
        shapeRenderer.rect(x, y + 12f, w, h - 24f);

        shapeRenderer.setColor(new Color(0.95f, 0.92f, 0.82f, 1f));
        shapeRenderer.rect(x + 3f, y + 24f, w - 6f, h - 48f);

        shapeRenderer.setColor(new Color(0.05f, 0.10f, 0.35f, 1f));
        shapeRenderer.rect(x, y + h - 16f, w, 16f);

        shapeRenderer.setColor(new Color(0.78f, 0.80f, 0.84f, 1f));
        shapeRenderer.rect(x, y + h - 24f, w, 8f);

        shapeRenderer.setColor(new Color(0.94f, 0.78f, 0.52f, 1f));
        shapeRenderer.triangle(
            x, y + 12f,
            x + w, y + 12f,
            x + w / 2f, y
        );

        shapeRenderer.setColor(new Color(0.15f, 0.15f, 0.15f, 1f));
        shapeRenderer.triangle(
            x + w * 0.35f, y + 6f,
            x + w * 0.65f, y + 6f,
            x + w / 2f, y - 6f
        );
    }

    private void drawTexts() {
        batch.begin();

        int currentScore = gameManager.getCurrentScore();
        int bestScore = gameManager.getBestScore();
        int lives = gameManager.getLives();

        if (gameState == GameState.START) {
            drawStartTexts();
        }

        if (gameState == GameState.PLAYING) {
            drawHudTexts(currentScore, bestScore, lives);

            smallFont.setColor(new Color(1f, 0.88f, 0.46f, 1f));
            smallFont.draw(batch, "Progression vers la victoire", 18, 334);

            smallFont.setColor(new Color(0.92f, 0.94f, 0.98f, 1f));
            smallFont.draw(batch, "P = Pause | ESC = Menu", 18, 316);

            if (lives == 1) {
                float pulse = 0.75f + 0.25f * (float) Math.sin(stateTime * 8f);
                textFont.setColor(1f, 0.22f, 0.18f, pulse);
                textFont.draw(batch, "ATTENTION : DERNIERE VIE", 325, 518);
            }
        }

        if (gameState == GameState.PAUSED) {
            drawHudTexts(currentScore, bestScore, lives);

            titleFont.setColor(new Color(0.05f, 0.11f, 0.27f, 1f));
            titleFont.draw(batch, "PAUSE", 390, 285);

            textFont.setColor(new Color(0.82f, 0.32f, 0.10f, 1f));
            textFont.draw(batch, "Appuie sur P pour reprendre", 315, 235);
        }

        if (gameState == GameState.GAME_OVER) {
            titleFont.setColor(new Color(0.82f, 0.12f, 0.17f, 1f));
            titleFont.draw(batch, "GAME OVER", 325, 315);

            textFont.setColor(new Color(0.05f, 0.11f, 0.27f, 1f));
            textFont.draw(batch, characterName + " a ete rattrape par les devoirs...", 248, 274);
            textFont.draw(batch, "Score final : " + currentScore, 350, 238);
            textFont.draw(batch, "Meilleur score : " + bestScore, 330, 208);
            textFont.draw(batch, "ESPACE = Rejouer", 360, 178);
        }

        if (gameState == GameState.VICTORY) {
            titleFont.setColor(new Color(0.10f, 0.56f, 0.22f, 1f));
            titleFont.draw(batch, "VICTOIRE MORALE !", 242, 315);

            textFont.setColor(new Color(0.05f, 0.11f, 0.27f, 1f));
            textFont.draw(batch, characterName + " a survecu au chaos scolaire !", 248, 274);
            textFont.draw(batch, "Score final : " + currentScore, 350, 202);
            textFont.draw(batch, "ESPACE = Retour Menu", 342, 172);
        }

        batch.end();
    }

    private void drawCenteredText(BitmapFont font, String text, float centerX, float y, Color color) {
    glyphLayout.setText(font, text);
    font.setColor(color);
    font.draw(batch, text, centerX - glyphLayout.width / 2f, y);
}

private void drawMenuCharacterLabel(String text, float cardX, float cardWidth, float y, Color color) {
    float centerX = cardX + cardWidth / 2f;

    glyphLayout.setText(textFont, text);
    textFont.setColor(new Color(0f, 0f, 0f, 0.45f));
    textFont.draw(batch, text, centerX - glyphLayout.width / 2f + 2f, y - 2f);

    drawCenteredText(textFont, text, centerX, y, color);
}
   private void drawStartTexts() {
    float menuCenterX = WORLD_WIDTH / 2f;

    float leftCardX = 150f;
    float rightCardX = 550f;

    // =========================
    // TITRE PRINCIPAL
    // =========================
    String word1 = "MADININA";
    String word2 = " DODGE";
    String word3 = " SCHOOL";

    glyphLayout.setText(titleFont, word1);
    float width1 = glyphLayout.width;

    glyphLayout.setText(titleFont, word2);
    float width2 = glyphLayout.width;

    glyphLayout.setText(titleFont, word3);
    float width3 = glyphLayout.width;

    float totalWidth = width1 + width2 + width3;
    float startX = menuCenterX - totalWidth / 2f;
    float titleY = 480f;

    titleFont.setColor(MENU_RED_TITLE);
    titleFont.draw(batch, word1, startX, titleY);

    titleFont.setColor(MENU_GREEN);
    titleFont.draw(batch, word2, startX + width1, titleY);

    // SCHOOL un peu moins noir pur pour être plus lisible
    titleFont.setColor(new Color(0.08f, 0.08f, 0.08f, 1f));
    titleFont.draw(batch, word3, startX + width1 + width2, titleY);

    // =========================
    // SOUS-TITRE
    // =========================
    drawCenteredText(
        textFont,
        "Choisis ton personnage",
        menuCenterX,
        430f,
        new Color(1.00f, 0.58f, 0.10f, 1f)
    );

    // =========================
    // LABELS SOUS LES CARTES
    // =========================
    if (selectedCharacter == CHARACTER_ALIZEE) {
        drawMenuCharacterLabel(
            "1 = Alizee selectionnee",
            leftCardX,
            CARD_WIDTH,
            88f,
            MENU_RED
        );

        drawMenuCharacterLabel(
            "2 = Jeremie",
            rightCardX,
            CARD_WIDTH,
            88f,
            MENU_RED
        );
    } else {
        drawMenuCharacterLabel(
            "1 = Alizee",
            leftCardX,
            CARD_WIDTH,
            88f,
            MENU_RED
        );

        drawMenuCharacterLabel(
            "2 = Jeremie selectionne",
            rightCardX,
            CARD_WIDTH,
            88f,
            MENU_RED
        );
    }

    // =========================
    // TEXTE BAS MENU
    // =========================
    drawCenteredText(
        titleFont,
        "ENTREE = Commencer",
        menuCenterX,
        62f,
        Color.WHITE
    );

    drawCenteredText(
        smallFont,
        "Deplace-toi avec Fleches, Q/D ou A/D",
        menuCenterX,
        34f,
        Color.WHITE
    );
}

    private void drawHudTexts(int currentScore, int bestScore, int lives) {
        textFont.setColor(Color.WHITE);
        textFont.draw(batch, "Personnage : " + characterName, 28, 510);
        textFont.draw(batch, "Score : " + currentScore, 28, 480);
        textFont.draw(batch, "Meilleur score : " + bestScore, 28, 450);
        textFont.draw(batch, "Vies : " + lives, 28, 420);
        textFont.draw(batch, "Objectif : " + gameManager.getTargetScore(), 28, 390);

        textFont.setColor(new Color(1f, 0.84f, 0.38f, 1f));
        textFont.draw(batch, "Difficulte : " + gameManager.getDifficultyLabel(), 728, 515);

        smallFont.setColor(Color.WHITE);
        smallFont.draw(batch, "Intensite", 728, 435);
        smallFont.draw(batch, "Vies", 728, 466);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();

        titleFont.dispose();
        textFont.dispose();
        smallFont.dispose();

        if (backgroundSchoolTexture != null) {
            backgroundSchoolTexture.dispose();
        }
        if (backgroundMenuTexture != null) {
            backgroundMenuTexture.dispose();
        }
        if (backgroundCharactersTexture != null) {
            backgroundCharactersTexture.dispose();
        }
        if (characterAlizeeTexture != null) {
            characterAlizeeTexture.dispose();
        }
        if (characterJeremieTexture != null) {
            characterJeremieTexture.dispose();
        }
    }
}