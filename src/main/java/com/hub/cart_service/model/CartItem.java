package com.hub.cart_service.model;

import com.hub.common_library.model.AbstractAuditEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
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
    private String userId;

    @Id
    private Long courseId;

    private int quantity;
}
