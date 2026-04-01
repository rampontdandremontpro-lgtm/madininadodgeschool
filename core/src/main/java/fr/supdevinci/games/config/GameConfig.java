package fr.supdevinci.games.config;

import com.badlogic.gdx.graphics.Color;

public final class GameConfig {

    private GameConfig() {
    }

    public static final float WORLD_WIDTH = 960f;
    public static final float WORLD_HEIGHT = 540f;

    public static final int CHARACTER_ALIZEE = 1;
    public static final int CHARACTER_JEREMIE = 2;

    public static final float PLAYER_DRAW_WIDTH = 130f;
    public static final float PLAYER_DRAW_HEIGHT = 200f;
    public static final float PLAYER_DRAW_OFFSET_X = 38f;

    public static final float CARD_WIDTH = 260f;
    public static final float CARD_HEIGHT = 250f;

    public static final Color MENU_RED = new Color(167f / 255f, 0f / 255f, 30f / 255f, 1f);       // #A7001E
    public static final Color MENU_RED_TITLE = new Color(194f / 255f, 7f / 255f, 4f / 255f, 1f);  // #C20704
    public static final Color MENU_GREEN = new Color(103f / 255f, 148f / 255f, 54f / 255f, 1f);   // #679436
    public static final Color MENU_BLACK = new Color(0.08f, 0.08f, 0.08f, 1f);
}