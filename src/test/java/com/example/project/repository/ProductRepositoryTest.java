package com.example.project.repository;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.project.entity.constant.ProductCategory;
import com.example.project.entity.store.Product;
import com.example.project.repository.store.ProductRepository;

@SpringBootTest
public class ProductRepositoryTest {

        @Autowired
        private ProductRepository productRepository;

        @Test
        public void createTest() {
                List<List<Object>> products = Arrays.asList(
                                Arrays.asList("스위트콤보", "오리지널팝콘L+탄산음료M2", ProductCategory.COMBO, 11000L,
                                                "/15f9c480faef496283bcbc443aba4f3b.jpg"),
                                Arrays.asList("더블콤보", "오리지널팝콘M2+탄산음료M2", ProductCategory.COMBO, 15000L,
                                                "/9c05d8b90a04441fa151e5cc5f70bf07.jpg"),
                                Arrays.asList("콜라 M", "콜라M", ProductCategory.DRINK, 3000L,
                                                "/ba1ecc27c7a54259b4b5c1884ad24de9.jpg"),
                                Arrays.asList("콜라 L", "콜라L", ProductCategory.DRINK, 3500L,
                                                "/c40b844a2e8b4bdc9f0f7131a4262ab6.jpg"),
                                Arrays.asList("사이다 M", "사이다M", ProductCategory.DRINK, 3000L,
                                                "/97953e47fc2e46d1b8b6397effd84ecc.jpg"),
                                Arrays.asList("사이다 L", "사이다L", ProductCategory.DRINK, 3500L,
                                                "/2e013d0db16c4202a5f71b7706c1853e.jpg"),
                                Arrays.asList("오리지널팝콘 L", "오리지널팝콘L", ProductCategory.POPCORN, 6000L,
                                                "/8827dc6093994a4ca3b70bce78b44fba.jpg"),
                                Arrays.asList("오리지널팝콘 M", "오리지널팝콘M", ProductCategory.POPCORN, 5000L,
                                                "/ca8bad8b2f914beca5631b2ab200eaf0.jpg"),
                                Arrays.asList("카라멜팝콘 L", "카라멜팝콘L", ProductCategory.POPCORN, 6000L,
                                                "/8827dc6093994a4ca3b70bce78b44fba.jpg"),
                                Arrays.asList("카라멜팝콘 M", "카라멜팝콘M", ProductCategory.POPCORN, 5000L,
                                                "/ca8bad8b2f914beca5631b2ab200eaf0.jpg"));

                products.forEach(product -> {
                        Product entity = Product.builder()
                                        .name((String) product.get(0))
                                        .itemDetails(((String) product.get(1)))
                                        .category((ProductCategory) product.get(2))
                                        .price((Long) product.get(3))
                                        .image((String) product.get(4))
                                        .build();
                        productRepository.save(entity);
                });
        }

        @Test
        public void readTest() {
                System.out.println(productRepository.findById(2L).get());
        }

        @Test
        public void readAllTest() {
                productRepository.findAll().forEach(product -> System.out.println(product));
        }

}
