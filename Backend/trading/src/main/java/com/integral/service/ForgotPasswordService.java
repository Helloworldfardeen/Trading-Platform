package com.integral.service;

import com.integral.domain.VerificationType;
import com.integral.model.ForgotPasswordToken;
import com.integral.model.User;

public interface ForgotPasswordService {
	ForgotPasswordToken createToken(User user, String id, String otp, VerificationType verificationType, String sendTo);

	ForgotPasswordToken findByid(String id);

	ForgotPasswordToken findByUser(Long userId);//previously it wass int so chnged

	void deleteToken(ForgotPasswordToken token);
}
