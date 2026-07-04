package com.nascorp.marketpal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ProductResponse {
    
    private Long id;
    private String title;
    private String description;
    private Double price;
    private String imageUrl;
    private String category;
    //for security purposes only expose username of the seller not whole dataset
    private String sellerUsername;
    private LocalDateTime createdAt;
}
