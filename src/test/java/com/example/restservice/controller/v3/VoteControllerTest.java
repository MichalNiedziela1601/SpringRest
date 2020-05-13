package com.example.restservice.controller.v3;

import com.example.restservice.Util;
import com.example.restservice.domain.Option;
import com.example.restservice.domain.Poll;
import com.example.restservice.domain.Vote;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VoteControllerTest {

    @Autowired
    private MockMvc mvc;


    @Test
    void testCreateVote() throws Exception {
        String accessToken = Util.obtainAccessToken(mvc, "mickey", "cheese");
        MvcResult getPoll = mvc.perform(get("/oauth2/v3/polls/1").header("Authorization", "Bearer " + accessToken)).andReturn();
        String pollString = getPoll.getResponse().getContentAsString();
        ObjectMapper ob = new ObjectMapper();
        Poll poll = ob.readValue(pollString, Poll.class);
        Option option1 = poll.getOptions().stream().filter(option -> option.getId() == 1).collect(Collectors.toList()).get(0);
        Vote vote1 = new Vote();
        vote1.setOption(option1);
        String body = ob.writeValueAsString(vote1);


        mvc.perform(post("/oauth2/v3/polls/1/votes").header("Authorization", "Bearer " + accessToken)
                .content(body).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/oauth2/v3/polls/1/votes").header("Authorization", "Bearer " + accessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].option.value", is("Red")));
    }
}