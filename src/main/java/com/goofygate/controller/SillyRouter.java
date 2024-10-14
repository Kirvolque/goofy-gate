package com.goofygate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class SillyRouter {

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public SillyRouter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @GetMapping(value = "/gateway", produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<ResponseEntity<String>> goofAround() {
        final String backendServiceUrl = "http://localhost:8081/service";
        return webClientBuilder.build().get().uri(backendServiceUrl)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> ResponseEntity.ok("GoofyGate Forwarded: " + response))
                .defaultIfEmpty(ResponseEntity.badRequest().body("Oops! No goof found behind the service door!"));
    }
}