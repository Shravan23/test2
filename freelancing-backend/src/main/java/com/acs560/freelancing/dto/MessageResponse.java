package com.acs560.freelancing.dto;

import com.acs560.freelancing.model.Bid;
import com.acs560.freelancing.model.Job;
import com.acs560.freelancing.model.Message;
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
public class MessageResponse {
    Job job;
    User contractor;
    Bid bid;
    List<Message> messages;
    User user;

}
