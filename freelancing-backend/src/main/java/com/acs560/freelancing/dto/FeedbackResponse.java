package com.acs560.freelancing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FeedbackResponse {
    Long feedbackGivenById;
    Long feedbackGivenToId;
    String feedbackGivenBy;
    String feedbackGivenTo;
    Long bidId;
    String feedbackText;

    Integer rating;
}
