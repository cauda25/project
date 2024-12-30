package com.example.project.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.project.repository.store.CartItemRepository;

@SpringBootTest
public class CartItemRepositoryTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Test
    public void deleteTest() {
        cartItemRepository.deleteById(6L);
    }

}
