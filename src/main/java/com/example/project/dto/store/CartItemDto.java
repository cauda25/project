package com.example.project.dto.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemDto {
    private Long id;

    private Long cartId;

    private ProductDto productDto;

    private Long quantity;

    private Long price;
}
