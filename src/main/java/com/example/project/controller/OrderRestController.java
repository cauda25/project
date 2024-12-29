package com.example.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.AuthMemberDto;
import com.example.project.dto.MovieDto;
import com.example.project.dto.PageRequestDTO;
import com.example.project.dto.ReviewDto;
import com.example.project.dto.store.CartItemDto;
import com.example.project.dto.store.OrderItemDto;
import com.example.project.service.store.CartService;
import com.example.project.service.store.OrderItemService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequestMapping("/rest")
@RequiredArgsConstructor
@Log4j2
@RestController
public class OrderRestController {

    private final CartService cartService;

    // @PostMapping("/cart/add")
    // public ResponseEntity<CartItemDto> postCartItem(
    // @RequestBody CartItemDto cartItemDto) {
    // log.info("rest 상품 상세 요청 {}", cartItemDto);

    // SecurityContext context = SecurityContextHolder.getContext();
    // Authentication authentication = context.getAuthentication();
    // AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();
    // cartService.addToCart(authMemberDto.getMemberDto().getMid(), cartItemDto);

    // return new ResponseEntity<>(cartItemDto, HttpStatus.OK);
    // }

    // @GetMapping("/cart/add")
    // public ResponseEntity<String> getPostItem(
    // @ModelAttribute("orderItemDto") @RequestBody CartItemDto cartItemDto) {
    // log.info("rest 상품 상세 요청 {}", cartItemDto);

    // // SecurityContext context = SecurityContextHolder.getContext();
    // // Authentication authentication = context.getAuthentication();
    // // AuthMemberDto authMemberDto = (AuthMemberDto)
    // authentication.getPrincipal();
    // // orderItemService.addToCart(authMemberDto.getMemberDto().getMid(),
    // // orderItemDto);

    // return ResponseEntity.ok("장바구니 저장 완료");
    // }

}
