package com.example.project.service.store;

import java.util.List;

import com.example.project.dto.store.OrderItemDto;
import com.example.project.dto.store.ProductDto;
import com.example.project.entity.store.Order;
import com.example.project.entity.store.OrderItem;
import com.example.project.entity.store.Product;

public interface OrderItemService {

        List<OrderItemDto> findByOrderId(Long orderId);

        void insertOrderItems(Long memberId, Long orderId, List<Long> checkedItems);

        void deleteOrderItems(Long orderId);

        public default OrderItemDto entityToDto(OrderItem orderItem) {
                ProductDto productDto = ProductDto.builder()
                                .id(orderItem.getProduct().getId())
                                .name(orderItem.getProduct().getName())
                                .itemDetails(orderItem.getProduct().getItemDetails())
                                .category(orderItem.getProduct().getCategory())
                                .price(orderItem.getProduct().getPrice())
                                .image(orderItem.getProduct().getImage())
                                .build();

                return OrderItemDto.builder()
                                .id(orderItem.getId())
                                .orderId(orderItem.getOrder().getId())
                                .productDto(productDto)
                                .quantity(orderItem.getQuantity())
                                .price(orderItem.getPrice())
                                .build();
        }

        public default OrderItem dtoToEntity(OrderItemDto orderItemDto) {
                Product product = Product.builder()
                                .id(orderItemDto.getProductDto().getId())
                                .build();

                return OrderItem.builder()
                                .id(orderItemDto.getId())
                                .order(Order.builder().id(orderItemDto.getOrderId()).build())
                                .product(product)
                                .quantity(orderItemDto.getQuantity())
                                .price(orderItemDto.getPrice())
                                .build();
        }

}
