package com.hub.cart_service.model;

import com.hub.common_library.model.AbstractAuditEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_item")
@IdClass(CartItemId.class)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem extends AbstractAuditEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @Column(name = "course_id")
    private Long courseId;

}
