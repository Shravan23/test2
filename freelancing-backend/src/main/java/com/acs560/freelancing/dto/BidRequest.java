package com.acs560.freelancing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for capturing bid information.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BidRequest {

    private BigDecimal amount;
    private LocalDateTime proposedCompletionDate;
    private Long jobId;
    private LocalDateTime completedBy;
}
