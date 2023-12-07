package com.acs560.freelancing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "message")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User receiver;

    @ManyToOne
    @JoinColumn(name="job_id", nullable = true)
    private Job job;

    @Column(length = 64000)
    @Size(min = 2)
    private String text;

    private Date createdAt;
}
