package com.integral.model;

import com.integral.domain.VerificationType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class ForgotPasswordToken {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;
	@OneToOne // to define for which user this forgot password
	private User user;
	private VerificationType verificationType;// like mobile or email where do you want a otp
	private String sendTo;
	private String otp;

}
