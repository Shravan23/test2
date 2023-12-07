package com.acs560.freelancing.service;

import com.acs560.freelancing.model.Bid;
import com.acs560.freelancing.model.Feedback;
import com.acs560.freelancing.model.Job;
import com.acs560.freelancing.model.User;
import com.acs560.freelancing.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    FeedbackRepository feedbackRepository;

    public Feedback saveFeedback(Feedback feedback) {
        return  feedbackRepository.save(feedback);
    }

    public Feedback getFeedback(Long id) {
        return feedbackRepository.findById(id).get();
    }

    public Feedback findByBid(Bid bid) {
        return feedbackRepository.findByBid(bid);
    }

    public List<Feedback> findByClient(User user) {
        return feedbackRepository.findByClient(user);
    }

    public Feedback findByJob(Job job) {
        return feedbackRepository.findByJob(job);
    }

    public List<Feedback> findByBids(List<Bid> bids) {
        return feedbackRepository.findByBid(bids);
    }

}
