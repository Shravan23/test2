package com.acs560.freelancing.service;

import com.acs560.freelancing.model.Job;
import com.acs560.freelancing.model.User;
import com.acs560.freelancing.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    @Autowired
    JobRepository jobRepository;

    public Job getJob(Long jobId) {
        return jobRepository.findById(jobId).get();
    }

    public Job addJob(Job job) {
        return jobRepository.save(job);
    }

    public List<Job> listJobs() {
        List<Job> jobs = jobRepository.findAll();
        jobs.sort(
                (j1,j2) -> {
                    return j1.getJobId() > j2.getJobId() ? -1: 0;
                }
        );
        return jobs;
    }

    public List<Job> findByUser(User user) {
        return jobRepository.findByAuthor(user);
    }

    public List<Job> findHiredJobsByClient (User user) {
        return jobRepository.findByAuthorAndHired(user);
    }



}
