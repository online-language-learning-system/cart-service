package com.hub.cart_service.model.dto;

import java.util.List;

public record CartItemListGetDto(
        String userId,
        List<Long> courseId
) {
}
