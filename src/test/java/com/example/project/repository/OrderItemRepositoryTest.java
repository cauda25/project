package com.example.project.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.project.repository.store.OrderItemRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class OrderItemRepositoryTest {

    @Autowired
    private OrderItemRepository orderItemRepository;

    // @Transactional
    @Test
    public void readTest() {
        System.out.println(orderItemRepository.findByOrderId(1L));
    }

}
