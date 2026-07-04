package com.nascorp.marketpal.service;

import com.nascorp.marketpal.dto.ProductRequest;
import com.nascorp.marketpal.dto.ProductResponse;
import com.nascorp.marketpal.entity.Product;
import com.nascorp.marketpal.entity.User;
import com.nascorp.marketpal.repository.ProductRepository;
import com.nascorp.marketpal.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .category(product.getCategory())
                .sellerUsername(product.getSeller().getUsername())
                .createdAt(product.getCreatedAt())
                .build();
    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        User seller = getCurrentUser();

        Product product = Product.builder()
                .title(productRequest.getTitle())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .imageUrl(productRequest.getImageUrl())
                .category(productRequest.getCategory())
                .seller(seller)
                .build();

        return toResponse(productRepository.save(product));
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
               .stream()
               .map(this::toResponse)
               .collect(Collectors.toList());
    }

    public ProductResponse getProducts(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return toResponse(product);
    }

    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User currentUser = getCurrentUser();

        if(!product.getSeller().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to edit the product");
        }

        product.setTitle(productRequest.getTitle());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setImageUrl(productRequest.getImageUrl());
        product.setCategory(productRequest.getCategory());

        return toResponse(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User currentUser = getCurrentUser();

        if(!product.getSeller().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to delete this product");
        }

        productRepository.delete(product);
    }

    public List<ProductResponse> getMyProducts() {
        User currentUser = getCurrentUser();
        return productRepository.findBySeller(currentUser)
               .stream()
               .map(this::toResponse)
               .collect(Collectors.toList());
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.findByTitleContainingIgnoreCase(keyword)
               .stream()
               .map(this::toResponse)
               .collect(Collectors.toList());
    }


}