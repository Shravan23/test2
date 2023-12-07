package com.acs560.freelancing.dto;

import com.acs560.freelancing.model.Bid;
import com.acs560.freelancing.model.Feedback;
import com.acs560.freelancing.model.Profile;
import com.acs560.freelancing.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    User user;
    Profile profile;
    Boolean canEdit;
    List<Bid> bids ;

    List<Feedback> feedbacks;

}
