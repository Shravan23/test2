package com.acs560.freelancing.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The User class is an entity model object representing a user in the system.
 * Each user has a unique username and email, and has a role of either CLIENT or FREELANCER.
 */
@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class User {
    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    /**
     * Unique username for the user, used for logging in.
     */
    @Column(unique = true, nullable = false)
    private String username;
    /**
     * Hashed password for the user.
     */
    @Column(nullable = false)
    private String passwordHash;
    /**
     * Role of the user, either CLIENT or FREELANCER.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    /**
     * Unique email address for the user.
     */
    @Column(unique = true, nullable = false)
    private String email;
    /**
     * Date and time when the user was created.
     */

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    /**
     * Date and time when the user was last updated.
     */
    @Column
    private LocalDateTime updatedAt;
    /**
     * Enumeration for the role of a user.
     */
    public enum Role {
        CLIENT, FREELANCER
    }

    private String name;
}
