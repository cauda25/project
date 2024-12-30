package com.example.project.repository.store;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.constant.ProductCategory;
import com.example.project.entity.store.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(ProductCategory category);
}
