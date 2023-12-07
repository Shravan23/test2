package com.acs560.freelancing.controller;

import com.acs560.freelancing.dto.BidRequest;
import com.acs560.freelancing.model.Bid;
import com.acs560.freelancing.model.User;
import com.acs560.freelancing.service.BidService;
import com.acs560.freelancing.service.JobService;
import com.acs560.freelancing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/bids")
public class BidController {

    @Autowired
    private BidService bidService;

    @Autowired
    private JobService jobService;

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ROLE_FREELANCER')")
    public ResponseEntity<Bid> saveBid(@RequestBody BidRequest bidDto) {
        User user = getUser();
        Bid bid = convertToEntity(bidDto, user.getUsername());
        return ResponseEntity.ok(bidService.saveBid(bid));
    }

    @GetMapping("/accept/{bidId}")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<String> acceptBid(@PathVariable Long bidId) throws Exception{
        Bid bid = bidService.getBid(bidId);
        User user = getUser();
        // Additional logic to accept the bid
        if(bid==null || userService.findByUsername(user.getUsername()).get().getUserId() != bid.getJob().getAuthor().getUserId() ) {
            throw  new Exception("No bid or Permission");
        }
        Boolean saved = bidService.acceptBid(bid);
        if(! saved ) {
            throw new Exception("Bid can't be saved!");
        }
        return ResponseEntity.ok("Accepted the Bid: "+bidId);
    }

    @GetMapping("/myContracts")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<List<Bid>> getMyContracts() {
        User user = getUser();
        Set<Bid> contracts = new HashSet<Bid>(bidService.findByUser(user));

        List<Bid> bidsForMyJobs = bidService.findByUserJobs(user);

        contracts.addAll(bidsForMyJobs);

        System.out.println("I've added all the contracts");

        contracts.removeIf(bid -> {
            return bid.getAccepted() == 0;
        });
        System.out.println("I've removed all not accepted Bids");
        List<Bid> response = new ArrayList<>(contracts);
        System.out.println("Final Response");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/close/{bidId}")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<?> closeBid(@PathVariable Long bidId) {
        User user = getUser();

        Bid bid = bidService.getBid(bidId);
        if(bid.getClosed() == 1){
            return ResponseEntity.ok("Bid is already closed: " + bidId);
        }

        if(bid.getJob().getAuthor().getUserId() != user.getUserId()) {
            return ResponseEntity.ok("You can't close this bid, you're not the owner" + bidId);
        }
        bid.setClosed(1);
        return ResponseEntity.ok(bidService.saveBid(bid));
    }

    private Bid convertToEntity(BidRequest bidDto, String userName) {
        Bid bid = new Bid();
        bid.setAmount(bidDto.getAmount());
        bid.setProposedCompletionDate(bidDto.getProposedCompletionDate());
        bid.setCompletedBy(bidDto.getCompletedBy());
        bid.setCreatedAt(LocalDateTime.now());
        bid.setUser(userService.findByUsername(userName).get());
        bid.setJob(jobService.getJob(bidDto.getJobId()));
        return bid;
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username).get();
    }
}
