package com.example.project.service.store;

import com.example.project.dto.store.CartItemDto;

public interface CartService {

    void addToCart(Long memberId, CartItemDto cartItemDto);

}
