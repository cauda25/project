package com.example.project.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.project.dto.AuthMemberDto;
import com.example.project.dto.MemberDto;
import com.example.project.dto.store.CartItemDto;
import com.example.project.dto.store.OrderDto;
import com.example.project.dto.store.OrderItemDto;
import com.example.project.dto.store.ProductDto;
import com.example.project.entity.constant.OrderStatus;
import com.example.project.entity.store.Order;
import com.example.project.repository.store.OrderRepository;
import com.example.project.service.store.CartItemService;
import com.example.project.service.store.OrderItemService;
import com.example.project.service.store.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Log4j2
@Controller
// @RequestMapping("")
public class OrderController {

    private final CartItemService cartItemService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @GetMapping("/order")
    public void getCart(Model model) {
        log.info("cart 폼 요청");

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();
        MemberDto memberDto = authMemberDto.getMemberDto();
        OrderDto orderDto = orderService.getOrderDto(authMemberDto.getMemberDto().getMid(), OrderStatus.PENDING);
        List<OrderItemDto> orderItemDtos = orderItemService.findByOrderId(orderDto.getId());

        model.addAttribute("memberDto", memberDto);
        model.addAttribute("orderDto", orderDto);
        model.addAttribute("orderItemDtos", orderItemDtos);

    }

    @GetMapping("/payment-success")
    public void getSuccess() {
        log.info("결제 성공 요청");

    }

}
