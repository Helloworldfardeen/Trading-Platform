package com.integral.service;

import com.integral.domain.VerificationType;
import com.integral.model.User;
import com.integral.model.VerificationCode;

public interface VerificationCodeService {
	VerificationCode sendVerificationCode(User user,VerificationType verificationType);
	VerificationCode getVerificationCodeById(Long id) throws Exception;
	VerificationCode getVerificationCodeByUser(Long userId);
//	Boolean
	void deleteVerificationCodeById(VerificationCode verification);
	

}
