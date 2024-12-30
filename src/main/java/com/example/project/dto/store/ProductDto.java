package com.example.project.dto.store;

import com.example.project.entity.constant.ProductCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDto {
    private Long id;

    private String name;

    private String itemDetails;

    private ProductCategory category;

    private Long price;

    private String image;
}
