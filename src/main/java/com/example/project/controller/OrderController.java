package com.example.project.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.project.dto.AuthMemberDto;
import com.example.project.dto.MemberDto;
import com.example.project.dto.store.OrderDto;
import com.example.project.dto.store.OrderItemDto;
import com.example.project.entity.constant.OrderStatus;
import com.example.project.service.store.CartItemService;
import com.example.project.service.store.OrderItemService;
import com.example.project.service.store.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Log4j2
@Controller
@RequestMapping("/payment")
public class OrderController {

    private final CartItemService cartItemService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/payment")
    public void getCart(@RequestParam(name = "orderId") long orderId, Model model) {
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/success")
    public void getSuccess(@RequestParam(name = "orderId") Long orderId, Model model) {
        log.info("결제 성공 요청 {}", orderId);
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();
        MemberDto memberDto = authMemberDto.getMemberDto();

        orderService.setStatusCompleted(orderId);
        cartItemService.deleteByOrderId(orderId, memberDto.getMid());
        model.addAttribute("orderId", orderId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/history")
    public void getHistory(Model model) {
        log.info("결제 내역 요청 {}");
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();
        MemberDto memberDto = authMemberDto.getMemberDto();
        List<OrderDto> orderDtos = orderService.getStatusCompleted(memberDto.getMid());
        Collections.reverse(orderDtos);

        model.addAttribute("orderDtos", orderDtos);
    }

}
