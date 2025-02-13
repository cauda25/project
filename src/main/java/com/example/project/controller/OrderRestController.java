package com.example.project.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.AuthMemberDto;
import com.example.project.dto.MemberDto;
import com.example.project.dto.store.OrderItemDto;
import com.example.project.service.MailService;
import com.example.project.service.store.OrderItemService;
import com.example.project.service.store.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequestMapping("/rest")
@RequiredArgsConstructor
@Log4j2
@RestController
public class OrderRestController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final MailService mailService;

    @PostMapping("/payment")
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

    @GetMapping("/payment")
    public ResponseEntity<String> getIsPending(@RequestParam Long orderId) {
        log.info("아이디: {}", orderId);
        if (!orderService.isStatusPending(orderId)) {
            return ResponseEntity.ok("false");
        }
        return ResponseEntity.ok("true");
    }

    @PostMapping("/success/{orderId}")
    public ResponseEntity<String> postSuccess(
            @RequestBody MemberDto memberDto, @PathVariable Long orderId) {
        log.info("이메일: {}", memberDto.getEmail());
        log.info("주문Id: {}", orderId);

        // 기프티콘 메일 전송
        String m = mailService.mailSend(orderId, memberDto.getEmail());
        log.info(m);
        if (m == "성공") {
            return ResponseEntity.ok("true");
        }
        return ResponseEntity.ok("false");
    }

    @PostMapping("/exit")
    public ResponseEntity<String> removeOrderItemsFromDatabase(@RequestParam Long orderId) {
        log.info("아이디: {}", orderId);
        orderItemService.deleteOrderItems(orderId); // 해당 orderId에 해당하는 item들을 삭제
        orderService.setStatusCancelled(orderId);
        return ResponseEntity.ok("Order items removed from database.");
    }

}
