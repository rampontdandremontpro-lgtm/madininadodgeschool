package fr.supdevinci.games.render.gameplay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import fr.supdevinci.games.assets.GameAssets;
import fr.supdevinci.games.config.GameConfig;
import fr.supdevinci.games.core.GameLogic;
import fr.supdevinci.games.core.GameSession;
import fr.supdevinci.games.model.Homework;
import fr.supdevinci.games.model.Player;

import java.util.EnumMap;
import java.util.Map;

public class GameplayObjectRenderer {

    private final GameAssets assets;
    private final GameSession session;
    private final Map<Homework.HomeworkType, HomeworkRenderStyle> renderStyles = new EnumMap<>(Homework.HomeworkType.class);

    public GameplayObjectRenderer(GameAssets assets, GameSession session) {
        this.assets = assets;
        this.session = session;
        initStyles();
    }

    public void render() {
        SpriteBatch batch = assets.getBatch();
        GameLogic gm = session.getGameManager();
        Player player = gm.getPlayer();

        batch.begin();
        drawPlayerSprite(player);
        drawNameAbovePlayer(player);

        for (Homework hw : gm.getHomeworks()) {
            drawHomeworkTexture(batch, hw);
        }

        batch.end();
    }

    private void initStyles() {
        renderStyles.put(Homework.HomeworkType.COPY, new HomeworkRenderStyle(2.0f, 2.0f));
        renderStyles.put(Homework.HomeworkType.NOTEBOOK, new HomeworkRenderStyle(1.75f, 1.75f));
        renderStyles.put(Homework.HomeworkType.TEST, new HomeworkRenderStyle(2.0f, 2.0f));
        renderStyles.put(Homework.HomeworkType.GIANT_PEN, new HomeworkRenderStyle(3.0f, 1.75f));
        renderStyles.put(Homework.HomeworkType.POMME_CANNELLE, new HomeworkRenderStyle(1.55f, 1.55f));
        renderStyles.put(Homework.HomeworkType.PATE_GOYAVE, new HomeworkRenderStyle(1.55f, 1.40f));
        renderStyles.put(Homework.HomeworkType.CARESSE_RARE, new HomeworkRenderStyle(3f, 1.65f));
    }

    private void drawPlayerSprite(Player player) {
        if (session.getGameManager().isPlayerInvulnerable()) {
            boolean visible = ((int) (session.getStateTime() * 14f)) % 2 == 0;
            if (!visible) {
                return;
            }
        }

        SpriteBatch batch = assets.getBatch();
        TextureRegion region = assets.getCharacterRegion(session.getSelectedCharacter());

        float x = player.getX() - GameConfig.PLAYER_DRAW_OFFSET_X;
        float y = player.getY();

        if (player.isMoving()) {
            y += (float) Math.sin(session.getStateTime() * 14f) * 2f;
        }

        batch.setColor(Color.WHITE);
        if (player.getDirection() == Player.Direction.LEFT) {
            batch.draw(region, x + GameConfig.PLAYER_DRAW_WIDTH, y, -GameConfig.PLAYER_DRAW_WIDTH, GameConfig.PLAYER_DRAW_HEIGHT);
        } else {
            batch.draw(region, x, y, GameConfig.PLAYER_DRAW_WIDTH, GameConfig.PLAYER_DRAW_HEIGHT);
        }
    }

    private void drawNameAbovePlayer(Player player) {
        String label = session.getCharacterName();

        assets.getGlyphLayout().setText(assets.getSmallFont(), label);
        float textX = player.getX() + (player.getWidth() / 2f) - (assets.getGlyphLayout().width / 2f);
        float textY = player.getY() + GameConfig.PLAYER_DRAW_HEIGHT + 18f;

        assets.getSmallFont().setColor(0f, 0f, 0f, 0.35f);
        assets.getSmallFont().draw(assets.getBatch(), label, textX + 1.5f, textY - 1.5f);

        assets.getSmallFont().setColor(new Color(0.96f, 0.97f, 1f, 1f));
        assets.getSmallFont().draw(assets.getBatch(), label, textX, textY);
    }

    private void drawHomeworkTexture(SpriteBatch batch, Homework hw) {
        Texture texture = assets.getHomeworkTexture(hw.getType());
        HomeworkRenderStyle style = renderStyles.get(hw.getType());

        float drawWidth = hw.getWidth() * style.getScaleX();
        float drawHeight = hw.getHeight() * style.getScaleY();
        float drawX = hw.getX() - (drawWidth - hw.getWidth()) / 2f;
        float drawY = hw.getY() - (drawHeight - hw.getHeight()) / 2f;

        batch.setColor(Color.WHITE);
        batch.draw(texture, drawX, drawY, drawWidth, drawHeight);
    }
}