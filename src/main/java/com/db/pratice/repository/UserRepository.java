package com.db.pratice.repository;

import com.db.pratice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * This interface extend the JpaRepository.
 * JpaRepository is used to perform operation on the DB.
 */
public interface UserRepository extends JpaRepository<User,Integer> {
   // helping to avoid null pointer exceptions // may or may not contain null-value
   Optional<User> findByUsername(String username); //present in JPA Repository
}
