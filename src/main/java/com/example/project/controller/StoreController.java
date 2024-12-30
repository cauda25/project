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
import com.example.project.service.store.ProductService;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping("/store")
public class StoreController {

    private final ProductService productService;

    @GetMapping("/main")
    public void getMain(Model model) {
        log.info("main 폼 요청");
        List<ProductDto> productDtos = productService.getAllProducts();
        model.addAttribute("productDtos", productDtos);

    }

    @GetMapping("/productDetail")
    public void getHome(@RequestParam Long id, Model model) {
        log.info("home 폼 요청");
        ProductDto dto = productService.getProduct(id);
        model.addAttribute("dto", dto);

    }
}