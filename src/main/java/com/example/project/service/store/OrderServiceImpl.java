package com.example.project.service.store;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.project.dto.store.OrderDto;
import com.example.project.dto.store.OrderItemDto;
import com.example.project.dto.store.ProductDto;
import com.example.project.entity.Member;
import com.example.project.entity.constant.OrderStatus;
import com.example.project.entity.store.Order;
import com.example.project.entity.store.OrderItem;
import com.example.project.entity.store.Product;
import com.example.project.repository.store.OrderRepository;
import com.example.project.repository.store.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    // @Override
    // public void addToCart(Long memberId, OrderItemDto orderItemDto) {
    // // 1. 장바구니(OrderStatus = CART) 상태인 Order를 조회하거나 새로 생성
    // Long totalprice = orderItemDto.getPrice() * orderItemDto.getQuantity();

    // if (orderRepository.existsByMemberIdAndStatus(memberId, OrderStatus.CART)) {
    // Order cart = orderRepository.findByMemberIdAndStatus(memberId,
    // OrderStatus.CART);
    // cart.setTotalPrice(cart.getTotalPrice() + totalprice);
    // } else {
    // Order cart = Order.builder()
    // .member(Member.builder().mid(memberId).build())
    // .totalPrice(totalprice)
    // .status(OrderStatus.CART)
    // .build();
    // }
    // }

    private Order createNewCart(Long memberId) {
        Order cart = new Order();
        cart.setMember(Member.builder().mid(memberId).build());
        cart.setStatus(OrderStatus.CART);
        return orderRepository.save(cart);
    }

    @Override
    public OrderDto getOrderDto(Long memberId, OrderStatus orderStatus) {
        return entityToDto(orderRepository.findByMemberMidAndStatus(memberId, orderStatus));
    }

}
