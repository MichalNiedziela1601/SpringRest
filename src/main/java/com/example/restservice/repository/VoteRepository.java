package com.example.restservice.repository;

import com.example.restservice.domain.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VoteRepository extends PagingAndSortingRepository<Vote, Long> {

    @Query(value = "select v.* from Option o, Vote v where o.POLL_ID = ?1 and v.OPTION_ID = o.OPTION_ID", nativeQuery = true)
    public Iterable<Vote> findByPoll(Long pollId);
}
