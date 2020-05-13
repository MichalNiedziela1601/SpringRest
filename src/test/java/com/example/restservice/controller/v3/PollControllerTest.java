package com.example.restservice.controller.v3;

import com.example.restservice.Util;
import com.example.restservice.domain.Option;
import com.example.restservice.domain.Poll;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
class PollControllerTest {

    @Autowired MockMvc mvc;


    @Test
    void testGetAllWhenUnauthorized() throws Exception {
        mvc.perform(get("/oauth2/v3/polls")).andDo(print())
                .andExpect(status().isUnauthorized())
        .andExpect(content().string("{\"error\":\"unauthorized\",\"error_description\":\"Full authentication is required to access this resource\"}"));
    }

    @Test
    void testGetAllWhenAuthorized() throws Exception {
        String accessToken = Util.obtainAccessToken(mvc,"mickey","cheese");
        mvc.perform(get("/oauth2/v3/polls").header("Authorization","Bearer " + accessToken)).andDo(print())
        .andExpect(status().isOk());
    }

    @Test
    void testGetPollById() throws Exception {
        String accessToken = Util.obtainAccessToken(mvc, "mickey", "cheese");
        mvc.perform((get("/oauth2/v3/polls?pollId=1").header("Authorization", "Bearer " + accessToken))).andDo(print())
                .andExpect(status().isOk())
        .andExpect(jsonPath("content[0].question", is("What is your favorite color?")));
    }

    @Test
    void testCreatePoll() throws Exception {
        List<String> options = Arrays.asList("Warsaw","Cracow","Poznan");
        Poll poll = Util.createPoll("What is a capitol of Poland",  options);
        ObjectMapper ob = new ObjectMapper();
        String pollJson = ob.writeValueAsString(poll);


        String accessToken = Util.obtainAccessToken(mvc, "mickey", "cheese");
        mvc.perform((post("/oauth2/v3/polls").header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
        .content(pollJson))).andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdatePoll() throws Exception {
        List<String> options = Arrays.asList("Warsaw","Cracow","Poznan");
        Poll poll = Util.createPoll("What is a capitol of Poland",  options);
        ObjectMapper ob = new ObjectMapper();
        String pollJson = ob.writeValueAsString(poll);


        String accessToken = Util.obtainAccessToken(mvc, "mickey", "cheese");
        ResultActions authorization = mvc.perform((post("/oauth2/v3/polls").header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(pollJson))).andDo(print())
                .andExpect(status().isCreated());
        String location = authorization.andReturn().getResponse().getHeader("Location");
        Option wroclaw = new Option();
        wroclaw.setValue("Wroclaw");
        poll.getOptions().add(wroclaw);
        String updatePoll = ob.writeValueAsString(poll);

        mvc.perform((put(location).header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(updatePoll))).andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void testDeletePoll() throws Exception {
        List<String> options = Arrays.asList("Warsaw","Cracow","Poznan");
        Poll poll = Util.createPoll("What is a capitol of Poland",  options);
        ObjectMapper ob = new ObjectMapper();
        String pollJson = ob.writeValueAsString(poll);


        String accessToken = Util.obtainAccessToken(mvc, "mickey", "cheese");
        ResultActions authorization = mvc.perform((post("/oauth2/v3/polls").header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(pollJson))).andDo(print())
                .andExpect(status().isCreated());
        String location = authorization.andReturn().getResponse().getHeader("Location");
        mvc.perform((delete(location).header("Authorization","Bearer " + accessToken)))
                .andExpect(status().is(403));

        accessToken = Util.obtainAccessToken(mvc, "superadmin", "nimda");
        mvc.perform((delete(location).header("Authorization","Bearer " + accessToken)))
                .andExpect(status().isOk());

        mvc.perform(get(location).header("Authorization","Bearer " + accessToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetPoll() throws Exception {
        String accessToken = Util.obtainAccessToken(mvc, "mickey", "cheese");
        mvc.perform(get("/oauth2/v3/polls/1").header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk());
    }
}

