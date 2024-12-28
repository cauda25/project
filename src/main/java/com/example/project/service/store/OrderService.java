package com.example.project.service.store;

import com.example.project.dto.store.OrderDto;
import com.example.project.entity.Member;
import com.example.project.entity.store.Order;

public interface OrderService {

    // void addToCart(Long memberId, Long productId, int quantity);

    public default OrderDto entityToDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .memberId(order.getMember().getMid())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .build();
    }

    public default Order dtoToEntity(OrderDto orderDto) {
        return Order.builder()
                .member(Member.builder().mid(orderDto.getMemberId()).build())
                .totalPrice(orderDto.getTotalPrice())
                .status(orderDto.getStatus())
                .build();
    }
}
