package com.acs560.freelancing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FreelancerProfileRequest {
    private String phoneNumber;
    private String professionalTitle;
    private List<String> skills; // Skills could be a list of strings
    private List<String> otherLinks; // Links to portfolios or professional profiles
    private String experienceSummary;
    private Double hourlyRate;
    private Double ratings; // Average rating of the freelancer

}