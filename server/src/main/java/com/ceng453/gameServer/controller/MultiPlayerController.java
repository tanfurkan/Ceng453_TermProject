package com.ceng453.gameServer.controller;

import com.ceng453.gameServer.dao.NetworkDAO;
import com.ceng453.gameServer.services.MultiPlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MultiPlayerController {

    private final MultiPlayerService multiPlayerService;

    @PostMapping("/multiplayer")
    public NetworkDAO multiPlayer(@RequestBody NetworkDAO networkDAO) {
        return multiPlayerService.multiPlayer(networkDAO);
    }
}
