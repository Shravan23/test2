package com.acs560.freelancing.controller;

import com.acs560.freelancing.dto.FeedbackRequest;
import com.acs560.freelancing.dto.FeedbackResponse;
import com.acs560.freelancing.dto.FeedbackResponseEntity;
import com.acs560.freelancing.model.Bid;
import com.acs560.freelancing.model.Feedback;
import com.acs560.freelancing.model.User;
import com.acs560.freelancing.service.BidService;
import com.acs560.freelancing.service.FeedbackService;
import com.acs560.freelancing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    BidService bidService;

    @Autowired
    FeedbackService feedbackService;

    @Autowired
    UserService userService;
    @GetMapping("/{bidId}")
    public ResponseEntity<String> send(@PathVariable("bidId") long bidId) throws Exception {

        Bid bid = bidService.getBid(bidId);

        if (bid == null ) {
            throw new Exception("Bid does not exists");
        }

        // If user is NOT owner or contractor, he can't post:
        if( !canPost(bid)) {
            throw new Exception("Not owner of the job nor contractor!");
        }

        Feedback feedback = feedbackService.findByBid(bid);

        if( alreadySentFeedback(feedback)) {
            return ResponseEntity.ok("You've already submitted the Feedback");
        }

        String myRoleForBid = getUser().getRole().toString();
        String cooperatorName = myRoleForBid.equals("CLIENT") ? bid.getJob().getAuthor().getUsername() : bid.getUser().getUsername();

        return ResponseEntity.ok("Successfully Posted the Review for " + cooperatorName);
    }

    @PostMapping("/save")
    public ResponseEntity<FeedbackResponse> save(@RequestBody FeedbackRequest feedbackRequest) throws Exception{

        if(feedbackRequest.getRating() < 1 || feedbackRequest.getRating() > 5) {
            throw new Exception("Rate must be between 1 and 5");
        }
        if(feedbackRequest.getFeedbackText().length() < 5) {
            throw new Exception("Please enter feedback text");
        }

        Bid bid = bidService.getBid(feedbackRequest.getBidId());

        if( !canPost(bid)) {
            throw new Exception("Not owner of the job nor contractor!");
        }
        User user = getUser();

        Feedback feedback = new Feedback();
        feedback.setBid(bid);

        String myRoleForBid = getUser().getRole().toString();
        FeedbackResponse feedbackResponse = new FeedbackResponse();
        feedbackResponse.setFeedbackGivenById(user.getUserId());
        feedbackResponse.setFeedbackGivenBy(user.getUsername());

        User givenTo = user.getRole().toString().equals("FREELANCER") ? bid.getJob().getAuthor() : bid.getUser();
        feedbackResponse.setFeedbackGivenToId(givenTo.getUserId());
        feedbackResponse.setFeedbackGivenTo(givenTo.getUsername());
        feedbackResponse.setRating(feedbackRequest.getRating());
        feedbackResponse.setFeedbackText(feedbackRequest.getFeedbackText());
        feedbackResponse.setBidId(feedbackRequest.getBidId());


        // If I am contractor (owner of the bid), set client rate&feedback.
        // Otherwise, I am job owner and I set contractor rate&feedback.
        if(myRoleForBid.equals("FREELANCER")) {
            feedback.setClientFeedback(feedbackRequest.getFeedbackText());
            feedback.setClientRating(feedbackRequest.getRating());
        } else {
            feedback.setUserFeedback(feedbackRequest.getFeedbackText());
            feedback.setUserRating(feedbackRequest.getRating());
        }

        // If feedback for the bid exists, update it. Otherwise, insert new.
        Feedback dbFeedback = feedbackService.findByBid(bid);
        if(dbFeedback == null) {
            dbFeedback = feedbackService.saveFeedback(feedback); // Insert
        } else {

            if(myRoleForBid.equals("FREELANCER")) {
                dbFeedback.setClientFeedback( feedback.getClientFeedback() );
                dbFeedback.setClientRating(feedback.getClientRating() );
            } else {
                dbFeedback.setUserFeedback(feedback.getUserFeedback());
                dbFeedback.setUserRating(feedback.getUserRating());
            }

            dbFeedback = feedbackService.saveFeedback(dbFeedback); // Update

        }


        return ResponseEntity.ok(feedbackResponse);
    }

    @GetMapping("view/{bidId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FeedbackResponseEntity> viewBidFeedbacks(@PathVariable("bidId") long bidId) throws Exception {

        Bid bid = bidService.getBid(bidId);

        if(!canPost(bid)) {
            throw new Exception("Not owner of the job nor contractor!");
        }

        Feedback feedback = feedbackService.findByBid(bid);
        FeedbackResponseEntity feedbackResponse = new FeedbackResponseEntity();
        feedbackResponse.setFeedbackId(feedback.getFeedbackId());
        feedbackResponse.setClientFeedback(feedback.getClientFeedback());
        feedbackResponse.setUserFeedback(feedback.getUserFeedback());
        feedbackResponse.setBid(feedback.getBid());
        feedbackResponse.setUserRating(feedback.getUserRating());
        feedbackResponse.setClientRating(feedback.getClientRating());

        return ResponseEntity.ok(feedbackResponse);

    }
    private boolean canPost(Bid bid) {
        User user = getUser();
        return user.getUserId() != bid.getUser().getUserId() || user.getUserId() != bid.getJob().getAuthor().getUserId();
    }

    private boolean alreadySentFeedback(Feedback feedback) {
        if(feedback == null) {
            return false;
        }
        User user = getUser();
        String role = user.getRole().toString();

        return ( role.equals("CLIENT") && feedback.getClientRating() > 0 )
                || ( role.equals("FREELANCER") && feedback.getUserRating() > 0 );
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username).get();
    }
}
