package com.integral.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integral.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByuserId(Long userId);

}
