package com.acs560.freelancing.controller;

//import com.acs560.freelancing.configuration.SecurityService;
import com.acs560.freelancing.dto.JobRequest;
import com.acs560.freelancing.dto.JobResponse;
import com.acs560.freelancing.model.*;
import com.acs560.freelancing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    JobService jobService;

    @Autowired
    BidService bidService;

    @Autowired
    FeedbackService feedbackService;

    @Autowired
    UserService userService;

    @GetMapping("/all")
    public ResponseEntity<?> listJobs() {

        List<Job> jobs = jobService.listJobs();

        return ResponseEntity.ok(jobs);
    }

    @GetMapping({"/view/{id}", "/{id}" })
    public ResponseEntity<JobResponse> viewJob(@PathVariable("id") long id){

        Job job = jobService.getJob(id);

        // Get my bid for the job and assign to the view
//        Bid myBid = null;
//
//        // Check if logged in:
//        User user = getUser();
//        if( user != null){
//            myBid = bidService.getUsersBidByJob(user, job);
//            if(myBid != null) {
//                myBid.setProposal(myBid.getProposal());
//            }
//        }

        // Calculate client rate:
        long avgClientFeedback = 0;
        int totalFeedbackNo = 0;
        List<Feedback> feedbacks = feedbackService.findByClient(job.getAuthor());
        if(feedbacks.size() > 0) {
            int sum = 0;
            int no = 0;
            for (Feedback f : feedbacks) {
                sum += f.getClientRating();
                no++;
            }
            avgClientFeedback = sum / no;
            totalFeedbackNo = feedbacks.size();
        }

        List<Job> jobs = jobService.findByUser(job.getAuthor());
        List<Job> hiredJobs = jobService.findHiredJobsByClient(job.getAuthor());
        double totalJobsNo = jobs.size();
        double hiredJobsNo = hiredJobs.size();
        double hireRate = (hiredJobsNo / totalJobsNo) * 100;

        JobResponse jobResponse = new JobResponse();
        jobResponse.setTotalFeedbackNo(totalFeedbackNo);
        jobResponse.setHiredJobsNo((int)hiredJobsNo);
        jobResponse.setTotalJobsNo((int) totalJobsNo);
        jobResponse.setAvgClientFeedback(avgClientFeedback);
        jobResponse.setHireRate((int) hireRate);
        jobResponse.setBidsNumber(bidService.findByJob(job).size());
        jobResponse.setJob(job);


        return ResponseEntity.ok(jobResponse);
    }


    //Update the endpoint so it'll accept the id too
    //Also don't just pass the Job, create a dto for the same.
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<Job> saveJob(
            @RequestParam(name = "id", required = false) Long id,
            @RequestBody JobRequest jobRequest ){

        // Set current loged user ID as author
        User user = getUser();
        if(user == null){
            System.out.println("Please login to add a job!");
            return null;
        }
        Job job = new Job();
        job.setAuthor(user);
        Job savedJob = null;

        if( id != null && id > 0){
            job = jobService.getJob(id);
            if(job.getAuthor().getUserId() == user.getUserId()) {
                job.setBudget(jobRequest.getBudget());
                job.setTitle(jobRequest.getTitle());
                job.setDescription(jobRequest.getDescription());
                job.setUpdatedAt(LocalDateTime.now());
            }
        } else {
            job.setDescription(jobRequest.getDescription());
            job.setTitle(jobRequest.getTitle());
            job.setBudget(jobRequest.getBudget());
            job.setCreatedAt(LocalDateTime.now());
        }
        return ResponseEntity.ok(jobService.addJob(job));
    }

    @GetMapping("/bids/{jobId}")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<?> viewBids( @PathVariable("jobId") long jobId) {

        Job job = jobService.getJob(jobId);

        User user = getUser();

        if( job == null || job.getAuthor().getUserId() != user.getUserId() ) {
            System.out.println("Job not found or you don't have privileges");
            return ResponseEntity.ok("Bad Request, No jobs found");
        }

        List<Bid> bids = bidService.findByJob(job);

        return ResponseEntity.ok(bids);
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username).get();
    }

}
