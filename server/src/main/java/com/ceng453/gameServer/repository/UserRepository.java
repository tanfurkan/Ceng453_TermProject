package com.ceng453.gameServer.repository;

import com.ceng453.gameServer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * This method overloads findByUsername function to return Optional<User>
     * It is added to return a non null valid object as checker for database answer.
     *
     * @param username Name of the user which is wanted.
     * @return an Optional object that may or not hold User which is checkable
     */
    Optional<User> findByUsername(String username);
}
