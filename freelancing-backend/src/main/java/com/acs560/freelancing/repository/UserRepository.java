package com.acs560.freelancing.repository;

import com.acs560.freelancing.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for managing {@link User} entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by username.
     *
     * @param username the username of the user.
     * @return an Optional of User.
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user exists by username.
     *
     * @param username the username to check.
     * @return true if the user exists, false otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user exists by email.
     *
     * @param email the email to check.
     * @return true if the user exists, false otherwise.
     */
    boolean existsByEmail(String email);

    Optional<User> findByUserId(Long userId);

}
