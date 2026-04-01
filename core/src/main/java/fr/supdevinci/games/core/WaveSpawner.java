package fr.supdevinci.games.core;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import fr.supdevinci.games.model.Homework;

public class WaveSpawner {

    private final HomeworkFactory homeworkFactory;
    private final float worldWidth;
    private final float worldHeight;

    public WaveSpawner(HomeworkFactory homeworkFactory, float worldWidth, float worldHeight) {
        this.homeworkFactory = homeworkFactory;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public void spawnWave(GamePhase phase, Array<Homework> homeworks) {
        if (phase == GamePhase.REVISION) {
            spawnRevisionWave(homeworks);
            return;
        }

        spawnRegularHomework(phase, homeworks);

        switch (phase) {
            case SECONDE:
                trySpawnRegular(phase, homeworks, 0.30f);
                break;

            case PREMIERE:
                trySpawnRegular(phase, homeworks, 0.55f);
                trySpawnRegular(phase, homeworks, 0.25f);
                break;

            case TERMINALE:
                trySpawnRegular(phase, homeworks, 0.75f);
                trySpawnRegular(phase, homeworks, 0.50f);
                trySpawnRegular(phase, homeworks, 0.30f);
                break;

            default:
                break;
        }
    }

    private void spawnRevisionWave(Array<Homework> homeworks) {
        spawnSnackOrRare(homeworks);

        if (MathUtils.randomBoolean(0.55f)) {
            spawnSnackOrRare(homeworks);
        }

        if (MathUtils.randomBoolean(0.20f)) {
            spawnSnackOrRare(homeworks);
        }
    }

    private void trySpawnRegular(GamePhase phase, Array<Homework> homeworks, float probability) {
        if (MathUtils.randomBoolean(probability)) {
            spawnRegularHomework(phase, homeworks);
        }
    }

    private void spawnRegularHomework(GamePhase phase, Array<Homework> homeworks) {
        homeworks.add(homeworkFactory.createRegularHomework(phase, worldWidth, worldHeight));
    }

    private void spawnSnackOrRare(Array<Homework> homeworks) {
        homeworks.add(homeworkFactory.createSnackOrRare(worldWidth, worldHeight));
    }
}