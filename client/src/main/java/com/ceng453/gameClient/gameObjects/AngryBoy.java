package com.ceng453.gameClient.gameObjects;

import javafx.scene.shape.Polygon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AngryBoy implements Alien {

    private Integer health = 2;
    private Polygon triangle = new Polygon();

    AngryBoy(){
        triangle.getPoints().addAll(50.0, 50.0,
                60.0, 60.0,
                20.0, 40.0);
    }

    @Override
    public void attack() {

    }

    @Override
    public void spawn(double x, double y) {

    }

    @Override
    public void updatePosition(double x, double y) {

    }

}
