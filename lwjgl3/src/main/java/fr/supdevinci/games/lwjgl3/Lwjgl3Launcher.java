package fr.supdevinci.games.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import fr.supdevinci.games.Main;

public class Lwjgl3Launcher {

    private static final int WINDOW_WIDTH = 960;
    private static final int WINDOW_HEIGHT = 540;
    private static final String GAME_TITLE = "Madinina Dodge School";

    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new Main(), createConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration createConfiguration() {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setTitle(GAME_TITLE);
        config.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);

        config.useVsync(true);
        config.setForegroundFPS(60);

        // option bonus : empêche le redimensionnement (plus propre pour votre jeu)
        config.setResizable(false);

        config.setWindowIcon(
            "libgdx128.png",
            "libgdx64.png",
            "libgdx32.png",
            "libgdx16.png"
        );

        return config;
    }
}