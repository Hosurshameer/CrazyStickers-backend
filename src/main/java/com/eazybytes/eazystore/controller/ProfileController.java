package com.eazybytes.eazystore.controller;


import com.cloudinary.Cloudinary;
import com.eazybytes.eazystore.dto.ProfileRequestDto;
import com.eazybytes.eazystore.dto.ProfileResponseDto;
import com.eazybytes.eazystore.service.IProfileService;
import com.eazybytes.eazystore.service.impl.ProfileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {
private final IProfileService iProfileService;
private final Cloudinary cloudinary;

    @GetMapping
    public ResponseEntity<ProfileResponseDto> getProfile(){
        ProfileResponseDto profileResponseDto=iProfileService.getProfile();

    return  ResponseEntity.ok(profileResponseDto);



    }
    @PutMapping
    public  ResponseEntity<ProfileResponseDto> updateProfile(@Validated @RequestBody ProfileRequestDto profileRequestDto){
       ProfileResponseDto profileResponseDto=iProfileService.updateProfile(profileRequestDto);
       return ResponseEntity.ok(profileResponseDto);
    }


}
