package com.example.project.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.project.dto.store.OrderItemDto;
import com.example.project.dto.store.ProductDto;
import com.example.project.service.store.OrderItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderItemService orderItemService;

    @GetMapping("/cart")
    public void getCart(Model model) {
        log.info("cart 폼 요청");

        List<OrderItemDto> orderItemDtos = orderItemService.findByOrderId(1L);
        System.out.println(orderItemDtos);

        model.addAttribute("orderItemDtos", orderItemDtos);

    }

}
