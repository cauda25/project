package com.example.project.service.store;

import java.util.List;

import com.example.project.dto.store.CartItemDto;
import com.example.project.dto.store.OrderItemDto;
import com.example.project.dto.store.ProductDto;
import com.example.project.entity.store.Cart;
import com.example.project.entity.store.Order;
import com.example.project.entity.store.OrderItem;
import com.example.project.entity.store.Product;

public interface CartService {

    void addToCart(Long memberId, CartItemDto cartItemDto);

}
