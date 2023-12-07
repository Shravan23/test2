package com.acs560.freelancing.service;

import com.acs560.freelancing.model.User;
import com.acs560.freelancing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Service class for loading user-specific data.
 * Implements the UserDetailsService interface from Spring Security to provide core user information.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    /**
     * Loads the user data by the given username.
     * @param username the username identifying the user
     * @return a fully populated user record (never null)
     * @throws UsernameNotFoundException if the user could not be found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        System.out.println("Hello Returns - " + authorities);
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPasswordHash(), authorities);

    }
}
