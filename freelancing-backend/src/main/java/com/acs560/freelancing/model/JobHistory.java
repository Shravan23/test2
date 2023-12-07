package com.acs560.freelancing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class JobHistory {
    private Bid bid;
    private Feedback feedback;
    private Job job;


}
