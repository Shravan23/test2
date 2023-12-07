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
public class ClientProfileRequest {
    private String phoneNumber;
    private String location;
    private String profileDescription;
    private List<Long> postedJobs; // List of Job IDs posted by the client
    private Double rating; // Average rating of the client

    // Getters and Setters
    // ...
}