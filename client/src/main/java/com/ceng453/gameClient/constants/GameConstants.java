package com.ceng453.gameClient.constants;

import javafx.scene.paint.Color;

public class GameConstants {
    public static int PLAYER_INITIAL_HEALTH = 5;
    public static String username;

    public static int PLAYER_RADIUS = 20;
    public static double PLAYER_BULLET_SPEED = 5;
    public static double PLAYER_BULLET_GENERATION_DURATION = 0.8;
    public static double PLAYER_BULLET_MOVE_DURATION = 0.02; // Should be small for smooth move
    public static Color PLAYER_BULLET_COLOR = Color.VIOLET;

    public static int ALIEN_RADIUS = 20;
    public static double ALIEN_BULLET_GENERATION_DURATION = 1;
    public static double ALIEN_BULLET_MOVE_DURATION = 0.02; // Should be small for smooth move
    public static Color ALIEN_BULLET_COLOR = Color.RED;

    public static int LEVEL_ONE_ALIEN_HEALTH = 2;
    public static int LEVEL_ONE_ALIEN_SCORE = 100;
    public static double LEVEL_ONE_ALIEN_BULLET_SPEED = 3; // 3 5 10 Preferred
    public static Color LEVEL_ONE_ALIEN_COLOR = Color.GREEN;
    public static int LEVEL_ONE_ALIEN_NUMBER_LINE = 8;

    public static int LEVEL_TWO_ALIEN_HEALTH = 2;
    public static int LEVEL_TWO_ALIEN_SCORE = 300;
    public static double LEVEL_TWO_ALIEN_BULLET_SPEED = 5; // 3 5 10 Preferred
    public static Color LEVEL_TWO_ALIEN_COLOR = Color.CORAL;
    public static int LEVEL_TWO_ALIEN_NUMBER_LINE = 8;

    public static int LEVEL_THREE_ALIEN_HEALTH = 4;
    public static int LEVEL_THREE_ALIEN_SCORE = 500;
    public static double LEVEL_THREE_ALIEN_BULLET_SPEED = 7; // 3 5 10 Preferred
    public static Color LEVEL_THREE_ALIEN_COLOR = Color.HONEYDEW;
    public static int LEVEL_THREE_ALIEN_NUMBER_LINE = 10;

    public static int LEVEL_FOUR_ALIEN_NUMBER_LINE = 12;

    public static int BOSS_HEALTH = 20;
    public static int BOSS_SCORE = 250;
    public static double BOSS_BULLET_SPEED = 10; // 3 5 10 Preferred
    public static Color BOSS_ALIEN_COLOR = Color.PERU;
    public static int BOSS_RADIUS = 50;
    public static double BOSS_BULLET_GENERATION_DURATION = 0.5;
    public static Color BOSS_BULLET_COLOR = Color.YELLOWGREEN;


    public static int BULLET_RADIUS = 3;
}
