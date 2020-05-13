package com.example.restservice.controller;

import com.example.restservice.RestServiceApplication;
import com.example.restservice.controller.exceptions.RestExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebAppConfiguration
@SpringBootTest(classes = {RestServiceApplication.class})
@ImportAutoConfiguration(RestExceptionHandler.class)
class ComputeResultControllerTestIT {

    @Inject
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testComputeResultWhenNoVote() throws Exception {
        mockMvc.perform(get("/computeresult?pollId=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"totalVotes\":0,\"results\":[]}"));
    }


}