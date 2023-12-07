package com.acs560.freelancing.repository;

import com.acs560.freelancing.model.Job;
import com.acs560.freelancing.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByAuthor(User author);

    @Query("SELECT j"
            + " FROM Bid b "
            + " JOIN b.job j "
            + " WHERE j.author = :user AND b.accepted = 1")
    List<Job> findByAuthorAndHired(@Param("user") User user);


}

