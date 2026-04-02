package fr.supdevinci.games.render.gameplay;

import fr.supdevinci.games.assets.GameAssets;
import fr.supdevinci.games.core.GameSession;
import fr.supdevinci.games.render.gameplay.hud.GameplayHudRenderer;

/**
 * Gère tout le rendu graphique du jeu :
 * - fond
 * - joueur
 * - objets
 * - interface (HUD)
 */
public class GameplayRenderer {

    private final GameplayBackgroundRenderer backgroundRenderer;
    private final GameplayObjectRenderer objectRenderer;
    private final GameplayHudRenderer hudRenderer;

    public GameplayRenderer(GameAssets assets, GameSession session) {
        this.backgroundRenderer = new GameplayBackgroundRenderer(assets, session);
        this.objectRenderer = new GameplayObjectRenderer(assets, session);
        this.hudRenderer = new GameplayHudRenderer(assets, session);
    }
    
    /**
    * Affiche une frame complète du jeu.
    */
    public void render() {
        backgroundRenderer.render();
        objectRenderer.render();
        hudRenderer.render();
    }
}