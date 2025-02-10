package com.example.project.service.store;

import java.util.List;

import com.example.project.dto.store.OrderDto;
import com.example.project.dto.store.OrderItemDto;
import com.example.project.entity.Member;
import com.example.project.entity.constant.OrderStatus;
import com.example.project.entity.store.Order;

public interface OrderService {

    // void addToCart(Long memberId, Long productId, int quantity);

    OrderDto getOrderDto(Long memberId, OrderStatus orderStatus);

    Long createOrder(Long memberId);

    void setStatusCancelled(Long id);

    void setStatusCompleted(Long id);

    // List<Tuple> getStatusCompleted(Long memberId);

    List<OrderDto> getStatusCompleted(Long memberId);

    public default OrderDto entityToDto(Order order, List<OrderItemDto> orderItem) {

        return OrderDto.builder()
                .id(order.getId())
                .memberId(order.getMember().getMid())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .orderItemDtos(orderItem)
                .updateDate(order.getUpdateDate())
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
