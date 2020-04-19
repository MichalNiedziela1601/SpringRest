package com.example.restservice.controller;

import com.example.restservice.controller.dto.VoteResult;
import com.example.restservice.service.ComputeResultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
public class ComputeResultController {

    @Inject
    private ComputeResultService computeResultService;

    @RequestMapping(value = "/computeresult", method = RequestMethod.GET)
    public ResponseEntity<?> computeResult(@RequestParam Long pollId) {
        VoteResult pollVoteResults = computeResultService.getPollVoteResults(pollId);
        return new ResponseEntity<VoteResult>(pollVoteResults, HttpStatus.OK);
    }
}
