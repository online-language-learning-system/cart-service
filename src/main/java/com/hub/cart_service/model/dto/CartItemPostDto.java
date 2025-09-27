package com.hub.cart_service.model.dto;

import jakarta.validation.constraints.NotNull;

public record CartItemPostDto(
        @NotNull
        Long courseId
) {
}
