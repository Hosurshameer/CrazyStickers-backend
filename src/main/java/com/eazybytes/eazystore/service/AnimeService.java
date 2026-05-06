package com.eazybytes.eazystore.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnimeService {

    @Value("${replicate.api.key}")
    private String replicateApiKey;

    // ✅ keeping exactly as you want
    private final RestTemplate restTemplate = new RestTemplate();

    private final Cloudinary cloudinary;

    public String generateAnime(MultipartFile file, String prompt) throws Exception {

        // 🔹 STEP 1: Upload original image to Cloudinary
        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.emptyMap()
        );

        String imageUrl = uploadResult.get("secure_url").toString();

        // 🔹 STEP 2: Call Replicate
        String url = "https://api.replicate.com/v1/models/prunaai/p-image-edit/predictions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token " + replicateApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Prefer", "wait");

        Map<String, Object> body = Map.of(
                "input", Map.of(
                        "turbo", true,
                        "images", List.of(imageUrl),
                        "prompt", prompt,
                        "aspect_ratio", "1:1"
                )
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(
                url,
                new HttpEntity<>(body, headers),
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();

        // ✅ Safe handling
        if (responseBody == null) {
            throw new RuntimeException("Empty response from Replicate");
        }

        if (responseBody.get("error") != null) {
            throw new RuntimeException("Replicate error: " + responseBody.get("error"));
        }

        Object outputObj = responseBody.get("output");

        String replicateImageUrl;

        if (outputObj instanceof List) {
            List<?> outputList = (List<?>) outputObj;
            replicateImageUrl = outputList.get(0).toString();
        } else if (outputObj instanceof String) {
            replicateImageUrl = outputObj.toString();
        } else {
            throw new RuntimeException("Unexpected output format: " + outputObj);
        }

        // 🔹 STEP 3: Upload AI image to Cloudinary
        Map<String, Object> finalUpload = cloudinary.uploader().upload(
                replicateImageUrl,
                ObjectUtils.emptyMap()
        );

        return finalUpload.get("secure_url").toString();
    }
}