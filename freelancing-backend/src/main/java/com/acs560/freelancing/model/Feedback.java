package com.acs560.freelancing.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    @ManyToOne
    @JoinColumn(name="bid_id", nullable = true)
    private Bid bid;

    private Integer clientRating;

    private String clientFeedback;

    private Integer userRating;

    private String userFeedback;
}
