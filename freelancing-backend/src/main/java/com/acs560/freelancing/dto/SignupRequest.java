package com.acs560.freelancing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object (DTO) for capturing signup request information.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private String username;
    private String password;
    private String email;
    private String role;  // 'CLIENT' or 'FREELANCER'
    private String name;
}