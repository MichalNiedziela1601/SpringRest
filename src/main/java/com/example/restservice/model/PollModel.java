package com.example.restservice.model;

import com.example.restservice.domain.Option;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Set;

@Relation(collectionRelation = "polls", itemRelation = "poll")
public class PollModel extends RepresentationModel<PollModel> {

    private Long id;
    private String question;
    private Set<Option> options;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Set<Option> getOptions() {
        return options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

}
