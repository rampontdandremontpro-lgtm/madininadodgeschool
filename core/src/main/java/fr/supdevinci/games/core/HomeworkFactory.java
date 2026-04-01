package fr.supdevinci.games.core;

import com.badlogic.gdx.math.MathUtils;
import fr.supdevinci.games.model.Homework;

public class HomeworkFactory {

    public Homework createRegularHomework(GamePhase phase, float worldWidth, float worldHeight) {
        Homework.HomeworkType type = randomTypeByPhase(phase);
        return createHomeworkByType(type, phase, worldWidth, worldHeight);
    }

    public Homework createSnackOrRare(float worldWidth, float worldHeight) {
        int roll = MathUtils.random(0, 99);
        Homework.HomeworkType type;

        if (roll < 45) {
            type = Homework.HomeworkType.POMME_CANNELLE;
        } else if (roll < 90) {
            type = Homework.HomeworkType.PATE_GOYAVE;
        } else {
            type = Homework.HomeworkType.CARESSE_RARE;
        }

        return createHomeworkByType(type, GamePhase.REVISION, worldWidth, worldHeight);
    }

    private Homework createHomeworkByType(Homework.HomeworkType type, GamePhase phase, float worldWidth, float worldHeight) {
        float width;
        float height;
        float speed;

        if (type == Homework.HomeworkType.POMME_CANNELLE) {
            width = 62f;
            height = 62f;
            speed = 180f;
        } else if (type == Homework.HomeworkType.PATE_GOYAVE) {
            width = 64f;
            height = 52f;
            speed = 180f;
        } else if (type == Homework.HomeworkType.CARESSE_RARE) {
            width = 48f;
            height = 72f;
            speed = 170f;
        } else if (phase == GamePhase.SECONDE) {
            switch (type) {
                case NOTEBOOK:
                    width = 60f;
                    height = 70f;
                    speed = 225f;
                    break;
                case TEST:
                    width = 48f;
                    height = 58f;
                    speed = 265f;
                    break;
                case GIANT_PEN:
                    width = 28f;
                    height = 120f;
                    speed = 250f;
                    break;
                case COPY:
                default:
                    width = 58f;
                    height = 68f;
                    speed = 235f;
                    break;
            }
        } else if (phase == GamePhase.PREMIERE) {
            switch (type) {
                case NOTEBOOK:
                    width = 66f;
                    height = 76f;
                    speed = 285f;
                    break;
                case TEST:
                    width = 52f;
                    height = 62f;
                    speed = 335f;
                    break;
                case GIANT_PEN:
                    width = 32f;
                    height = 134f;
                    speed = 320f;
                    break;
                case COPY:
                default:
                    width = 62f;
                    height = 72f;
                    speed = 290f;
                    break;
            }
        } else {
            switch (type) {
                case NOTEBOOK:
                    width = 70f;
                    height = 80f;
                    speed = 340f;
                    break;
                case TEST:
                    width = 54f;
                    height = 64f;
                    speed = 410f;
                    break;
                case GIANT_PEN:
                    width = 34f;
                    height = 145f;
                    speed = 395f;
                    break;
                case COPY:
                default:
                    width = 64f;
                    height = 74f;
                    speed = 350f;
                    break;
            }
        }

        float x = MathUtils.random(0f, worldWidth - width);
        float y = worldHeight;

        return new Homework(x, y, width, height, speed, type);
    }

    private Homework.HomeworkType randomTypeByPhase(GamePhase phase) {
        int roll = MathUtils.random(0, 99);

        if (phase == GamePhase.SECONDE) {
            if (roll < 45) return Homework.HomeworkType.COPY;
            if (roll < 80) return Homework.HomeworkType.NOTEBOOK;
            if (roll < 95) return Homework.HomeworkType.TEST;
            return Homework.HomeworkType.GIANT_PEN;
        }

        if (phase == GamePhase.PREMIERE) {
            if (roll < 24) return Homework.HomeworkType.COPY;
            if (roll < 46) return Homework.HomeworkType.NOTEBOOK;
            if (roll < 73) return Homework.HomeworkType.TEST;
            return Homework.HomeworkType.GIANT_PEN;
        }

        if (roll < 18) return Homework.HomeworkType.COPY;
        if (roll < 36) return Homework.HomeworkType.NOTEBOOK;
        if (roll < 63) return Homework.HomeworkType.TEST;
        return Homework.HomeworkType.GIANT_PEN;
    }
}