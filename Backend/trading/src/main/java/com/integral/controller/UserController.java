package com.integral.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.integral.domain.VerificationType;
import com.integral.model.ForgotPasswordToken;
import com.integral.model.User;
import com.integral.model.VerificationCode;
import com.integral.request.ForgotPasswordTokenRequest;
import com.integral.request.ResetPasswordRequest;
import com.integral.responce.ApiResponse;
import com.integral.responce.AuthResponce;
import com.integral.service.EmailService;
import com.integral.service.ForgotPasswordService;
import com.integral.service.UserService;
import com.integral.service.VerificationCodeService;
import com.integral.utils.OtpUtils;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private VerificationCodeService verificationCodeService;
	@Autowired
	private ForgotPasswordService forgotPasswordService;

	@GetMapping("/api/users/profile")//http://localhost:7575/api/users/profile
	public ResponseEntity<User> getUserProfileByJwt(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PostMapping("/api/users/verification/{verificationType}/send-otp")
	public ResponseEntity<String> sendVerification(@RequestHeader("Authorization") String jwt,
			@PathVariable VerificationType verificationType) {
		User user;
		try {
			user = userService.findUserProfileByJwt(jwt);
			VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
			if (verificationCode == null) {
				verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);

			}
			if (verificationType.equals(verificationType.EMAIL)) {
				emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NO_CONTENT)
					.body("Inside the Catch Block of sendVerification Method");
		}

		return new ResponseEntity<>("VerificationOTP successfully Send", HttpStatus.OK);
	}

	@PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
	public ResponseEntity<User> enableTwoFactorAuthentication(@PathVariable String otp,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user;
		try {
			user = userService.findUserProfileByJwt(jwt);

			VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

			String sendTo = verificationCode.getVerficationType().equals(VerificationType.EMAIL)
					? verificationCode.getEmail()
					: verificationCode.getMobile();
			boolean isVerified = verificationCode.getOtp().equals(otp);
			if (isVerified) {
				User updateUser = userService.enableTwoFactorAuthentication(verificationCode.getVerficationType(),
						sendTo, user);
				verificationCodeService.deleteVerificationCodeById(verificationCode);
				return new ResponseEntity<>(updateUser, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

		}

		throw new Exception("Wrong OTP");

	}
	// now for forgot Password here we Write Two Method One for sending token
	// another one for verifying the token finally we update Our Password......
	// that we did above
	// @PostMapping("/api/users/verification/{verificationType}/send-otp")
 
	
	//here is the Problem
	@PostMapping("/auth/users/reset-password/send-otp")
	public ResponseEntity<AuthResponce> sendForgotpasswordOtp(@RequestBody ForgotPasswordTokenRequest req) throws Exception {
		User user = userService.findUserByEmail(req.getSendTo());
		String otp = OtpUtils.genrateOTP();
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString();
		ForgotPasswordToken token = forgotPasswordService.findByUser(user.getId());
		if (token == null) {
			token = forgotPasswordService.createToken(user, id, otp, req.getVerificationType(), req.getSendTo());
		}
		if (req.getVerificationType().equals(VerificationType.EMAIL)) {

			emailService.sendVerificationOtpEmail(user.getEmail(), token.getOtp());

		}
		AuthResponce response = new AuthResponce();
		response.setSession(token.getId());
		response.setMessage("Password reset Otp Sent Successfully");

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PatchMapping("/auth/users/reset-password/verify-otp")
	public ResponseEntity<ApiResponse> resetPassword(@RequestParam String id, @RequestBody ResetPasswordRequest req,
			@RequestHeader("Authorization") String jwt) throws Exception {

		ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findByid(id);
		boolean isVerifies = forgotPasswordToken.getOtp().equals(req.getOtp());
		if (isVerifies) {
			userService.updatePassword(forgotPasswordToken.getUser(), req.getPassword());
			ApiResponse res = new ApiResponse();
			res.setMessage("password Update Successfullly");
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(res);
		}

		throw new Exception("wrong otp");

	}

}


/*
 * CoinGecko
Cryptocurrencies
Exchanges
NFT
Learn
Products
Candy Jar
Candy
Portfolio
 Search
/
Home 
Developer's Dashboard 
Stats
Usage Report
Label
fardeen
API Key
CG-tWXcAibH4GV6HkMJg42CzF9u
Total API Calls for this key (monthly)
0
Remaining monthly API Calls
10,000
Last Used
N/A
Monthly calls replenish on Sep 01, 2024






https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd
 * */
 