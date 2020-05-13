package com.example.restservice.service;

import com.example.restservice.Util;
import com.example.restservice.controller.dto.VoteResult;
import com.example.restservice.domain.Option;
import com.example.restservice.domain.Vote;
import com.example.restservice.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ComputeResultServiceTest {

    @Mock
    VoteRepository voteRepositoryMock;

    @InjectMocks
    ComputeResultService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testComputeResult() {
        Option madryt = Util.createOption(1L, "Madryt");
        Vote vote1 = Util.createVote(1L, madryt);
        Vote vote2 = Util.createVote(2L, Util.createOption(2L, "Barcelona"));
        Vote vote3 = Util.createVote(3L, madryt);
        List<Vote> votes = new ArrayList<>();
        votes.add(vote1);
        votes.add(vote2);
        votes.add(vote3);
        when(voteRepositoryMock.findByPoll(anyLong())).thenReturn(votes);

        VoteResult pollVoteResults = service.getPollVoteResults(1L);
        assertEquals(3,pollVoteResults.getTotalVotes());
        assertEquals("[OptionCount{optionId=1, count=2}, OptionCount{optionId=2, count=1}]",pollVoteResults.getResults().toString());
    }


}