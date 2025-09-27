package com.hub.cart_service.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CartItemId {
    private String userId;
    private Long courseId;
}

/*
    equals(Object o) -- compares two objects based on fields.
    hashCode() -- generates a hash code based on fields.
 */