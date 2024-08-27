package com.integral.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integral.model.TwoFactorOTP;
import com.integral.model.User;
import com.integral.repository.TwoFactorOtpRepository;

@Service
public class TwoFactorOtpServiceImpl implements TwoFactorOtpService {

	@Autowired
	private TwoFactorOtpRepository twoFactorOtpRepository;

	@Override
	public TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt) {
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString();
		TwoFactorOTP twoFactorOTP = new TwoFactorOTP();
		twoFactorOTP.setId(id);
		twoFactorOTP.setJwt(jwt);
		twoFactorOTP.setOtp(otp);
		twoFactorOTP.setUser(user);

		return twoFactorOtpRepository.save(twoFactorOTP);
	}

	@Override
	public TwoFactorOTP findbyUser(Long userId) {
		return twoFactorOtpRepository.findByUserId(userId);
	}

	@Override
	public TwoFactorOTP findbyId( String id) {
		Optional<TwoFactorOTP> opt = twoFactorOtpRepository.findById(id);
		return opt.orElse(null);
	}

	@Override
	public boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp, String otp) {

		return twoFactorOtp.getOtp().equals(otp);
	}

	@Override
	public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp) {
		twoFactorOtpRepository.delete(twoFactorOtp);
	}

}
