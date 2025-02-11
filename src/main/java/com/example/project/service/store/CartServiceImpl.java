package com.example.project.service.store;

import org.springframework.stereotype.Service;

import com.example.project.dto.store.CartItemDto;
import com.example.project.entity.store.Cart;
import com.example.project.entity.store.CartItem;
import com.example.project.entity.store.Product;
import com.example.project.repository.store.CartItemRepository;
import com.example.project.repository.store.CartRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public void addToCart(Long memberId, CartItemDto cartItemDto) {
        // 해당 멤버의 카트 찾기
        Cart cart = cartRepository.findByMemberMid(memberId);

        // 해당 멤버의 카트 아이템에 해당 상품이 존재하는지 여부
        if (!cartItemRepository.existsByCartIdAndProductId(cart.getId(), cartItemDto.getProductDto().getId())) {
            // 없다면 새로 생성 후 담기
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .product(Product.builder().id(cartItemDto.getProductDto().getId()).build())
                    .quantity(cartItemDto.getQuantity())
                    .price(cartItemDto.getPrice())
                    .build();
            cartItemRepository.save(cartItem);
        } else {
            // 있다면 수량 추가 후 업데이트
            CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(),
                    cartItemDto.getProductDto().getId());
            cartItem.setQuantity(cartItem.getQuantity() + cartItemDto.getQuantity() < 10L
                    ? cartItem.getQuantity() + cartItemDto.getQuantity()
                    : 10L);
            cartItemRepository.save(cartItem);
        }

    }

}
