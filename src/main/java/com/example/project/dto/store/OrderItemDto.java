package com.example.project.dto.store;

import com.example.project.entity.store.Order;
import com.example.project.entity.store.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemDto {
    private Long id;

    private Long orderId;

    private Long productId;

    private Long quantity;

    private Long price;
}
