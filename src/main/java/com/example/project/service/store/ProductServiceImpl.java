package com.example.project.service.store;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.project.dto.store.ProductDto;
import com.example.project.entity.constant.ProductCategory;
import com.example.project.repository.store.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(entity -> entityToDto(entity))
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getProduct(Long id) {
        return entityToDto(productRepository.findById(id).get());
    }

    @Override
    public List<ProductDto> getProductDtosByCategory(ProductCategory productCategory) {
        return productRepository.findByCategory(productCategory)
                .stream()
                .map(entity -> entityToDto(entity))
                .collect(Collectors.toList());
    }

}
