package com.example.project.service.store;

import java.util.List;

import com.example.project.dto.store.OrderItemDto;
import com.example.project.dto.store.ProductDto;
import com.example.project.entity.store.Order;
import com.example.project.entity.store.OrderItem;
import com.example.project.entity.store.Product;

public interface OrderItemService {

    void addToCart(Long memberId, OrderItemDto orderItemDto);

    List<OrderItemDto> findByOrderId(Long orderId);

    public default OrderItemDto entityToDto(OrderItem orderItem) {
        return OrderItemDto.builder()
                .id(orderItem.getId())
                .orderId(orderItem.getOrder().getId())
                .productId(orderItem.getProduct().getId())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }

    public default OrderItem dtoToEntity(OrderItemDto orderItemDto) {
        return OrderItem.builder()
                .id(orderItemDto.getId())
                .order(Order.builder().id(orderItemDto.getOrderId()).build())
                .product(Product.builder().id(orderItemDto.getProductId()).build())
                .quantity(orderItemDto.getQuantity())
                .price(orderItemDto.getPrice())
                .build();
    }

}
