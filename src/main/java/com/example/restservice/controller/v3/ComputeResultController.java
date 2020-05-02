package com.example.restservice.controller.v3;

import com.example.restservice.controller.dto.VoteResult;
import com.example.restservice.service.ComputeResultService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController("computeResultControllerV3")
@RequestMapping({"/v3/","/oauth2/v3/"})
public class ComputeResultController {

    @Inject
    private ComputeResultService computeResultService;

    @RequestMapping(value = "/computeresult", method = RequestMethod.GET)
    public ResponseEntity<?> computeResult(@RequestParam Long pollId) {
        VoteResult pollVoteResults = computeResultService.getPollVoteResults(pollId);
        return new ResponseEntity<VoteResult>(pollVoteResults, HttpStatus.OK);
    }
}
