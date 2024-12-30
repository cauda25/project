package com.example.project.repository.store;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.entity.store.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCartId(Long cartId);

    boolean existsByCartIdAndProductId(Long cartId, Long productId);

    CartItem findByCartIdAndProductId(Long cartId, Long productId);

}
