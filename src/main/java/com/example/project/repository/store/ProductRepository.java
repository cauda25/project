package com.example.project.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.store.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
