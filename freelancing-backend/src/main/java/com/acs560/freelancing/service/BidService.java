package com.acs560.freelancing.service;


import com.acs560.freelancing.model.Bid;
import com.acs560.freelancing.model.Job;
import com.acs560.freelancing.model.User;
import com.acs560.freelancing.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidService {
    @Autowired
    BidRepository bidRepository;

    public Bid saveBid(Bid bid) {
        return bidRepository.save(bid);
    }

    public Bid getBid(Long id) {
        return bidRepository.findById(id).get();
    }

    public Bid getUsersBidByJob(User user, Job job) {

        List<Bid> bids = bidRepository.findByUserAndJob(user, job);

        if(bids.isEmpty()){
            return null;
        }

        if(bids.size() > 1){
            // ToDo: Change the print Statement to throwing Error.
            System.out.println("ERROR: found more more than 1 user's bids for a job.");
        }
        try {
            return bids.get(0);
        } catch (IndexOutOfBoundsException e) {
            // ToDo: Change the print Statement to throwing Error.
          System.out.println("No bids found for this user");
        }
        return null;
    }

    public List<Bid> findByUser(User user){
        System.out.println("Im in findByUser of BidService - saying hello");
        return bidRepository.findByUser(user);
    }

    public List<Bid> findByJob(Job job) {
        return bidRepository.findByJob(job);
    }

    public boolean acceptBid(Bid bid) {
        bid.setAccepted(1);
        saveBid(bid);
        return true;
    }

    public List<Bid> findByUserJobs(User user) {
        System.out.println("Im in findByUserJobs of BidService - saying hello again");
        return bidRepository.findByUserJobs(user);
    }

    public List<Bid> findByClosedAndUser(Integer closed, User user) {
        return bidRepository.findByClosedAndUser(closed, user);
    }

}
