package com.ceng453.gameServer.repository;

import com.ceng453.gameServer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
