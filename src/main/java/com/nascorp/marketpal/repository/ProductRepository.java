package com.nascorp.marketpal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nascorp.marketpal.entity.User;
import com.nascorp.marketpal.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findBySeller(User seller);

    List<Product> findByTitleContainingIgnoreCase(String keyword);

    List<Product> findByCategory(String category);

    
}