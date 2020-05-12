package com.example.restservice.assembers;

import com.example.restservice.controller.PollController;
import com.example.restservice.domain.Poll;
import com.example.restservice.model.PollModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PollModelAssember extends RepresentationModelAssemblerSupport<Poll, PollModel> {

    public PollModelAssember() {
        super(PollController.class, PollModel.class);
    }


    @Override
    public PollModel toModel(Poll entity) {
        PollModel pollModel = new PollModel();
        pollModel.add(linkTo(methodOn(PollController.class).getPoll(entity.getPollId())).withSelfRel());
        pollModel.setId(entity.getPollId());
        pollModel.setQuestion(entity.getQuestion());
        pollModel.setOptions(entity.getOptions());
        return pollModel;
    }


}
