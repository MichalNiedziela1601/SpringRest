package com.example.restservice.controller;

import com.example.restservice.RestServiceApplication;
import com.example.restservice.Util;
import com.example.restservice.controller.exceptions.ResourceNotFoundException;
import com.example.restservice.controller.exceptions.RestExceptionHandler;
import com.example.restservice.domain.Option;
import com.example.restservice.domain.Poll;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@SpringBootTest(classes = {RestServiceApplication.class})
@ImportAutoConfiguration(RestExceptionHandler.class)
public class PollControllerTestIT {

    @Inject
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetAllPolls() throws Exception {
        mockMvc.perform(get("/polls"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", hasSize(5)));
    }

    @Test
    void testCreatePoll() throws Exception {
        List<String> options = Arrays.asList("Warsaw","Cracow","Poznan");
        Poll poll = Util.createPoll("What is a capitol of Poland",  options);
        ObjectMapper ob = new ObjectMapper();
        String pollJson = ob.writeValueAsString(poll);


        mockMvc.perform((post("/polls")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
                .content(pollJson))).andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void testGetPollByIdWhenPollNotExistsTHenThrowException() throws Exception {
        mockMvc.perform(get("/polls/100"))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof ResourceNotFoundException))
                .andDo(print())
        .andExpect(jsonPath("title", is("Resource not found")));
    }

    @Test
    void testCreatePollWhenQuestionEmptyThenThrowException() throws Exception {
        Poll poll = new Poll();
        poll.setQuestion("");
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(poll);
        mockMvc.perform(post("/polls").contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
        .andExpect(jsonPath("title", is("Validation Failed")))
        .andExpect(jsonPath("errors.question[0].message", is("Question is a required field")));
    }

    @Test
    void testCreatePollWhenOptionEmptyThenThrowException() throws Exception {
        Poll poll = new Poll();
        poll.setId(1L);
        poll.setQuestion("sdfsdf");
        Set<Option> options = new HashSet<>();
        poll.setOptions(options);
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(poll);
        mockMvc.perform(post("/polls").contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("title", is("Validation Failed")))
                .andExpect(jsonPath("errors.options[0].message", is("Options must be grater than 2 and less than 6")));
    }

    @Test
    void testCreatePollWhenOptionHaveMoreThen6valuesThenThrowException() throws Exception {
        Option op1 = new Option();
        op1.setId(1L);
        op1.setValue("Borys");
        Option op2 = new Option();
        op2.setId(2L);
        op2.setValue("Thomas");
        Option op3 = new Option();
        op3.setId(3L);
        op3.setValue("George");
        Option op4 = new Option();
        op4.setId(4L);
        op4.setValue("James");
        Option op5 = new Option();
        op5.setId(5L);
        op5.setValue("Jacob");
        Option op6 = new Option();
        op6.setId(6L);
        op6.setValue("Michal");
        Option op7 = new Option();
        op7.setId(7L);
        op7.setValue("Chris");
        Poll poll = new Poll();
        poll.setId(1L);
        poll.setQuestion("sdfsdf");
        Set<Option> options = new HashSet<>();
        options.add(op1);
        options.add(op2);
        options.add(op3);
        options.add(op4);
        options.add(op5);
        options.add(op6);
        options.add(op7);
        poll.setOptions(options);
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(poll);
        mockMvc.perform(post("/polls").contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("title", is("Validation Failed")))
                .andExpect(jsonPath("errors.options[0].message", is("Options must be grater than 2 and less than 6")));
    }

    @Test
    void testWhenJsonNotReadable() throws Exception {
        mockMvc.perform(post("/polls").contentType(MediaType.APPLICATION_JSON)
                .content("{\"question\":\"sdf\",\"options\":[{\"value\":1},{\"value\":2}],\"sdf\":\"dsfsd\""))
                .andDo(print())
        .andExpect(jsonPath("title",is("Message not readable")))
        .andExpect(status().isBadRequest());
    }
}
