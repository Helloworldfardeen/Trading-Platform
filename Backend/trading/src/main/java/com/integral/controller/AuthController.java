package com.integral.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.integral.config.JwtProvider;
import com.integral.model.TwoFactorOTP;
import com.integral.model.User;
import com.integral.repository.UserRepository;
import com.integral.responce.AuthResponce;
import com.integral.service.CustomUserDetailsService;
import com.integral.service.EmailService;
import com.integral.service.TwoFactorOtpService;
import com.integral.service.TwoFactorOtpServiceImpl;
import com.integral.service.WatchListService;
import com.integral.utils.OtpUtils;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CustomUserDetailsService customUserDetailService;
	@Autowired
	private TwoFactorOtpService twoFactorOtpService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private WatchListService watchlistService;

	@PostMapping("/signup")//http://localhost:7575/auth/signup
	public ResponseEntity<AuthResponce> register(@RequestBody User user) throws Exception {
		User isEmailExist = userRepository.findByEmail(user.getEmail());
		if (isEmailExist != null) {
			throw new Exception("Email is Already Exists. Try Another");
		}
		User newUser = new User();
		newUser.setEmail(user.getEmail());
		newUser.setPassword(user.getPassword());
		newUser.setFullName(user.getFullName());
		User savedUser = userRepository.save(newUser);
		watchlistService.createWatchList(savedUser);

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user.getEmail(),
				user.getPassword());
		SecurityContextHolder.getContext().setAuthentication(auth);

		String jwt = JwtProvider.generateToken(auth);
		AuthResponce responce = new AuthResponce();
		responce.setJwt(jwt);
		responce.setStatus(true);
		responce.setMessage("Register Success");
		return new ResponseEntity<>(responce, HttpStatus.CREATED);
	}

	@PostMapping("/signin")//http://localhost:7575/auth/signin
	public ResponseEntity<AuthResponce> login(@RequestBody User user) throws Exception {
		String username = user.getEmail();
		String password = user.getPassword();
		Authentication auth = authenticate(username, password);
		SecurityContextHolder.getContext().setAuthentication(auth);

		String jwt = JwtProvider.generateToken(auth);
		User authuser = userRepository.findByEmail(username);
		if (user.getTwoFactorAuth().isEnable()) {
			AuthResponce res = new AuthResponce();
			res.setMessage("Two Factor AuthIs Enable");
			res.setTwoFactorAuthEnable(true);
			String otp = OtpUtils.genrateOTP();

			TwoFactorOTP oldTwoFactorOtp = twoFactorOtpService.findbyUser(authuser.getId());
			if (oldTwoFactorOtp != null) {
				twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOtp);
			}
			TwoFactorOTP newTwoFactorOTP = twoFactorOtpService.createTwoFactorOtp(authuser, otp, jwt);
			emailService.sendVerificationOtpEmail(username, otp);

			res.setSession(newTwoFactorOTP.getId());
			return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
		}
		AuthResponce responce = new AuthResponce();
		responce.setJwt(jwt);
		responce.setStatus(true);
		responce.setMessage("Login Success");
		return new ResponseEntity<>(responce, HttpStatus.ACCEPTED);
	}

	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
		if (userDetails == null) {
			throw new BadCredentialsException("Invalid username");
		}
		if (!password.equals(userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid Password");
		}
		return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
	}

	@GetMapping("/two-factor/otp/{otp}")
	public ResponseEntity<AuthResponce> verifySignInOtp(@PathVariable String otp, @RequestParam String id) {
		TwoFactorOTP twofactorOtp = twoFactorOtpService.findbyId(id);

		try {

			if (twoFactorOtpService.verifyTwoFactorOtp(twofactorOtp, otp)) {
				AuthResponce res = new AuthResponce();
				res.setMessage("Two factor Authentication verified");
				res.setTwoFactorAuthEnable(true);
				res.setJwt(twofactorOtp.getJwt());
				return new ResponseEntity<>(res, HttpStatus.OK);

			}
		} catch (Exception e) {
			new Exception("Invalid Otp");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

		}
		return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
	}
}
