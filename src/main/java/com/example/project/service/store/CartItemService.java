package com.example.project.service.store;

import java.util.List;

import com.example.project.dto.store.CartItemDto;
import com.example.project.dto.store.ProductDto;
import com.example.project.entity.store.Cart;
import com.example.project.entity.store.CartItem;
import com.example.project.entity.store.Product;

public interface CartItemService {

    List<CartItemDto> findByMemberMid(Long memberId);

    void updateCartItem(CartItemDto cartItemDto);

    void deleteCartItem(Long id);

    public default CartItemDto entityToDto(CartItem cartItem, Product product) {

        ProductDto productDto = ProductDto.builder()
                .id(product.getId())
                .category(product.getCategory())
                .image(product.getImage())
                .itemDetails(product.getItemDetails())
                .name(product.getName())
                .price(product.getPrice())
                .build();

        return CartItemDto.builder()
                .id(cartItem.getId())
                .cartId(cartItem.getCart().getId())
                .productDto(productDto)
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .build();
    }

    public default CartItem dtoToEntity(CartItemDto cartItemDto) {
        return CartItem.builder()
                .cart(Cart.builder().id(cartItemDto.getCartId()).build())
                .product(Product.builder().id(cartItemDto.getProductDto().getId()).build())
                .quantity(cartItemDto.getQuantity())
                .price(cartItemDto.getPrice())
                .build();
    }
}
