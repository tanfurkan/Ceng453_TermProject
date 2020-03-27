package com.ceng453.gameServer.controller;

import com.ceng453.gameServer.model.User;
import com.ceng453.gameServer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public boolean login(@RequestBody User requestUser)
    {
        return userService.login(requestUser);
    }

    @RequestMapping("/getUserID/{username}")
    public Long getUserID(@PathVariable String username)
    {
        return userService.getUserID(username);
    }

    // Get all Users
    @GetMapping("/users")
    public List<User> getAllUsers()
    {
        return userService.getAllUsers();
    }

    // Get User using Id
    @RequestMapping("/users/{Id}")
    public User getUser(@PathVariable Long Id)
    {
        return userService.getUser(Id);
    }

    // Add User using user object
    @PostMapping("/register/")
    public void register(@RequestBody User user)
    {
        userService.register(user);
    }

    // update User using user object and user Id from URL
    @PutMapping("/profile/{Id}")
    public void updateUser(@RequestBody User user, @PathVariable Long Id)
    {
        userService.updateUser(user, Id);
    }

    // delete User with using user Id from URL
    @DeleteMapping("/profile/{Id}")
    public void deleteUser(@PathVariable Long Id)
    {
        userService.deleteUser(Id);
    }

}
