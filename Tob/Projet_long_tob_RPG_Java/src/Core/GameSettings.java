package Core;

import GeometricObjects.Vector2D;

public class GameSettings {
    //Screen Settings
    public static final int SCALE = 2;
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    public static final Vector2D SCREEN_CENTER = new Vector2D(SCREEN_WIDTH/2, SCREEN_HEIGHT/2);

    //Performance Settings
    public static final int TARGET_FPS = 60;
    public static final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
}
