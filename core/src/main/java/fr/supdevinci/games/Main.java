package fr.supdevinci.games;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fr.supdevinci.games.assets.GameAssets;
import fr.supdevinci.games.config.GameConfig;
import fr.supdevinci.games.core.GameController;
import fr.supdevinci.games.core.GameManager;
import fr.supdevinci.games.core.GameSession;
import fr.supdevinci.games.render.GameplayRenderer;
import fr.supdevinci.games.render.StartMenuRenderer;
import fr.supdevinci.games.core.GameLogic;
import fr.supdevinci.games.core.GameLogic;
import fr.supdevinci.games.decorator.RevisionGameLogicDecorator;

public class Main extends ApplicationAdapter {

    private OrthographicCamera camera;
    private Viewport viewport;

    private GameAssets assets;
    private GameSession session;
    private GameController controller;
    private StartMenuRenderer startMenuRenderer;
    private GameplayRenderer gameplayRenderer;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        viewport.apply();
        camera.position.set(GameConfig.WORLD_WIDTH / 2f, GameConfig.WORLD_HEIGHT / 2f, 0f);
        camera.update();

        assets = new GameAssets();
        assets.load();

        GameLogic gameManager = new GameManager(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        gameManager = new RevisionGameLogicDecorator(gameManager);
        session = new GameSession(gameManager);

        controller = new GameController(session);
        startMenuRenderer = new StartMenuRenderer(assets, session);
        gameplayRenderer = new GameplayRenderer(assets, session);
    }

    @Override
    public void render() {
        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        session.addStateTime(delta);

        controller.update(delta);

        ScreenUtils.clear(0f, 0f, 0f, 1f);

        camera.update();
        assets.getBatch().setProjectionMatrix(camera.combined);
        assets.getShapeRenderer().setProjectionMatrix(camera.combined);

        if (session.isStart()) {
            startMenuRenderer.render();
        } else {
            gameplayRenderer.render();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        assets.dispose();
    }
}