package com.auth.service.repository;

import com.auth.service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByLogin(String login);
}
