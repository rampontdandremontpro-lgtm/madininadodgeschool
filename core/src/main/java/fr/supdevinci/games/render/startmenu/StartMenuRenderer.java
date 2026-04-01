package fr.supdevinci.games.render.startmenu;

import fr.supdevinci.games.assets.GameAssets;
import fr.supdevinci.games.core.GameSession;

public class StartMenuRenderer {

    private final StartMenuBackgroundRenderer backgroundRenderer;
    private final StartMenuCardRenderer cardRenderer;
    private final StartMenuTextRenderer textRenderer;

    public StartMenuRenderer(GameAssets assets, GameSession session) {
        this.backgroundRenderer = new StartMenuBackgroundRenderer(assets);
        this.cardRenderer = new StartMenuCardRenderer(assets, session);
        this.textRenderer = new StartMenuTextRenderer(assets, session);
    }

    public void render() {
        backgroundRenderer.render();
        cardRenderer.render();
        textRenderer.render();
    }
}