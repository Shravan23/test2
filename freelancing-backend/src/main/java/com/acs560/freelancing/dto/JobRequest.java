package com.acs560.freelancing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for capturing job request information.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobRequest {
    private String title;
    private String description;
    private Long budget;

}