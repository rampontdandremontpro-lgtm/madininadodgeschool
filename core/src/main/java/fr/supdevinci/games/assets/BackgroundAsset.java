package fr.supdevinci.games.assets;

public enum BackgroundAsset {
    MENU("background_menu.png"),
    CHARACTERS("background_characters.png"),
    SCHOOL("background_school.png"),
    TERRAIN("background_terrain.png"),
    CLASSROOM("background_classroom.png"),
    REVISION("background_revision.png");

    private final String fileName;

    BackgroundAsset(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}