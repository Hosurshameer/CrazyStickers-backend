package com.eazybytes.eazystore.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    private final RestTemplate restTemplate = new RestTemplate();
    private final Cloudinary cloudinary;



    public String generateAnime(MultipartFile file) throws Exception {

        // 🔹 STEP 1: Upload original image to Cloudinary
        Map uploadResult = cloudinary.uploader().upload(
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
                        "images", List.of(Map.of("value", imageUrl)),
                        "prompt", "anime style, high quality, detailed face",
                        "aspect_ratio", "1:1"
                )
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(
                url,
                new HttpEntity<>(body, headers),
                Map.class
        );

        Map responseBody = response.getBody();

        if (responseBody == null || responseBody.get("output") == null) {
            throw new RuntimeException("Replicate failed: " + responseBody);
        }

        List<String> output = (List<String>) responseBody.get("output");
        String replicateImageUrl = output.get(0);


        // 🔹 STEP 3: Upload AI image to Cloudinary
        Map finalUpload = cloudinary.uploader().upload(
                replicateImageUrl,
                ObjectUtils.emptyMap()
        );

        return finalUpload.get("secure_url").toString();
    }
}
