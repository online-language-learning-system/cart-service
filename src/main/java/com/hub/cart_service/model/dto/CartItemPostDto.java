package com.hub.cart_service.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CartItemPostDto(

        @NotNull
        Long courseId,

        @NotNull
        @Min(value = 1, message = "The quantity must be greater than one")
        int quantity
) {
}
