package com.example.project.dto.store;

import java.time.LocalDateTime;
import java.util.List;

import com.example.project.entity.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDto {
    private Long id;

    private Long memberId;

    private Long totalPrice;

    private OrderStatus status;

    private List<OrderItemDto> orderItemDtos;

    private LocalDateTime updateDate;

}
