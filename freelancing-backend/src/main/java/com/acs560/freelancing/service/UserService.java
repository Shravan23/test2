package com.acs560.freelancing.service;

import com.acs560.freelancing.dto.ChangePasswordRequest;
import com.acs560.freelancing.dto.SignupRequest;
import com.acs560.freelancing.exception.EmailAlreadyExistsException;
import com.acs560.freelancing.exception.InvalidPasswordException;
import com.acs560.freelancing.exception.UsernameAlreadyExistsException;
import com.acs560.freelancing.model.User;
import com.acs560.freelancing.repository.UserRepository;
import com.acs560.freelancing.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Handles the user signup process.
     *
     * @param signupRequest The signup request containing user information.
     * @return The created user.
     */
    public User signUp(@Valid SignupRequest signupRequest) {
        if(userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new UsernameAlreadyExistsException("Username is already taken");
        }
        if(userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already registered");
        }
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPasswordHash(passwordEncoder.encode(signupRequest.getPassword()));
        user.setEmail(signupRequest.getEmail());
        user.setRole(User.Role.valueOf(signupRequest.getRole().toUpperCase()));
        user.setCreatedAt(LocalDateTime.now());
        user.setName(signupRequest.getName());

        return userRepository.save(user);
    }

    /**
     * Finds a user by username.
     *
     * @param username The username.
     * @return An Optional containing the found user or empty if not found.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }

    /**
     * Changes the password of a user.
     *
     * @param username The username of the user.
     * @param request The change password request containing old and new passwords.
     */
    public void changePassword(String username, @Valid ChangePasswordRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new InvalidPasswordException("Current password is incorrect");
        }
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    /**
     * Validates a raw password against an encoded password.
     *
     * @param rawPassword The raw password.
     * @param encodedPassword The encoded password.
     * @return true if the passwords match, false otherwise.
     */
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Validates a session based on a JWT token.
     *
     * @param token The JWT token.
     * @return true if the session is valid, false otherwise.
     */
    public boolean validateSession(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    /**
     * Logs out a user based on a JWT token.
     *
     * @param token The JWT token.
     */
    public void logout(String token) {
        jwtTokenProvider.blacklistToken(token);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
