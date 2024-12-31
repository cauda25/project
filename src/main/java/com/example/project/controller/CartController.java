package com.example.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.project.service.store.OrderItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping("/cart")
public class CartController {

    @GetMapping("/main")
    public void getCart(@RequestParam(name = "purchaseBtn") Long productId, Model model) {
        log.info("cart 폼 요청: {}", productId);
        model.addAttribute("pramProductId", productId);

    }
}
