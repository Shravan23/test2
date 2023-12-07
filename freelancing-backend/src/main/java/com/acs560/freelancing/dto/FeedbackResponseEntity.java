package com.acs560.freelancing.dto;

import com.acs560.freelancing.model.Bid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FeedbackResponseEntity {
    private Long feedbackId;

    private Bid bid;

    private Integer clientRating;

    private String clientFeedback;

    private Integer userRating;

    private String userFeedback;
}
