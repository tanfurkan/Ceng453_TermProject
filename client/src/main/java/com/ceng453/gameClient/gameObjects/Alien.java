package com.ceng453.gameClient.gameObjects;

public interface Alien {

    void attack();
    void spawn(double x, double y);
    void updatePosition(double x, double y);
    }
