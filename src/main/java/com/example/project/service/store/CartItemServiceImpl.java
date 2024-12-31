package com.example.project.service.store;

import java.util.List;
import java.util.stream.Collectors;

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
public class CartItemServiceImpl implements CartItemService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public List<CartItemDto> findByMemberMid(Long memberId) {
        // 해당 멤버의 카트 찾기
        Cart cart = cartRepository.findByMemberMid(memberId);

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        List<CartItemDto> cartItemDtos = cartItems.stream()
                .map(cartItem -> entityToDto(cartItem, cartItem.getProduct()))
                .collect(Collectors.toList());

        return cartItemDtos;
    }

    @Override
    public void updateCartItem(CartItemDto cartItemDto) {
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cartItemDto.getCartId(),
                cartItemDto.getProductDto().getId());

        cartItem.setQuantity(cartItemDto.getQuantity());
        cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }

}
