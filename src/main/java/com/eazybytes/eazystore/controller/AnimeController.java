package com.eazybytes.eazystore.controller;

import com.eazybytes.eazystore.service.AnimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AnimeController {

    private final AnimeService animeService;

    @PostMapping("/anime")
    public ResponseEntity<?> generate(
            @RequestParam("file") MultipartFile file,
            @RequestParam("prompt") String prompt
    ) throws Exception {

        // ✅ Validation
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File cannot be empty");
        }

        if (prompt == null || prompt.isBlank()) {
            throw new RuntimeException("Prompt is required");
        }

        String result = animeService.generateAnime(file, prompt);

        return ResponseEntity.ok(Map.of("imageUrl", result));
    }
}