package com.example.restservice.controller;

import com.example.restservice.controller.dto.OptionCount;
import com.example.restservice.controller.dto.VoteResult;
import com.example.restservice.repository.VoteRepository;
import com.example.restservice.service.ComputeResultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ComputeResultControllerTest {


    @InjectMocks
    ComputeResultController computeResultController;

    @Mock
    ComputeResultService mockComputeService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(computeResultController)
                .setViewResolvers(new ViewResolver() {
                    @Override
                    public View resolveViewName(String s, Locale locale) throws Exception {
                        return new MappingJackson2JsonView();
                    }
                })
                .build();
    }

    @Test
    void testComputeResult() throws Exception {
        VoteResult voteResult = new VoteResult();
        OptionCount count1 = new OptionCount();
        count1.setCount(2);
        count1.setOptionId(1L);
        OptionCount count2 = new OptionCount();
        count2.setOptionId(2L);
        count2.setCount(1);
        List<OptionCount> optionCounts = new ArrayList<>();
        optionCounts.add(count1);
        optionCounts.add(count2);
        voteResult.setTotalVotes(3);
        voteResult.setResults(optionCounts);
        when(mockComputeService.getPollVoteResults(1L)).thenReturn(voteResult);

        mockMvc.perform(get("/computeresult?pollId=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"totalVotes\":3,\"results\":[{\"optionId\":1,\"count\":2},{\"optionId\":2,\"count\":1}]}"));




    }
}