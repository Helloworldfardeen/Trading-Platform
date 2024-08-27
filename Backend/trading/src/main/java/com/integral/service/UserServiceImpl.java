package com.integral.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integral.config.JwtProvider;
import com.integral.domain.VerificationType;
import com.integral.model.TwoFactorAuth;
import com.integral.model.User;
import com.integral.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User findUserProfileByJwt(String jwt) throws Exception {
		String email = JwtProvider.getEmailFromToken(jwt);
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new Exception("user not Found");
		}

		return user;
	}

	@Override
	public User findUserByEmail(String email) throws Exception {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new Exception("user not Found");
		}

		return user;
	}

	@Override
	public User findUserById(Long userid) throws Exception {

		Optional<User> user = userRepository.findById(userid);
		if (user.isEmpty()) {
			throw new Exception("USER NOT FOUND");
		}
		return user.get();
	}

	// imp
	@Override
	public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user)
			throws Exception {
		TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
		twoFactorAuth.setEnable(true);
		twoFactorAuth.setSendTo(verificationType);
		user.setTwoFactorAuth(twoFactorAuth);

		return userRepository.save(user);
	}

	@Override
	public User updatePassword(User user, String newPassword) throws Exception {
		user.setPassword(newPassword);
		return userRepository.save(user);

	}

}
