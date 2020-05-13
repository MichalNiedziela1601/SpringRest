package com.example.restservice;

import com.example.restservice.domain.Option;
import com.example.restservice.domain.Poll;
import com.example.restservice.domain.Vote;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class Util {

    public static Option createOption(Long id, String value) {
        Option option = new Option();
        option.setId(id);
        option.setValue(value);
        return option;
    }

    public static Vote createVote(Long id, Option option) {
        Vote vote = new Vote();
        vote.setId(id);
        vote.setOption(option);
        return vote;
    }

    public static String obtainAccessToken(MockMvc mvc,String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username",username);
        params.add("password",password);
        params.add("grant_type","password");

        String encoding = Base64.getEncoder().encodeToString("quickpolliOSClient:top_secret".getBytes());

        ResultActions result = mvc.perform(post("/oauth/token").params(params)
                .header("Authorization", "Basic " + encoding)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String contentAsString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(contentAsString).get("access_token").toString();
    }

    public static Poll createPoll(String question, List<String> options) {
        Poll poll = new Poll();
        poll.setQuestion(question);
        Set<Option> pollOptions = new HashSet<>();
        options.stream().forEach(s -> {
            Option option = new Option();
            option.setValue(s);
            pollOptions.add(option);
        });
        poll.setOptions(pollOptions);
        return poll;
    }
}
