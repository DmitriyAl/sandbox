package io.microservices.exchangetoken.targetapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/old")
public class OldController {

    @GetMapping("orders")
    public ResponseEntity<List<String>> getOrders(Principal principal) {
        return ResponseEntity.ok().body(List.of("one", "two", "three", "four", "five"));
    }
}
