package com.example.project.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.store.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberMid(Long memberId);

}
