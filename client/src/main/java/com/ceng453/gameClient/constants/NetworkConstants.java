package com.ceng453.gameClient.constants;

public class NetworkConstants {
    public static final String API = "http://localhost:8080/";
    public static String jwtToken;

    public static String MULTIPLAYER_IP = "127.0.0.1";
    public static int MULTIPLAYER_PORT = 8084;

    public static String START_SIGNAL = "S";
    public static String INTRODUCTION_SIGNAL = "I";
    public static String LOCATION_SIGNAL = "L";
    public static String GAME_END_SIGNAL = "E";
    public static String ERROR_SIGNAL = "ERR";

    public static String SIGNAL_PARAM_TOKEN = "/";
    public static String LOCATION_TOKEN = "-";

    public static int SLEEP_TIME = 10;
}
