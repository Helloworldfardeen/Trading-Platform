package com.integral.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integral.model.PaymentOrder;

public interface PaymentRepository extends JpaRepository<PaymentOrder, Long> {

}
