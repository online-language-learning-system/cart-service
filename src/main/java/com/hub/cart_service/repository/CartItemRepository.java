package com.hub.cart_service.repository;

import com.hub.cart_service.model.CartItem;
import com.hub.cart_service.model.CartItemId;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository
        extends JpaRepository<CartItem, CartItemId> {

    @Query("SELECT cart.courseId FROM CartItem cart " +
            "WHERE cart.userId = :userId")
    List<Long> findCourseIdsByUserId(@Param("userId") String userId);

    /*
        Pessimistic Write Lock is a mechanism to lock data
        as soon as a transaction execute this query (reads or prepares to write),
        to prevent other transactions from changing it at the same time.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT cartItem FROM CartItem cartItem " +
            "WHERE cartItem.userId = :userId " +
            "AND cartItem.courseId = :courseId")
    Optional<CartItem> findByUserIdAndCourseId(@Param(value = "userId") String userId,
                                               @Param(value = "courseId") Long courseId);

}
