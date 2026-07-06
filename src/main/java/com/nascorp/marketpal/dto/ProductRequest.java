package com.nascorp.marketpal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is Required")
    @Positive(message = "Price should be greater than 0")
    private Double price;

    @NotBlank(message = "Category is Required")
    private String category;

    //it comes seperately from cloudinary we no need to define something here.
    private String imageUrl;
}
