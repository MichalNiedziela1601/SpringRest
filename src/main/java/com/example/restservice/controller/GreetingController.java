package com.example.restservice.controller;

import com.example.restservice.domain.Greeting;
import com.example.restservice.domain.Quote;
import com.example.restservice.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {
    private static final String template = "Hello, %s";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    QuoteService quoteService;

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        Quote quote = quoteService.call();
        return new Greeting(counter.incrementAndGet(),String.format(template,name), quote);
    }
}
