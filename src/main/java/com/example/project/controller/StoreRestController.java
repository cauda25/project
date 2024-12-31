package com.example.project.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.dto.AuthMemberDto;
import com.example.project.dto.MemberDto;
import com.example.project.dto.MovieDto;
import com.example.project.dto.PageRequestDTO;
import com.example.project.dto.ReviewDto;
import com.example.project.dto.store.CartItemDto;
import com.example.project.dto.store.OrderItemDto;
import com.example.project.dto.store.ProductDto;
import com.example.project.service.MemberService;
import com.example.project.service.store.CartItemService;
import com.example.project.service.store.CartService;
import com.example.project.service.store.OrderItemService;
import com.example.project.service.store.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequestMapping("/rest")
@RequiredArgsConstructor
@Log4j2
@RestController
public class StoreRestController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final ProductService productService;
    private final MemberService memberService;

    @PostMapping("/cart/add/{productId}")
    public ResponseEntity<CartItemDto> postCartItem(@PathVariable(name = "productId") Long productId,
            @RequestBody CartItemDto cartItemDto) {
        log.info("rest 상품 상세 요청 {}", productId);
        log.info("rest 상품 상세 요청 {}", cartItemDto);

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();

        ProductDto productDto = productService.getProduct(productId);
        cartItemDto.setProductDto(productDto);
        cartService.addToCart(authMemberDto.getMemberDto().getMid(), cartItemDto);

        return new ResponseEntity<>(cartItemDto, HttpStatus.OK);
    }

    @GetMapping("/cart/add")
    public ResponseEntity<String> getCartItem(
            @ModelAttribute("orderItemDto") @RequestBody CartItemDto cartItemDto) {
        log.info("rest 상품 상세 요청 {}", cartItemDto);

        // SecurityContext context = SecurityContextHolder.getContext();
        // Authentication authentication = context.getAuthentication();
        // AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();
        // orderItemService.addToCart(authMemberDto.getMemberDto().getMid(),
        // orderItemDto);

        return ResponseEntity.ok("장바구니 저장 완료");
    }

    @GetMapping("/cart/list")
    public ResponseEntity<List<CartItemDto>> getCartItemList(Principal principal) {
        log.info("rest 상품 상세 요청");

        // SecurityContext context = SecurityContextHolder.getContext();
        // Authentication authentication = context.getAuthentication();
        // AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();
        String memberId = principal.getName();
        log.info("멤버 아이디", memberId);
        MemberDto memberDto = memberService.getMemberById(memberId);
        List<CartItemDto> cartItemDtos = cartItemService.findByMemberMid(memberDto.getMid());

        return new ResponseEntity<>(cartItemDtos, HttpStatus.OK);
    }

    @PutMapping("/cart/modify/{productId}")
    public ResponseEntity<CartItemDto> putCartItem(
            @PathVariable(name = "productId") Long productId, @RequestBody CartItemDto cartItemDto) {
        log.info("rest 상품 수정 요청 {}", productId);
        log.info("rest 상품 수정 요청 {}", cartItemDto);
        ProductDto productDto = productService.getProduct(productId);
        cartItemDto.setProductDto(productDto);
        cartItemService.updateCartItem(cartItemDto);

        return new ResponseEntity<>(cartItemDto, HttpStatus.OK);
    }

    @DeleteMapping("/cart/delete/{id}")
    public ResponseEntity<String> deleteCartItem(
            @PathVariable Long id) {
        log.info("rest 상품 삭제 요청 {}", id);
        cartItemService.deleteCartItem(id);
        return ResponseEntity.ok("장바구니에서 제거되었습니다.");
    }

    @GetMapping("/cart/{productId}")
    public ResponseEntity<Boolean> getIsInCart(@PathVariable(name = "productId") Long productId) {
        log.info("rest 상품 상세 요청: {}", productId);

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        log.info("멤버 아이디", authentication);
        AuthMemberDto authMemberDto = (AuthMemberDto) authentication.getPrincipal();
        Long memberId = authMemberDto.getMemberDto().getMid();
        Boolean result = cartItemService.isInCart(memberId, productId);
        log.info("결과: {}", result);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/check-auth")
    public ResponseEntity<Boolean> checkAuth(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
