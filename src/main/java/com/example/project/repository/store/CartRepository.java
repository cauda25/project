package com.example.project.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.constant.OrderStatus;
import com.example.project.entity.store.Cart;
import com.example.project.entity.store.Order;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberMid(Long memberId);

}
