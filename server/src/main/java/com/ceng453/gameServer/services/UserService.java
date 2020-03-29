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
            Optional<User> optUser = userRepository.findByUsername(requestUser.getUsername());
            if (optUser.isEmpty()) throw new BadCredentialsException("Incorrect username or password");
            User user = optUser.get();
            if (!user.isDeleted()){
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(requestUser.getUsername(), requestUser.getPassword())
                );
            }
            else throw new BadCredentialsException("Incorrect username or password");
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = usersDetailsService
                .loadUserByUsername(requestUser.getUsername());

        //Creating authentication jwt token
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(jwt));

    }

    public Long getUserID(String username) throws Exception{
        Optional<User> optUser = userRepository.findByUsername(username);
        if (optUser.isEmpty()) throw new Exception("User is not found.");

        User user = optUser.get();

        if(user.isDeleted()) throw new Exception("User is not found.");

        return user.getId();
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userRepository.findAll());
    }

    public User getUser(Long Id) throws Exception{
        Optional<User> optUser = userRepository.findById(Id);
        if (optUser.isEmpty()) throw new Exception("User is not found");

        User user = optUser.get();
        if(user.isDeleted()) throw new Exception("User is not found");

        return user;

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
    }

    public String updateUser(User requestUser, Long Id) {
        if (userRepository.findById(Id).isPresent()) {
            User user = userRepository.findById(Id).get();
            if(user.isDeleted()) return "User is not found, please check the id.";

            user.setPassword(requestUser.getPassword());
            userRepository.save(user);
            return "User is updated successfully.";
            //TODO maybe changing name will be added.
        }
        return "User is not found, please check the input fields and id.";
    }

    public String deleteUser(Long Id) {
        if (userRepository.findById(Id).isPresent()) {
            User user = userRepository.findById(Id).get();

            if(user.isDeleted()) return "User is not found, please check the id.";

            user.setDeleted(true);
            userRepository.save(user);
            return "User is deleted successfully.";
        }
        return "User is not found, please check the id.";
    }

}
