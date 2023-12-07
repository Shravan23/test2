package com.acs560.freelancing.dto;

import com.acs560.freelancing.model.Job;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobResponse {
    long avgClientFeedback;
    int totalFeedbackNo;
    int bidsNumber;
    int hireRate;
    int totalJobsNo;
    int hiredJobsNo;

    Job job;


}
