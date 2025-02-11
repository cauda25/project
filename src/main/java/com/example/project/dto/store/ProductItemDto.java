package com.example.project.dto.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductItemDto {
    private Long id;

    private Long cartId;

    private Long productId;

    private Long quantity;

    private Long price;
}
