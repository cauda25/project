package com.example.project.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.constant.OrderStatus;
import com.example.project.entity.store.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByMemberMidAndStatus(Long memberId, OrderStatus orderStatus);

    boolean existsByMemberMidAndStatus(Long memberId, OrderStatus orderStatus);

}
