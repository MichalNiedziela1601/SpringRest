package com.example.restservice.service;

import com.example.restservice.controller.dto.OptionCount;
import com.example.restservice.controller.dto.VoteResult;
import com.example.restservice.domain.Poll;
import com.example.restservice.domain.Vote;
import com.example.restservice.repository.VoteRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Service
public class ComputeResultService {

    @Inject
    private VoteRepository voteRepository;

    public VoteResult getPollVoteResults(Long pollId) {
        Iterable<Vote> allVotes = voteRepository.findByPoll(pollId);
        VoteResult result = new VoteResult();
        Map<Long, OptionCount> tempMap = new HashMap<>();
        int totalVOtes = 0;
        for(Vote v : allVotes) {
            totalVOtes++;
            OptionCount optionCount = tempMap.get(v.getOption().getId());
            if(optionCount == null) {
                optionCount = new OptionCount();
                optionCount.setOptionId(v.getOption().getId());
                tempMap.put(v.getOption().getId(), optionCount);
            }
            optionCount.setCount(optionCount.getCount()+1);
        }
        result.setTotalVotes(totalVOtes);
        result.setResults(tempMap.values());
        return result;
    }
}
