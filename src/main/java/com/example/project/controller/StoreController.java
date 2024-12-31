package com.example.project.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project.dto.store.ProductDto;
import com.example.project.entity.constant.ProductCategory;
import com.example.project.service.store.ProductService;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping("/store")
public class StoreController {

    private final ProductService productService;

    @GetMapping("/main")
    public void getMain(@RequestParam String category, Model model) {
        log.info("main 폼 요청");
        List<ProductDto> productDtos = new ArrayList<>();
        switch (category) {
            case "combo":
                productDtos = productService.getProductDtosByCategory(ProductCategory.COMBO);
                break;
            case "popcorn":
                productDtos = productService.getProductDtosByCategory(ProductCategory.POPCORN);
                break;
            case "drink":
                productDtos = productService.getProductDtosByCategory(ProductCategory.DRINK);
                break;
            default:
                productDtos = productService.getProductDtosByCategory(ProductCategory.COMBO);
                break;
        }
        model.addAttribute("productDtos", productDtos);
        model.addAttribute("category", category);

    }

    @GetMapping("/productDetail")
    public void getHome(@RequestParam Long id, @RequestParam String category, Model model) {
        log.info("상품 상세 정보 폼 요청");
        log.info("상품 상세 정보 폼 요청: {}", category);
        ProductDto dto = productService.getProduct(id);
        model.addAttribute("dto", dto);
        model.addAttribute("category", category);

    }
}