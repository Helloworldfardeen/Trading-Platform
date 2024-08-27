package com.integral.service;

import com.integral.model.TwoFactorOTP;
import com.integral.model.User;

public interface TwoFactorOtpService {
	TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt);

	TwoFactorOTP findbyUser(Long userId);

	TwoFactorOTP findbyId(String id);

	boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp, String otp);

	void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp);
}
