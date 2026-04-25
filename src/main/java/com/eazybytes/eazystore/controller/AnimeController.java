package com.eazybytes.eazystore.controller;

import com.eazybytes.eazystore.service.AnimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AnimeController {

    private final AnimeService animeService;



    @PostMapping("/anime")
    public ResponseEntity<?> generate(@RequestParam String imageUrl) {
        try {
            String result = animeService.generateAnime(imageUrl);
            return ResponseEntity.ok(Map.of("imageUrl", result));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
