package com.hub.cart_service.service;

import com.hub.cart_service.mapper.CartItemMapper;
import com.hub.cart_service.model.CartItem;
import com.hub.cart_service.model.dto.CartItemGetDto;
import com.hub.cart_service.model.dto.CartItemPostDto;
import com.hub.cart_service.repository.CartItemRepository;
import com.hub.cart_service.utils.Constants;
import com.hub.common_library.exception.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CartItemService {

    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemMapper cartItemMapper,
                           CartItemRepository cartItemRepository) {
        this.cartItemMapper = cartItemMapper;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public CartItemGetDto addCartItem(CartItemPostDto cartItemPostDto) {

        // Extract current user id
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        CartItem cartItem = performAddCartItem(cartItemPostDto, currentUserId);

        return cartItemMapper.toGetDto(cartItem);
    }

    private CartItem performAddCartItem(CartItemPostDto cartItemPostDto, String currentUserId) {
        try {
            return cartItemRepository.findByUserIdAndCourseId(currentUserId, cartItemPostDto.courseId())
                    .map(existingCartItem -> updateExistingCartItem(cartItemPostDto, existingCartItem))
                    .orElseGet(() -> createNewCartItem(cartItemPostDto, currentUserId));

        } catch (PessimisticLockingFailureException pessimisticLockingFailureException) {
            log.error("CART_ITEM_SERVICE: Failed to acquire lock for adding cart item",
                    pessimisticLockingFailureException);
            throw new InternalServerErrorException(Constants.ErrorCode.ADD_CART_ITEM_FAILED);
        }
    }

    private CartItem createNewCartItem(CartItemPostDto cartItemPostDto, String currentUserId) {
        CartItem cartItem = new CartItem(currentUserId, cartItemPostDto.courseId(), cartItemPostDto.quantity());
        return cartItemRepository.save(cartItem);
    }

    private CartItem updateExistingCartItem(CartItemPostDto cartItemPostDto, CartItem existingCartItem) {
        existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemPostDto.quantity());
        return cartItemRepository.save(existingCartItem);
    }
}
