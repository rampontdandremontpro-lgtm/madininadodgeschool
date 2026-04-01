package fr.supdevinci.games.render.gameplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.supdevinci.games.assets.BackgroundAsset;
import fr.supdevinci.games.assets.GameAssets;
import fr.supdevinci.games.config.GameConfig;
import fr.supdevinci.games.core.GameLogic;
import fr.supdevinci.games.core.GameSession;

public class GameplayBackgroundRenderer {

    private final GameAssets assets;
    private final GameSession session;

    public GameplayBackgroundRenderer(GameAssets assets, GameSession session) {
        this.assets = assets;
        this.session = session;
    }

    public void render() {
        SpriteBatch batch = assets.getBatch();
        Texture background = resolveBackground();

        batch.begin();
        batch.setColor(Color.WHITE);
        batch.draw(background, 0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        if (session.isPlaying()) {
            float pulse = 0.03f + 0.02f * (float) Math.sin(session.getStateTime() * 2.2f);
            batch.setColor(0f, 0f, 0f, pulse);
            batch.draw(background, 0, 0, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);
        }

        batch.setColor(Color.WHITE);
        batch.end();
    }

    private Texture resolveBackground() {
        GameLogic gm = session.getGameManager();

        if (gm.isRevisionMode()) {
            return assets.getBackground(BackgroundAsset.REVISION);
        }

        int elapsedTime = gm.getCurrentScore();

        if (elapsedTime < 30) {
            return assets.getBackground(BackgroundAsset.SCHOOL);
        }
        if (elapsedTime < 70) {
            return assets.getBackground(BackgroundAsset.TERRAIN);
        }
        return assets.getBackground(BackgroundAsset.CLASSROOM);
    }
}