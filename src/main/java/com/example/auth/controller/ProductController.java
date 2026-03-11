package com.example.auth.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth.entity.Product;
import com.example.auth.service.ProductService;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "https://marketpal.vercel.app    ") // ✅ Allow React frontend
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // ✅ Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }

    // ✅ Get single product by ID
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return service.getProduct(id);
    }

    // ✅ Search by title or description
    @GetMapping("/search")
    public List<Product> search(@RequestParam String q) {
        return service.searchProducts(q);
    }

    // ✅ Filter by category
    @GetMapping("/category/{category}")
    public List<Product> getByCategory(@PathVariable String category) {
        return service.getByCategory(category);
    }

    // ✅ Add new product
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        System.out.println("📦 Received NEW product:");
        System.out.println("→ Title: " + product.getTitle());
        System.out.println("→ Image URL: " + product.getImageUrl());
        return service.addProduct(product);
    }

    // ✅ Update existing product
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product updated) {
        System.out.println("✏️ Updating PRODUCT ID: " + id);
        System.out.println("→ New title: " + updated.getTitle());
        System.out.println("→ New image URL: " + updated.getImageUrl());
        return service.updateProduct(id, updated);
    }

    // ✅ Delete product
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        System.out.println("🗑️ Deleting product ID: " + id);
        service.deleteProduct(id);
    }
}
