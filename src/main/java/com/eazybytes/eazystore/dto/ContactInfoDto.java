package com.eazybytes.eazystore.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "contact")
public record ContactInfoDto(String phone,String email,String address) {
}
