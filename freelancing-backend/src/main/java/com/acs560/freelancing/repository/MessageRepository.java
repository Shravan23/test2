package com.acs560.freelancing.repository;

import com.acs560.freelancing.model.Job;
import com.acs560.freelancing.model.Message;
import com.acs560.freelancing.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByJob(Job job);

    @Query(  "SELECT m"
            + " FROM Message m"
            + " WHERE m.job = :job AND ( m.sender = :user OR m.receiver = :user )"
            + " ORDER BY m.id DESC")
    Page<Message> findByJobAndSenderOrReceiver(
            @Param("job") Job job,
            @Param("user") User user,
            Pageable request
    );

    @Query("SELECT m"
            + " FROM Message m"
            + " WHERE m.sender = :user OR m.receiver = :user "
            + " ORDER BY m.id DESC")
    List<Message> findBySenderOrReceiver(@Param("user") User me);

    @Query("SELECT m"
            + " FROM Message m"
            + " WHERE "
            + "( ( m.sender = :user1 AND m.receiver = :user2 ) "
            + " OR "
            + " ( m.sender = :user2 AND m.receiver = :user1 ) ) "
            + " AND m.job is null "
            + " ORDER BY m.id DESC")
    List<Message> findByMyUsers(@Param("user1") User user1, @Param("user2") User user2);
}
