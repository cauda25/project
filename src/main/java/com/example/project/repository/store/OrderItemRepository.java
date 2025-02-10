package com.example.project.repository.store;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.store.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @EntityGraph(attributePaths = { "order", "product", "order.member" }, type = EntityGraphType.LOAD)
    List<OrderItem> findByOrderId(Long orderId);

    OrderItem findByOrderIdAndProductId(Long orderId, Long productId);

    boolean existsByOrderIdAndProductId(Long orderId, Long productId);

    void deleteByOrderId(Long orderId);

}
