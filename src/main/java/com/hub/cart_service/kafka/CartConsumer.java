package com.hub.cart_service.kafka;

import com.hub.cart_service.kafka.event.EnrollmentCompletedEvent;
import com.hub.cart_service.service.CartItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartConsumer {

    private final CartItemService cartItemService;

    @Bean
    public Consumer<EnrollmentCompletedEvent> removeCart() {
        return enrollmentCompletedEvent -> {
            List<Long> courseIds = enrollmentCompletedEvent.courseIds();
            String currentStudentId = enrollmentCompletedEvent.studentId();

            courseIds.forEach(
                courseId -> {
                    cartItemService.removeCourseFromCart(courseId, currentStudentId);
                    log.info("The course with id {} has been removed from cart", courseId);
                }
            );
        };
    }

}
