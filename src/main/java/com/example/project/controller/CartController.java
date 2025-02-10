package com.example.project.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping("/cart")
public class CartController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/main")
    public void getCart(@RequestParam(name = "purchaseBtn") Long productId, Model model) {
        log.info("cart 폼 요청: {}", productId);
        model.addAttribute("pramProductId", productId);

    }
}
