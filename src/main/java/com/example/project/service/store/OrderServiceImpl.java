package com.example.project.service.store;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import com.example.project.repository.store.OrderItemRepository;
import com.example.project.repository.store.OrderRepository;
import com.example.project.repository.store.ProductRepository;
import com.querydsl.core.Tuple;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

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
        return entityToDto(orderRepository.findByMemberMidAndStatus(memberId, orderStatus).get(0), null);
    }

    @Override
    public Long createOrder(Long memberId) {
        Order order = Order.builder()
                .status(OrderStatus.PENDING)
                .member(Member.builder().mid(memberId).build())
                .build();
        orderRepository.save(order);

        return order.getId();
    }

    @Override
    public void setStatusCancelled(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    public void setStatusCompleted(Long id) {
        Order order = orderRepository.findById(id).get();
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
    }

    @Override
    public List<OrderDto> getStatusCompleted(Long memberId) {
        return orderRepository.findByMemberMidAndStatus(memberId, OrderStatus.COMPLETED).stream()
                .sorted((order1, order2) -> Long.compare(order1.getId(), order2.getId()))
                .map(entity -> entityToDto(entity,
                        orderItemRepository.findByOrderId(entity.getId()).stream().map(orderItem -> {
                            Product product = productRepository.findById(orderItem.getProduct().getId()).get();
                            return OrderItemDto.builder().orderId(orderItem.getId())
                                    .productDto(ProductDto.builder()
                                            .id(product.getId())
                                            .name(product.getName())
                                            .itemDetails(product.getItemDetails())
                                            .category(product.getCategory())
                                            .price(product.getPrice())
                                            .image(product.getImage())
                                            .build())
                                    .orderId(entity.getId())
                                    .quantity(orderItem.getQuantity())
                                    .price(orderItem.getPrice())
                                    .build();
                        }).collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    // @Override
    // public List<> getStatusCompleted(Long memberId) {
    // List<Tuple> results = orderRepository.getOrderDetails(memberId);
    // for (Tuple tuple : results) {
    // tuple.get()
    // }

    // }

}
