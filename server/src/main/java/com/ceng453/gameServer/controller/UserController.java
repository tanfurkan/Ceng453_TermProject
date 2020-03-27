package com.ceng453.gameServer.controller;

import com.ceng453.gameServer.model.User;
import com.ceng453.gameServer.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public boolean login(@RequestBody User user)
    {
        return userService.login(user);
    }

    @GetMapping("/getUserID")
    public Long getUserID(@RequestParam(value = "username") String username)
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
    @GetMapping("/profile")
    public User getUser(@RequestParam(value = "id") Long Id)
    {
        return userService.getUser(Id);
    }

    // Add User using user object
    @PostMapping("/register")
    public String register(@RequestBody User user)
    {
        return userService.register(user);
    }

    // Update User using user object and user Id from URL
    @PutMapping("/profile")
    public void updateUser(@RequestBody User user, @RequestParam(value = "id") Long Id)
    {
        userService.updateUser(user, Id);
    }

    // Delete User with using user Id from URL
    @DeleteMapping("/profile")
    public void deleteUser(@RequestParam(value = "id") Long Id)
    {
        userService.deleteUser(Id);
    }

}
