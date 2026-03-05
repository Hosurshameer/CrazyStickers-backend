package com.eazybytes.eazystore.controller;

import com.eazybytes.eazystore.dto.ContactDto;
import com.eazybytes.eazystore.dto.ContactInfoDto;
import com.eazybytes.eazystore.service.IContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final IContactService iContactService;
    private final ContactInfoDto contactInfoDto;

    @PostMapping

    public ResponseEntity<String> saveContact(@Valid @RequestBody ContactDto contactDto){
        iContactService.saveContact(contactDto);


            return ResponseEntity.status(HttpStatus.CREATED).body("Request processed successfully");

    }
     @GetMapping
    public ResponseEntity<ContactInfoDto> getContactInfo(){
        return ResponseEntity.ok(contactInfoDto);

    }
}
