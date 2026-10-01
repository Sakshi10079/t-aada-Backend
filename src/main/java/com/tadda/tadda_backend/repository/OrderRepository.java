package com.tadda.tadda_backend.repository;

import com.tadda.tadda_backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
