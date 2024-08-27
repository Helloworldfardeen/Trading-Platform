package com.integral.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integral.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
