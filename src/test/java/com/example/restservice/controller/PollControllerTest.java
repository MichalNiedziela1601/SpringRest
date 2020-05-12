package com.example.restservice.controller;

import com.example.restservice.domain.Option;
import com.example.restservice.domain.Poll;
import com.example.restservice.repository.PollRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PollControllerTest {

    @Mock
    private PollRepository pollRepository;

    @InjectMocks
    PollController pollController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pollController)
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
    void testGetAllPolls() throws Exception {
        when(pollRepository.findAll()).thenReturn(new ArrayList<Poll>());

       mockMvc.perform(MockMvcRequestBuilders.get("/polls")
       .param("page","0").param("size","10"))
               .andExpect(status().isOk())
               .andExpect(content().string(""));
    }

    @Test
    void testGetPollById() throws Exception {
        Poll poll = new Poll();
        poll.setPollId(1L);
        poll.setQuestion("Who will win World Cup?");
        Option option1 = new Option();
        option1.setId(2L);
        option1.setValue("UK");
        Set<Option> options = new HashSet<>();
        options.add(option1);
        poll.setOptions(options);
        when(pollRepository.findById(1L)).thenReturn(java.util.Optional.of(poll));

        mockMvc.perform(MockMvcRequestBuilders.get("/polls/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"question\":\"Who will win World Cup?\",\"options\":[{\"id\":2,\"value\":\"UK\"}]}"));
    }

    @Test
    void testCreatePoll() throws Exception {
        Poll newPoll = new Poll();
        newPoll.setPollId(1L);
        newPoll.setQuestion("Who will win World Cup?");
        Option option1 = new Option();
        option1.setId(2L);
        option1.setValue("UK");
        Option option2 = new Option();
        option2.setId(3L);
        option2.setValue("France");
        Set<Option> options = new HashSet<>();
        options.add(option1);
        options.add(option2);
        newPoll.setOptions(options);
        ObjectMapper ob = new ObjectMapper();
        String requestBody = ob.writeValueAsString(newPoll);

        when(pollRepository.save(newPoll)).thenReturn(newPoll);

        mockMvc.perform((MockMvcRequestBuilders.post("/polls")
        .content(requestBody))
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
        .andExpect(header().string("Location","http://localhost/polls/1"));
    }
}