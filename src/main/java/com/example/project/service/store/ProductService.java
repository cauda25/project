package com.example.project.service.store;

import java.util.List;

import com.example.project.dto.store.ProductDto;
import com.example.project.entity.constant.ProductCategory;
import com.example.project.entity.store.Product;

public interface ProductService {

    List<ProductDto> getAllProducts();

    ProductDto getProduct(Long id);

    List<ProductDto> getProductDtosByCategory(ProductCategory category);

    public default ProductDto entityToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .itemDetails(product.getItemDetails())
                .category(product.getCategory())
                .price(product.getPrice())
                .image(product.getImage())
                .build();
    }

}
