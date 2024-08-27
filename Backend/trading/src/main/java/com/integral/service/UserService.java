package com.integral.service;

import com.integral.domain.VerificationType;
import com.integral.model.User;

public interface UserService {

	public User findUserProfileByJwt(String jwt) throws Exception;

	public User findUserByEmail(String email) throws Exception;

	public User findUserById(Long userid) throws Exception;

	public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user)
			throws Exception;

	User updatePassword(User user, String newPassword) throws Exception;

}
