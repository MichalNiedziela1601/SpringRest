package com.example.restservice.controller;

import com.example.restservice.assembers.PollModelAssember;
import com.example.restservice.controller.exceptions.ResourceNotFoundException;
import com.example.restservice.domain.Poll;
import com.example.restservice.model.PollModel;
import com.example.restservice.repository.PollRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@Api(value = "polls", description = "Poll API")
public class PollController {

    @Inject
    private PollRepository pollRepository;

    @Inject
    private PollModelAssember pollModelAssember;

    @Autowired
    private PagedResourcesAssembler<Poll> pagedResourcesAssembler;

    private Poll verifyPoll(Long pollId) {
        Optional<Poll> p = pollRepository.findById(pollId);
        if(!p.isPresent()) {
            throw new ResourceNotFoundException("Poll with id " + pollId + " not found");
        }
        return p.get();
    }

    @RequestMapping(value = "/polls", method = RequestMethod.GET)
   @ApiOperation(value = "Retrieves all the polls", response=Poll.class, responseContainer = "List")
    public ResponseEntity<PagedModel<PollModel>> getAllPolls(Pageable pageable) {
        Page<Poll> allPolls = pollRepository.findAll(pageable);
        PagedModel<PollModel> collModel = pagedResourcesAssembler.toModel(allPolls, pollModelAssember);
        return new ResponseEntity<>(collModel, HttpStatus.OK);
    }

//    private void updatePollResourceWithLinks(Poll poll, Pageable pageable) {
//        poll.add(linkTo(methodOn(PollController.class).getAllPolls(pageable)).slash(poll.getPollId()).withSelfRel());
//        poll.add(linkTo(methodOn(VoteController.class).getAllVotes(poll.getPollId())).withRel("votes"));
//        poll.add(linkTo(methodOn(ComputeResultController.class).computeResult(poll.getPollId())).withRel("compute-result"));
//    }

    @RequestMapping(value = "/polls", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new Poll", notes="The newly created poll Id will be sent in the location response header"
            ,response = Void.class)
    public ResponseEntity<?> createPoll(@Valid @RequestBody Poll poll) {
        pollRepository.save(poll);
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newPollUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(poll.getPollId()).toUri();
        responseHeaders.setLocation(newPollUri);
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/polls/{pollId}", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves a Poll associated with the Id", response = Poll.class)
    public ResponseEntity<?> getPoll(@PathVariable Long pollId) {
        Poll p = verifyPoll(pollId);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @RequestMapping(value = "/polls/{pollId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePoll(@RequestBody Poll poll, @PathVariable Long pollId) {
        verifyPoll(pollId);
        Poll p = pollRepository.save(poll);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/polls/{pollId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePoll(@PathVariable Long pollId) {
        verifyPoll(pollId);
        pollRepository.deleteById(pollId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
