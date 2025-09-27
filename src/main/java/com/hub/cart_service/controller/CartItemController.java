package com.hub.cart_service.controller;

import com.hub.cart_service.grpc.CourseDetail;
import com.hub.cart_service.mapper.CourseMapper;
import com.hub.cart_service.model.dto.CartItemGetDto;
import com.hub.cart_service.model.dto.CartItemPostDto;
import com.hub.cart_service.model.dto.CourseDetailRestDto;
import com.hub.cart_service.service.CartItemService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping("/storefront/cart/view")
    public ResponseEntity<List<CourseDetailRestDto>> viewAllItemsFromCart() {
        List<CourseDetail> protoCourses = cartItemService.viewItemFromCartByUserId();
        /*
                Vấn đề của bạn: bạn trả về object Protobuf (CourseDetail)
                thay vì DTO bình thường. Jackson không hiểu hết Protobuf,
                nên nó cố serialize luôn cả field nội bộ UnknownFieldSet
                → gây ra lỗi vòng lặp vô tận.
         */
        List<CourseDetailRestDto> restCourses = protoCourses.stream()
                .map(CourseMapper::toRestDto)
                .toList();
        return ResponseEntity.ok(restCourses);
    }

    @PostMapping("/storefront/cart/items")
    public ResponseEntity<CartItemGetDto> createNewCartItem(@Valid @RequestBody CartItemPostDto cartItemPostDto) {
        return ResponseEntity.ok(cartItemService.addCartItem(cartItemPostDto));
    }

    @DeleteMapping("/storefront/cart/{courseId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable(value = "courseId") Long courseId) {
        cartItemService.removeCourseFromCart(courseId);
        return ResponseEntity.noContent().build();
    }
}
