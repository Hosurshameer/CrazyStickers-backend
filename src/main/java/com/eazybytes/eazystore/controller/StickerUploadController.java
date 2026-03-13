package com.eazybytes.eazystore.controller;


import com.eazybytes.eazystore.entity.StickersEntity;
import com.eazybytes.eazystore.repository.StickerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/stickers")
@RequiredArgsConstructor
public class StickerUploadController {

    private final StickerRepository stickerRepository;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadSticker(@RequestParam("image") MultipartFile file){
     try {

         Path uploadPath = Paths.get("uploads/customer-stickers/");
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String userName=authentication.getName();
         String safeUsername = userName.replace("@", "_").replace(".", "_");
         String fileName = safeUsername +"_"+ UUID.randomUUID() + "_" + file.getOriginalFilename();


         if (!Files.exists(uploadPath)) {
             Files.createDirectories(uploadPath);
         }

         Path filePath=uploadPath.resolve(fileName);
         Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
         String imageUrl="/images/customer-stickers/"+fileName;

         StickersEntity stickersEntity=new StickersEntity();
         stickersEntity.setUsername(userName);
         stickersEntity.setStickerType("custom");
         stickersEntity.setImagePath(imageUrl);
         stickersEntity.setCreatedAt(LocalDateTime.now());

         stickerRepository.save(stickersEntity);

         return ResponseEntity.ok(imageUrl);
     }catch (Exception e){
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload sticker");
     }





    }

    @GetMapping("/my-stickers")
    public ResponseEntity< List<StickersEntity>> getStickers(){

        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();

        String username=authentication.getName();

        List<StickersEntity> stickers=stickerRepository.findByUsername(username);

        return ResponseEntity.ok(stickers);

    }

}
