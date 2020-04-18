package com.example.restservice.domain;

public class Greeting {

    private final long id;
    private final String content;
    private final Quote quote;

    public Greeting(long id, String content, Quote quote) {
        this.id = id;
        this.content = content;
        this.quote = quote;

    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Quote getQuote() {
        return quote;
    }
}
