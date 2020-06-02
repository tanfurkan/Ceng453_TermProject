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
@RequestMapping("/server_program7/api")
public class UserController {

    private final UserService userService;

    /**
     * This method maps POST request to /login.
     *
     * @param user User information that will be used for login action
     * @return JwtResponse with the current sessions JWTToken
     */
    @PostMapping("/login")
    @ApiOperation(value = "Logins user with given username and password",
            notes = "Provide username and password to login",
            response = ResponseEntity.class)
    public ResponseEntity<?> login(@ApiParam(value = "User information that will be used for login action")
                                   @RequestBody User user) {
        return userService.login(user);
    }

    /**
     * This method maps GET request to /getUserID.
     *
     * @param username Username of the user whose ID is wanted
     * @return The ID of the user with given username
     * @throws Exception if ID does not exist or user is deleted
     */
    @GetMapping("/getUserID")
    @ApiOperation(value = "Gets ID of the user with given username",
            notes = "Provide username to receive ID of that user",
            response = Long.class)
    public Long getUserID(@ApiParam(value = "Username of the user whose ID is wanted")
                          @RequestParam(value = "username") String username) throws Exception {
        return userService.getUserID(username);
    }

    /**
     * This method maps GET request to /users.
     *
     * @return List of all users
     */
    @GetMapping("/users")
    @ApiOperation(value = "Gets all users from database",
            notes = "Needs no parameter",
            response = List.class)
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * This method maps GET request to /profile.
     *
     * @param Id ID of the user that is wanted
     * @return User with the given ID
     * @throws Exception if ID does not exist or user is deleted
     */
    @GetMapping("/profile")
    @ApiOperation(value = "Gets the user from the provided ID",
            notes = "Provide ID to receive the user information",
            response = User.class)
    public User getUser(@ApiParam(value = "ID of the user that is wanted")
                        @RequestParam(value = "id") Long Id) throws Exception {
        return userService.getUser(Id);
    }

    /**
     * This method maps POST request to /profile.
     * It saves new valid User to database.
     * If user is not valid or credentials are bad, returns a message about that error.
     *
     * @param user User information that will be registered
     * @return Response message of the server
     */
    @PostMapping("/register")
    @ApiOperation(value = "Registers the user information given",
            notes = "Provide username and password that will be saved as new member",
            response = ResponseEntity.class)
    public ResponseEntity<?> register(@ApiParam(value = "User information that will be registered")
                                      @RequestBody User user
    ) {
        return userService.register(user);
    }

    /**
     * This method maps PUT request to /profile.
     * It updates users information with given parameters.
     * If user is not valid or credentials are bad, returns a message about that error.
     *
     * @param user User information that will be registered
     * @param Id   ID of the user that will be updated
     * @return Response message of the server as String
     */
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

    /**
     * This method maps DELETE request to /profile.
     * It deletes user with given ID.
     * If user is not valid or credentials are bad, returns a message about that error.
     *
     * @param Id ID of the user that will be deleted
     * @return Response message of the server as String
     */
    // DELETE request to /profile returns String message of the server response. If successful, deletes the user with given ID
    @DeleteMapping("/profile")
    @ApiOperation(value = "Delete the user with given ID",
            notes = "Provide ID of the user that will be deleted",
            response = String.class)
    public String deleteUser(@ApiParam(value = "ID of the user that will be deleted")
                             @RequestParam(value = "id") Long Id) {
        return userService.deleteUser(Id);
    }

}
