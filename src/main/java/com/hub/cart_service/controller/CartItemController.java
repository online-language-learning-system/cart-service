package com.hub.cart_service.controller;

import com.hub.cart_service.model.dto.CartItemGetDto;
import com.hub.cart_service.model.dto.CartItemPostDto;
import com.hub.cart_service.service.CartItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping("/storefront/cart/items")
    public ResponseEntity<CartItemGetDto> createNewCartItem(@Valid @RequestBody CartItemPostDto cartItemPostDto) {
        return ResponseEntity.ok(cartItemService.addCartItem(cartItemPostDto));
    }
}
