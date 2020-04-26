package com.example.restservice.repository;

import com.example.restservice.domain.Option;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OptionRepository extends PagingAndSortingRepository<Option, Long> {
}
