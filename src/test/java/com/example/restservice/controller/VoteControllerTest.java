package com.example.restservice.controller;

import com.example.restservice.Util;
import com.example.restservice.domain.Option;
import com.example.restservice.domain.Vote;
import com.example.restservice.repository.VoteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest {

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    VoteController voteController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(voteController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers(new ViewResolver() {
                    @Override
                    public View resolveViewName(String s, Locale locale) throws Exception {
                        return new MappingJackson2JsonView();
                    }
                })
                .build();
    }

    @Test
    void testCreateVote() throws Exception {
        Vote vote = new Vote();
        vote.setId(1L);
        Option option1 = new Option();
        option1.setValue("Red");
        option1.setId(1L);
        vote.setOption(option1);

        ObjectMapper ob = new ObjectMapper();
        String body = ob.writeValueAsString(vote);

        when(voteRepository.save(any(Vote.class))).thenReturn(vote);

        mockMvc.perform((MockMvcRequestBuilders.post("/polls/1/votes")
                .content(body))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetAllVotes() throws Exception {
        Option red = Util.createOption(1L, "Red");
        Option blue = Util.createOption(2L, "blue");
        Vote v1 = Util.createVote(1L, red);
        Vote v2 = Util.createVote(2L, blue);
        Vote v3 = Util.createVote(3L, blue);
        List<Vote> votes = new ArrayList<>();
        votes.add(v1);
        votes.add(v2);
        votes.add(v3);
        when(voteRepository.findByPoll(anyLong())).thenReturn(votes);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/polls/1/votes"))
                .andDo(print())
                .andExpect(content().string("[{\"id\":1,\"option\":{\"id\":1,\"value\":\"Red\"}},{\"id\":2,\"option\":{\"id\":2,\"value\":\"blue\"}},{\"id\":3,\"option\":{\"id\":2,\"value\":\"blue\"}}]"));
    }
}