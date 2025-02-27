package com.example.project.dto.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemDto {
    private Long id;

    private Long orderId;

    private ProductDto productDto;

    private Long quantity;

    private Long price;
}
