package com.abel.swagger.repository;

import com.abel.swagger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * User repository.
 *
 * @author Alex Belikov
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);
}
