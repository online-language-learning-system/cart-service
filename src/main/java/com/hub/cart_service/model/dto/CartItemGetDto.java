package com.hub.cart_service.model.dto;

import lombok.Builder;

@Builder
public record CartItemGetDto(
        String userId,
        Long courseId
) {
}
