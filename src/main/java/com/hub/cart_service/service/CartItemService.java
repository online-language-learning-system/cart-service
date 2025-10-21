package com.hub.cart_service.service;

import com.hub.cart_service.grpc.CourseDetail;
import com.hub.cart_service.grpc.CourseListRequest;
import com.hub.cart_service.grpc.CourseListResponse;
import com.hub.cart_service.grpcclient.CourseGrpcClient;
import com.hub.cart_service.mapper.CartItemMapper;
import com.hub.cart_service.model.CartItem;
import com.hub.cart_service.model.dto.CartItemGetDto;
import com.hub.cart_service.model.dto.CartItemPostDto;
import com.hub.cart_service.repository.CartItemRepository;
import com.hub.cart_service.utils.Constants;
import com.hub.common_library.exception.DuplicatedException;
import com.hub.common_library.exception.InternalServerErrorException;
import com.hub.common_library.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartItemService {

    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final CourseGrpcClient courseGrpcClient;

    public CartItemService(CartItemMapper cartItemMapper,
                           CartItemRepository cartItemRepository,
                           CourseGrpcClient courseGrpcClient) {
        this.cartItemMapper = cartItemMapper;
        this.cartItemRepository = cartItemRepository;
        this.courseGrpcClient = courseGrpcClient;
    }

    public List<CourseDetail> viewItemFromCartByUserId() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Long> courseIds = cartItemRepository.findCourseIdsByUserId(userId);

//        CourseListRequest request = CourseListRequest.newBuilder()
//                .addCourseId(courseId)
//                .build();

        CourseListResponse response = courseGrpcClient.getCourseDetails(courseIds);

        return response.getCoursesList();
    }


    @Transactional
    public void removeCourseFromCart(Long courseId, String currentUserId) {

        if (currentUserId == null) {
            currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        }

        CartItem cartItem = cartItemRepository.findByUserIdAndCourseId(currentUserId, courseId)
                .orElseThrow(() -> new NotFoundException(Constants.ErrorCode.COURSE_NOT_FOUND_IN_CART, courseId));

        cartItemRepository.delete(cartItem);
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
            Optional<CartItem> existingCartItem =
                    cartItemRepository.findByUserIdAndCourseId(currentUserId, cartItemPostDto.courseId());

            if (existingCartItem.isPresent()) {
                throw new DuplicatedException(Constants.ErrorCode.COURSE_ALREADY_IN_CART);
            }

            return createNewCartItem(cartItemPostDto, currentUserId);

        } catch (PessimisticLockingFailureException pessimisticLockingFailureException) {
            log.error("CART_ITEM_SERVICE: Failed to acquire lock for adding cart item",
                    pessimisticLockingFailureException);
            throw new InternalServerErrorException(Constants.ErrorCode.ADD_CART_ITEM_FAILED);
        }
    }

    private CartItem createNewCartItem(CartItemPostDto cartItemPostDto, String currentUserId) {
        CartItem cartItem = new CartItem(currentUserId, cartItemPostDto.courseId());
        return cartItemRepository.save(cartItem);
    }

}
