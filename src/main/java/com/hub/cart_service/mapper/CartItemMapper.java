package com.hub.cart_service.mapper;

import com.hub.cart_service.model.CartItem;
import com.hub.cart_service.model.dto.CartItemGetDto;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {
    public CartItemGetDto toGetDto(CartItem cartItem) {
        return CartItemGetDto
                .builder()
                .userId(cartItem.getUserId())
                .courseId(cartItem.getCourseId())
                .build();
    }
}
