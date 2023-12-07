package com.acs560.freelancing.controller;
import com.acs560.freelancing.dto.MessageResponse;
import com.acs560.freelancing.model.*;
import com.acs560.freelancing.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    MessageService messageService;

    @Autowired
    JobService jobService;

    @Autowired
    UserService userService;

    @Autowired
    BidService bidService;

    @GetMapping("/job_room/{jobId}/{contractor}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponse> jobRoom(
                          @PathVariable("jobId") long jobId,
                          @PathVariable("contractor") long contractorId) {

        User user = getUser();

        Job job = jobService.getJob(jobId);

        User contractor = userService.findByUserId(contractorId).get();


        Bid bid = null;
        List<Message> messages = null;
        if( job != null){
            bid = job.getAuthor().getUserId() == user.getUserId()
                    ? bidService.getUsersBidByJob(contractor, job)
                    : bidService.getUsersBidByJob(user, job);
        } else {
            messages = messageService.findByMyConversers(user, contractor);
        }
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessages(messages);
        messageResponse.setBid(bid);
        messageResponse.setJob(job);
        messageResponse.setUser(user);
        messageResponse.setContractor(contractor);

        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping("/job_room/{jobId}/{contractor}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Message> sendMessageToJobRoom(
            @RequestBody String messageText,
            @PathVariable("jobId") long jobId,
            @PathVariable("contractor") long contractorId) throws Exception {

        Job job = jobService.getJob(jobId);
        User contractor = userService.findByUserId(contractorId).get();
        User user = getUser();

        // Check if I have rights to add message:
        if( job != null) {
            if(job.getAuthor().getUserId() != user.getUserId() && job.getAuthor().getUserId() != contractor.getUserId() ){
                throw new Exception("Current user can not write message to this job");
            }
        }
        Message message = new Message();
        message.setText(messageText);

        if( job != null ) {
            message.setJob(job);
        }
        message.setReceiver(contractor);
        message.setSender(getUser());
        message.setText(messageText);
        message.setCreatedAt( new Date() );

        Message result = messageService.save(message);
        if(result == null) {
            throw new Exception("Can't save new message");
        }

        return ResponseEntity.ok(message);

    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username).get();
    }
}
