package com.integral.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integral.model.PaymentDetails;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {

	PaymentDetails findByUserId(Long userId);
	
}
