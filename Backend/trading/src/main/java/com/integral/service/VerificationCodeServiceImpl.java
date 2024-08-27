package com.integral.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integral.domain.VerificationType;
import com.integral.model.User;
import com.integral.model.VerificationCode;
import com.integral.repository.VerificationCodeRepository;
import com.integral.utils.OtpUtils;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

	@Autowired
	private VerificationCodeRepository verificationCodeRepository;

	@Override
	public VerificationCode sendVerificationCode(User user, VerificationType verificationType) {
		VerificationCode verificationCodeLocal = new VerificationCode();
		verificationCodeLocal.setOtp(OtpUtils.genrateOTP());
		verificationCodeLocal.setVerficationType(verificationType);
		verificationCodeLocal.setUser(user);
		return verificationCodeRepository.save(verificationCodeLocal);
	}

	@Override
	public VerificationCode getVerificationCodeById(Long id) throws Exception {
		Optional<VerificationCode> verificationCode = verificationCodeRepository.findById(id);
		if (verificationCode.isPresent()) {
			return verificationCode.get();
		}
		throw new Exception("Verification Code Not Found");
	}

	@Override
	public VerificationCode getVerificationCodeByUser(Long userId) {
		return verificationCodeRepository.findByUserId(userId);
	}

	@Override
	public void deleteVerificationCodeById(VerificationCode verification) {
       verificationCodeRepository.delete(verification);
	}

}
