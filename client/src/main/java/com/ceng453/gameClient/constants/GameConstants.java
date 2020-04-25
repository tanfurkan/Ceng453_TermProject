package com.ceng453.gameClient.constants;

import javafx.scene.paint.Color;

public class GameConstants {
    public static int PLAYER_INITIAL_HEALTH = 5;

    public static int PLAYER_RADIUS = 20;
    public static double PLAYER_BULLET_SPEED = 5;
    public static double PLAYER_BULLET_GENERATION_DURATION = 0.8;
    public static double PLAYER_BULLET_MOVE_DURATION = 0.02; // Should be small for smooth move
    public static Color PLAYER_BULLET_COLOR = Color.VIOLET;

    public static int ALIEN_RADIUS = 20;
    public static double ALIEN_BULLET_GENERATION_DURATION = 1;
    public static double ALIEN_BULLET_MOVE_DURATION = 0.02; // Should be small for smooth move
    public static Color ALIEN_BULLET_COLOR = Color.RED;

    public static int LEVEL_ONE_ALIEN_HEALTH = 1;
    public static int LEVEL_ONE_ALIEN_SCORE = 100;
    public static double LEVEL_ONE_ALIEN_BULLET_SPEED = 3; // 3 5 10 Preferred
    public static Color LEVEL_ONE_ALIEN_COLOR = Color.GREEN;

    public static int BULLET_RADIUS = 3;
}
