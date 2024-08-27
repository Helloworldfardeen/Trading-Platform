package com.integral.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integral.model.TwoFactorOTP;

public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOTP, String> {
	TwoFactorOTP findByUserId(Long userId); 
}
