package com.acs560.freelancing.service;

import com.acs560.freelancing.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * UserPrincipal class implements UserDetails interface from Spring Security.
 * It provides a core user information for the security services.
 */
public class UserPrincipal implements UserDetails {

    private User user;
    /**
     * Constructor to initialize UserPrincipal with a user.
     * @param user the user object
     */
    public UserPrincipal(User user) {
        this.user = user;
    }
    /**
     * Creates a UserPrincipal object from a user object.
     * @param user the user object
     * @return a UserPrincipal object
     */
    public static UserPrincipal create(User user) {
        return new UserPrincipal(user);
    }
    /**
     * Gets the authorities granted to the user.
     *
     * @return The authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()));
    }

    /**
     * Gets the password of the user.
     *
     * @return The password.
     */
    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }
    /**
     * Gets the username of the user.
     *
     * @return The username.
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }
    /**
     * Indicates whether the user's account has expired.
     *
     * @return true if the user's account is valid (ie non-expired),
     * false if no longer valid (ie expired).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    /**
     * Indicates whether the user is locked or unlocked.
     *
     * @return true if the user is not locked, false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    /**
     * Indicates whether the user's credentials (password) has expired.
     *
     * @return true if the user's credentials are valid (ie non-expired),
     * false if no longer valid (ie expired).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    /**
     * Indicates whether the user is enabled or disabled.
     *
     * @return true if the user is enabled, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
