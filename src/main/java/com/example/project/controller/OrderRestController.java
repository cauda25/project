package com.example.project.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.AuthMemberDto;
import com.example.project.dto.MovieDto;
import com.example.project.dto.PageRequestDTO;
import com.example.project.dto.ReviewDto;
import com.example.project.dto.store.CartItemDto;
import com.example.project.dto.store.OrderItemDto;
import com.example.project.service.store.CartService;
import com.example.project.service.store.OrderItemService;
import com.example.project.service.store.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequestMapping("/rest")
@RequiredArgsConstructor
@Log4j2
@RestController
public class OrderRestController {

    private final CartService cartService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @PostMapping("/order")
    public ResponseEntity<List<OrderItemDto>> postCartItem(
            @RequestBody List<Long> checkedItems) {
        log.info("rest 상품 상세 요청 {}", checkedItems);

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();
        Long orderId = orderService.createOrder(authMemberDto.getMemberDto().getMid());
        orderItemService.insertOrderItems(authMemberDto.getMemberDto().getMid(), orderId, checkedItems);
        List<OrderItemDto> orderItemDtos = orderItemService.findByOrderId(orderId);

        return new ResponseEntity<>(orderItemDtos, HttpStatus.OK);
    }

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

    @PostMapping("/exit")
    public ResponseEntity<String> removeOrderItemsFromDatabase(@RequestParam Long orderId) {
        log.info("아이디: {}", orderId);
        orderItemService.deleteOrderItems(orderId); // 해당 orderId에 해당하는 item들을 삭제
        orderService.setStatusCancelled(orderId);
        return ResponseEntity.ok("Order items removed from database.");
    }

}
