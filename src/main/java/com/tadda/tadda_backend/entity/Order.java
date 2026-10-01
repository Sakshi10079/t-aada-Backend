package com.tadda.tadda_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = "brand_owner_id", nullable = false)
    private User brandOwner;

    private String customerName;

    private String customerPhone;

    @Column(columnDefinition = "TEXT")
    private String shippingAddress;

    private String city;

    private String state;

    private String pincode;

    private String product;

    private String variant;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    private LocalDateTime createdAt;
}