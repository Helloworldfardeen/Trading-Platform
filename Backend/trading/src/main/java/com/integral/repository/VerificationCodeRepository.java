package com.integral.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integral.model.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long>{
	public VerificationCode findByUserId(Long userId);

}
