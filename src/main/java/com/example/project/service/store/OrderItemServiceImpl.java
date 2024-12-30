package com.example.project.service.store;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.project.dto.store.OrderItemDto;
import com.example.project.dto.store.ProductDto;
import com.example.project.entity.Member;
import com.example.project.entity.MemberFavoriteMovie;
import com.example.project.entity.constant.OrderStatus;
import com.example.project.entity.store.Cart;
import com.example.project.entity.store.CartItem;
import com.example.project.entity.store.Order;
import com.example.project.entity.store.OrderItem;
import com.example.project.repository.store.CartItemRepository;
import com.example.project.repository.store.CartRepository;
import com.example.project.repository.store.OrderItemRepository;
import com.example.project.repository.store.OrderRepository;
import com.example.project.repository.store.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Override
    public void addToCart(Long memberId, OrderItemDto orderItemDto) {
        Long totalprice = orderItemDto.getPrice() * orderItemDto.getQuantity();

        if (orderRepository.existsByMemberMidAndStatus(memberId, OrderStatus.CART)) {
            Order cart = orderRepository.findByMemberMidAndStatus(memberId, OrderStatus.CART);
            cart.setTotalPrice(cart.getTotalPrice() + totalprice);
            orderItemDto.setOrderId(cart.getId());
            if (orderItemRepository.existsByOrderIdAndProductId(cart.getId(), orderItemDto.getProductDto().getId())) {
                OrderItem orderItem = orderItemRepository.findByOrderIdAndProductId(cart.getId(),
                        orderItemDto.getProductDto().getId());
                orderItem.setQuantity(orderItemDto.getQuantity() + orderItem.getQuantity());
                orderItemRepository.save(orderItem);
            } else {
                orderItemRepository.save(dtoToEntity(orderItemDto));

            }
        } else {
            Order cart = Order.builder()
                    .member(Member.builder().mid(memberId).build())
                    .totalPrice(totalprice)
                    .status(OrderStatus.CART)
                    .build();
            orderRepository.save(cart);
            orderItemDto.setOrderId(cart.getId());
            if (orderItemRepository.existsByOrderIdAndProductId(cart.getId(), orderItemDto.getProductDto().getId())) {
                OrderItem orderItem = orderItemRepository.findByOrderIdAndProductId(cart.getId(),
                        orderItemDto.getProductDto().getId());
                orderItem.setQuantity(orderItemDto.getQuantity() + orderItem.getQuantity());
                orderItemRepository.save(orderItem);
            } else {
                orderItemRepository.save(dtoToEntity(orderItemDto));

            }
        }

    }

    @Override
    public List<OrderItemDto> findByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId).stream().map(orderItem -> entityToDto(orderItem))
                .collect(Collectors.toList());
        // List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        // List<OrderItemDto> orderItemDtos = new ArrayList<>();
        // for (OrderItem orderItem : orderItems) {
        // orderItemDtos.add(entityToDto(orderItem));
        // }
        // return orderItemDtos;
    }

    @Override
    public void insertOrderItems(Long memberId, Long orderId, List<Long> checkedItems) {
        Long cartId = cartRepository.findByMemberMid(memberId).getId();
        Long totalPrice = 0L;
        for (Long checkedItem : checkedItems) {
            CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cartId, checkedItem);
            totalPrice += cartItem.getPrice() * cartItem.getQuantity();
            OrderItem orderItem = OrderItem.builder()
                    .price(cartItem.getPrice())
                    .quantity(cartItem.getQuantity())
                    .order(orderRepository.findById(orderId).get())
                    .product(productRepository.findById(checkedItem).get())
                    .build();
            orderItemRepository.save(orderItem);
        }
        Order order = orderRepository.findById(orderId).get();
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrderItems(Long orderId) {
        orderItemRepository.deleteByOrderId(orderId);
    }
}
