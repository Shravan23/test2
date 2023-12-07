package com.acs560.freelancing.repository;

import com.acs560.freelancing.model.Bid;
import com.acs560.freelancing.model.Job;
import com.acs560.freelancing.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByJob(Job job);
    List<Bid> findByUser(User user);

    @Query("SELECT b"
            + " FROM Bid b"
            + " JOIN b.job j"
            + " WHERE j.author = :me ")
    List<Bid> findByUserJobs(@Param("me") User me);

    List<Bid> findByClosedAndUser(int closed, User user);

    List<Bid> findByUserAndJob(User user, Job job);

}
