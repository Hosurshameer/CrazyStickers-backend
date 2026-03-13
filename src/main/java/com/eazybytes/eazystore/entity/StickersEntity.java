package com.eazybytes.eazystore.entity;


import jakarta.persistence.*;
//import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "custom_stickers")
public class StickersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  String username;

    private String imagePath;

    private String stickerType;

    private LocalDateTime createdAt;
}
