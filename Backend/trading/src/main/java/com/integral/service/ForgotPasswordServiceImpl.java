package com.integral.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integral.domain.VerificationType;
import com.integral.model.ForgotPasswordToken;
import com.integral.model.User;
import com.integral.repository.ForgotPasswordRepository;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

	@Autowired
	private ForgotPasswordRepository forgotPasswordRepository;

	@Override
	public ForgotPasswordToken createToken(User user, String id, String otp, VerificationType verificationType,
			String sendTo) {
		ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
		forgotPasswordToken.setUser(user);
		forgotPasswordToken.setId(id);
		forgotPasswordToken.setSendTo(sendTo);
		forgotPasswordToken.setOtp(otp);
		forgotPasswordToken.setVerificationType(verificationType);

		return forgotPasswordRepository.save(forgotPasswordToken);
	}

	@Override
	public ForgotPasswordToken findByid(String id) {
		Optional<ForgotPasswordToken> token = forgotPasswordRepository.findById(id);
		return token.orElse(null);
	}

	@Override
	public ForgotPasswordToken findByUser(Long userId) {

		return forgotPasswordRepository.findByUserId(userId);// change i have made
	}

	@Override
	public void deleteToken(ForgotPasswordToken token) {
		forgotPasswordRepository.delete(token);

	}

}
