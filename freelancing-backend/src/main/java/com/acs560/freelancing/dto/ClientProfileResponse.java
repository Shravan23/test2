package com.acs560.freelancing.dto;

import com.acs560.freelancing.model.Job;
import com.acs560.freelancing.model.JobHistory;
import com.acs560.freelancing.model.Profile;
import com.acs560.freelancing.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientProfileResponse {
    User user;
    Profile profile;
    Integer totalJobs;
    Integer hiredJobs;
    List<JobHistory> jobHistory;
}
