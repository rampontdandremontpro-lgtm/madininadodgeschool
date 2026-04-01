package fr.supdevinci.games.render.startmenu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.supdevinci.games.assets.GameAssets;
import fr.supdevinci.games.config.GameConfig;

public class StartMenuBackgroundRenderer {

    private final GameAssets assets;

    public StartMenuBackgroundRenderer(GameAssets assets) {
        this.assets = assets;
    }

    public void render() {
        SpriteBatch batch = assets.getBatch();
        Texture background = assets.getBackgroundMenuTexture();

        batch.begin();
        batch.setColor(Color.WHITE);
        batch.draw(background, 0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        batch.end();
    }
}