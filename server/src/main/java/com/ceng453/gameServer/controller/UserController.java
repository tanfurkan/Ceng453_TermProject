package com.ceng453.gameServer.controller;

import com.ceng453.gameServer.model.User;
import com.ceng453.gameServer.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @ApiOperation(value = "Logins user with given username and password",
            notes = "Provide username and password to login",
            response = ResponseEntity.class)
    public ResponseEntity<?> login(@ApiParam(value = "User information that will be used for login action")
                                   @RequestBody User user) throws Exception {
        return userService.login(user);
    }

    @GetMapping("/getUserID")
    @ApiOperation(value = "Gets ID of the user with given username",
            notes = "Provide username to receive ID of that user",
            response = Long.class)
    public Long getUserID(@ApiParam(value = "Username of the user whose ID is wanted")
                          @RequestParam(value = "username") String username) throws Exception{
        return userService.getUserID(username);
    }

    // Get all Users
    @GetMapping("/users")
    @ApiOperation(value = "Gets all users from database",
            notes = "Needs no parameter",
            response = List.class)
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get User using Id
    @GetMapping("/profile")
    @ApiOperation(value = "Gets the user from the provided ID",
            notes = "Provide ID to receive the user information",
            response = User.class)
    public User getUser(@ApiParam(value = "ID of the user that is wanted")
                        @RequestParam(value = "id") Long Id) throws Exception{
        return userService.getUser(Id);
    }

    // Add User using user object
    @PostMapping("/register")
    @ApiOperation(value = "Registers the user information given",
            notes = "Provide username and password that will be saved as new member",
            response = String.class)
    public String register(@ApiParam(value = "User information that will be registered")
                           @RequestBody User user
    ) {
        return userService.register(user);
    }

    // Update User using user object and user Id from URL
    @PutMapping("/profile")
    @ApiOperation(value = "Updates the user that is from given ID with new user information",
            notes = "Provide new password that will be updated",
            response = String.class)
    public String updateUser(@ApiParam(value = "User information that will be used to update user")
                             @RequestBody User user,
                             @ApiParam(value = "ID of the user that will be updated")
                             @RequestParam(value = "id") Long Id) {
        return userService.updateUser(user, Id);
    }

    // Delete User with using user Id from URL
    @DeleteMapping("/profile")
    @ApiOperation(value = "Delete the user with given ID",
            notes = "Provide ID of the user that will be deleted",
            response = String.class)
    public String deleteUser(@ApiParam(value = "ID of the user that will be deleted")
                             @RequestParam(value = "id") Long Id) {
        return userService.deleteUser(Id);
    }

}
