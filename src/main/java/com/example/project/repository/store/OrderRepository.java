package com.example.project.repository.store;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.constant.OrderStatus;
import com.example.project.entity.store.Order;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository {

    // Order findByMemberMidAndStatus(Long memberId, OrderStatus orderStatus);

    List<Order> findByMemberMidAndStatus(Long memberId, OrderStatus orderStatus);

    boolean existsByMemberMidAndStatus(Long memberId, OrderStatus orderStatus);

}
