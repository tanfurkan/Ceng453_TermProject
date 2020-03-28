package com.ceng453.gameServer.services;

import com.ceng453.gameServer.model.JwtResponse;
import com.ceng453.gameServer.model.User;
import com.ceng453.gameServer.repository.UserRepository;
import com.ceng453.gameServer.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final AuthenticationManager authenticationManager;

    private final UsersDetailsService usersDetailsService;

    private final JwtUtil jwtTokenUtil;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public ResponseEntity<?> login(User requestUser) throws Exception {
        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestUser.getUsername(), requestUser.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = usersDetailsService
                .loadUserByUsername(requestUser.getUsername());

        //Creating authentication jwt token
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(jwt));

    }

    public Long getUserID(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return (long) -1;
        }
        return user.get().getId();
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userRepository.findAll());
    }

    public User getUser(Long Id) {
        if (userRepository.findById(Id).isPresent())
            return userRepository.findById(Id).get();
        else
            return null;
    }

    public String register(User user) {

        if (user.getUsername().isEmpty())
            return "Username cannot be empty.";
        if (user.getPassword().isEmpty())
            return "Password cannot be empty.";
        if (userRepository.findByUsername(user.getUsername()).isPresent())
            return "Username is already taken.";
        else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return "User created successfully. Please log in";
        }
        //TODO add more secure password checking system
    }

    public String updateUser(User requestUser, Long Id) {
        if (userRepository.findById(Id).isPresent()) {
            User user = userRepository.findById(Id).get();
            user.setPassword(requestUser.getPassword());
            userRepository.save(user);
            return "User is updated successfully.";
            //TODO maybe changing name will be added.
        }
        return "User is not updated, please check the input fields and id.";
    }

    public String deleteUser(Long Id) {
        if (userRepository.findById(Id).isPresent()) {
            User user = userRepository.findById(Id).get();
            user.setDeleted(true);
            userRepository.save(user);
            return "User is deleted successfully.";
        }
        return "User is not deleted, please check the id.";
    }

}
