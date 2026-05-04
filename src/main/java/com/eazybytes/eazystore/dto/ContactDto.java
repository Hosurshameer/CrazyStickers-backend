package com.eazybytes.eazystore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class ContactDto {
    @NotBlank(message = "Name is mandatory")
    @Size(min=5,max=30,message = "Name should be between 5 and 30 characters")
    private String name;
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Mobile Number is mandatory")
    @Pattern(regexp = "^\\d{10}$",message = "Mobile Number must be 10 digits")
    private String mobileNumber;
    @NotBlank(message = "Message is mandatory")
    @Size(min=5,max=500,message = "Message should be between 10 and 500 characters")
    private String message;
}