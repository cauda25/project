package com.example.project.service.store;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.project.dto.store.OrderDto;
import com.example.project.dto.store.OrderItemDto;
import com.example.project.dto.store.ProductDto;
import com.example.project.entity.Member;
import com.example.project.entity.constant.OrderStatus;
import com.example.project.entity.store.Order;
import com.example.project.entity.store.Product;
import com.example.project.repository.store.OrderItemRepository;
import com.example.project.repository.store.OrderRepository;
import com.example.project.repository.store.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

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
    @Transactional
    public Long createOrder(Long memberId) {
        // OrderStatus가 PENDING 상태인 Order가 존재할 경우 CANCELLED로 변경
        List<Order> orders = orderRepository.findByMemberMidAndStatus(memberId, OrderStatus.PENDING);
        orders.forEach(order -> {
            if (order.getStatus() == OrderStatus.PENDING) {
                order.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);
            }
        });
        // OrderStatus가 CANCELLED 상태인 Order를 가지고 있는 OrderItems가 존재할 경우 삭제
        orders = orderRepository.findByMemberMidAndStatus(memberId, OrderStatus.CANCELLED);
        orders.forEach(order -> {
            if (order.getStatus() == OrderStatus.CANCELLED) {
                orderItemRepository.deleteByOrderId(order.getId());
            }
        });

        // 새로운 Order 생성
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
    // @Transactional
    // @Scheduled(fixedRate = 60000) // 1분마다 실행
    // public void deleteUnCompletedOrder() {
    // LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);

    // // 10분 이상 경과한 데이터를 찾기
    // List<Order> orders = orderRepository.findByUpdateDateBefore(tenMinutesAgo);

    // for (Order order : orders) {
    // if (order.getStatus() == OrderStatus.PENDING) {
    // orderItemRepository.deleteByOrderId(order.getId());
    // order.setStatus(OrderStatus.CANCELLED);
    // }
    // }
    // }

    @Override
    public Boolean isStatusPending(Long orderId) {
        return orderRepository.existsByIdAndStatus(orderId, OrderStatus.PENDING);
    }

}
