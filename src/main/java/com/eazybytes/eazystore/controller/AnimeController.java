package com.eazybytes.eazystore.controller;

import com.eazybytes.eazystore.service.AnimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AnimeController {

    private final AnimeService animeService;



    @PostMapping("/anime")
    public ResponseEntity<?> generate(@RequestParam("file") MultipartFile file) throws Exception {

        String result = animeService.generateAnime(file);

        return ResponseEntity.ok(Map.of("imageUrl", result));
    }
}
