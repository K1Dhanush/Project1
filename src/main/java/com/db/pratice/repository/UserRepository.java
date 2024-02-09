package com.db.pratice.repository;

import com.db.pratice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * This interface extend the JpaRepository.
 * JpaRepository is used to perform operation on the DB.
 */
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUserName(String username);
}
