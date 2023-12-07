package com.acs560.freelancing.controller;

import com.acs560.freelancing.dto.AuthenticationResponse;
import com.acs560.freelancing.dto.ChangePasswordRequest;
import com.acs560.freelancing.dto.LoginRequest;
import com.acs560.freelancing.dto.SignupRequest;
import com.acs560.freelancing.exception.BadCredentialsException;
import com.acs560.freelancing.model.User;
import com.acs560.freelancing.security.JwtTokenProvider;
import com.acs560.freelancing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Handles user registration.
     *
     * @param signupRequest The sign-up request containing user information.
     * @return A ResponseEntity containing the created user.
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignupRequest signupRequest) {
        User user = userService.signUp(signupRequest);
        return ResponseEntity.ok(user);
    }

    /**
     * Handles user login.
     *
     * @param loginRequest The login request containing username and password.
     * @return A ResponseEntity containing an AuthenticationResponse with a JWT token.
     * @throws BadCredentialsException if the credentials are invalid.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) throws BadCredentialsException {
        User user = userService.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!userService.validatePassword(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid credentials");
        }
        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole());
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    /**
     * Handles password change.
     *
     * @param request The change password request containing old and new passwords.
     * @return A ResponseEntity indicating the result of the operation.
     */
    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userService.changePassword(auth.getName(), request);
        return ResponseEntity.ok().build();
    }

    /**
     * Handles user logout.
     *
     * @param request The HTTP request.
     * @return A ResponseEntity indicating the result of the operation.
     */
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        userService.logout(token);
        return ResponseEntity.ok().build();
    }
}
