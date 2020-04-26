package com.example.restservice.repository;

import com.example.restservice.domain.Poll;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PollRepository extends PagingAndSortingRepository<Poll, Long> {
}
