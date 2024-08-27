package com.integral.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integral.model.ForgotPasswordToken;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordToken, String> {
	ForgotPasswordToken findByUserId(Long userid);
}
