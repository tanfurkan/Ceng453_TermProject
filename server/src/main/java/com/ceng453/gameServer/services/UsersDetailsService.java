package com.ceng453.gameServer.services;

import com.ceng453.gameServer.model.User;
import com.ceng453.gameServer.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsersDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * This method overrides the loadByUsername function to connect project's database to Spring Security user database.
     * It loads UserDetails in Spring Security User Database if user is registered in project's database.
     * @param username Name of the user
     * @throws UsernameNotFoundException if username is not found on database.
     * @return UserDetails of the user with given username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> optUser = userRepository.findByUsername(username);
        if (optUser.isPresent()) {
            User user = optUser.get();
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("Username or password is incorrect");
        }
    }
}
