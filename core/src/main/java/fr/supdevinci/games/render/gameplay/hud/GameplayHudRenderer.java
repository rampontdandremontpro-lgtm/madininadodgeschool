package fr.supdevinci.games.render.gameplay.hud;

import fr.supdevinci.games.assets.GameAssets;
import fr.supdevinci.games.core.GameSession;

public class GameplayHudRenderer {

    private final HudPanelRenderer panelRenderer;
    private final HudTextRenderer textRenderer;
    private final HudOverlayRenderer overlayRenderer;

    public GameplayHudRenderer(GameAssets assets, GameSession session) {
        this.panelRenderer = new HudPanelRenderer(assets, session);
        this.textRenderer = new HudTextRenderer(assets, session);
        this.overlayRenderer = new HudOverlayRenderer(assets, session);
    }

    public void render() {
        panelRenderer.render();
        textRenderer.render();
        overlayRenderer.render();
    }
}