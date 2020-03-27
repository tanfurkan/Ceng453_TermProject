package com.ceng453.gameServer.services;

import com.ceng453.gameServer.model.User;
import com.ceng453.gameServer.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean login(User requestUser)
    {
        Optional<User> user = userRepository.findByUsername(requestUser.getUsername());
        if (user.isEmpty()) {
            return false;
        }
        return user.get().getPassword().equals(requestUser.getPassword());
    }

    public Long getUserID(String username)
    {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return (long) -1;
        }
        return user.get().getId();
    }

    public List<User> getAllUsers()
    {
        return new ArrayList<>(userRepository.findAll());
    }

    public User getUser(Long Id)
    {
        if(userRepository.findById(Id).isPresent())
            return userRepository.findById(Id).get();
        else
            return null;
    }

    public String register(User user)
    {
        if(user.getUsername().isEmpty())
            return "Username cannot be empty.";
        if(user.getPassword().isEmpty())
            return "Password cannot be empty.";
        if(userRepository.findByUsername(user.getUsername()).isPresent())
            return "Username is already taken.";
        else {
            userRepository.save(user);
            return "User created successfully";
        }
        //TODO add more secure password checking system
    }

    public void updateUser(User requestUser, Long Id)
    {
        if(userRepository.findById(Id).isPresent()) {
            User user = userRepository.findById(Id).get();
            user.setPassword(requestUser.getPassword());
            userRepository.save(user);
            //TODO maybe changing name will be added.
        }
    }

    public void deleteUser(Long Id)
    {
        if(userRepository.findById(Id).isPresent()) {
            User user = userRepository.findById(Id).get();
            user.setDeleted(true);
            userRepository.save(user);
        }
    }
}
