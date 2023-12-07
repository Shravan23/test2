package com.acs560.freelancing.controller;
import com.acs560.freelancing.dto.*;
import com.acs560.freelancing.model.*;
import com.acs560.freelancing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    UserService userService;

    @Autowired
    BidService bidService;

    @Autowired
    JobService jobService;

    @Autowired
    FeedbackService feedbackService;

    @RequestMapping(value = { "", "/{id}" })
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProfileResponse> viewProfile(@PathVariable("id") Optional<Long> profileIdParam ){

        Long userId = profileIdParam.isPresent() ? profileIdParam.get() : 0L;

        User loggedUser = getUser();

        if(loggedUser == null){
            return ResponseEntity.ok(null);
        }

        User user;
        boolean canEdit = false;

        // If profile ID is not provided in URL, fetch currently logged user
        if(userId < 1) {
            user = loggedUser;
            canEdit = true;
        } else {
            user = userService.findByUserId(userId).get();
            if(userId == loggedUser.getUserId()){
                canEdit = true;
            }
        }

        if(user == null){
            return ResponseEntity.ok(null);
        }


        List<Bid> myBids = null;
        if( canEdit ){
            myBids = bidService.findByUser(user);
        }

        List<Bid> closedBids = bidService.findByClosedAndUser(1, user);

        List<Feedback> myFeedbacks = feedbackService.findByBids(closedBids);

        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.setUser(user);
        profileResponse.setBids(myBids);
        profileResponse.setCanEdit(canEdit);
        profileResponse.setFeedbacks(myFeedbacks);
        profileResponse.setProfile(user.getProfile());
        return ResponseEntity.ok(profileResponse);
    }

    @PostMapping("/save")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> saveProfile(@RequestBody ProfileRequest profileRequest){

        //
        // Get and update current logged user. Don't use params from input to prevent unauthorized editing.
        User me = getUser();

        me.setName(profileRequest.getName());
        me.setEmail(profileRequest.getEmail());

        if (me.getProfile() != null) {
            me.getProfile().setLocation(profileRequest.getProfile().getLocation());
            me.getProfile().setAbout(profileRequest.getProfile().getAbout());
        } else {
            me.setProfile(profileRequest.getProfile());
            me.getProfile().setUser(me);
        }
        me.setUpdatedAt(LocalDateTime.now());

        userService.save(me);

        return ResponseEntity.ok(me);
    }

    @GetMapping("/client/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ClientProfileResponse> viewClientProfile(@PathVariable("id") Long userId) throws Exception{

        User user = userService.findByUserId(userId).get();
        if( user == null ) {
            throw new Exception("User not found");
        }

        List<Job> clientJobs = jobService.findByUser(user);

        List<JobHistory> jobHistory = new ArrayList<>();

        int totalJobs = 0;
        int hiredJobs = jobService.findHiredJobsByClient(user).size();

        for(Job j : clientJobs) {

            totalJobs++;

            JobHistory jh = new JobHistory();
            Feedback feedback = feedbackService.findByJob(j);

            jh.setJob(j);
            jh.setFeedback(feedback);
            jobHistory.add(jh);

        }
        ClientProfileResponse response = new ClientProfileResponse();
        response.setProfile(user.getProfile());
        response.setUser(user);
        response.setTotalJobs(totalJobs);
        response.setHiredJobs(hiredJobs);
        response.setJobHistory(jobHistory);


        return ResponseEntity.ok(response);

    }
    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username).get();
    }

}
