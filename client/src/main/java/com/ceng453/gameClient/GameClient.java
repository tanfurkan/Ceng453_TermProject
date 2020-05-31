package com.ceng453.gameClient;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class GameClient extends SpringBootServletInitializer {

    public static void main(String[] args) {
        Application.launch(GameClientApplication.class, args);
    }

}
