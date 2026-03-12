package com.eazybytes.eazystore.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/stickers")
public class StickerUploadController {


    @PostMapping("/upload")
    public ResponseEntity<String> uploadSticker(@RequestParam("image") MultipartFile file){
     try {
         String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
         Path uploadPath = Paths.get("uploads/customer-stickers/");

         if (!Files.exists(uploadPath)) {
             Files.createDirectories(uploadPath);
         }
         String str="sameer";
         Path filePath=uploadPath.resolve(fileName);
         Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
         String imageUrl="/images/customer-stickers/"+fileName;

         return ResponseEntity.ok(imageUrl);
     }catch (Exception e){
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload sticker");
     }



    }

}
