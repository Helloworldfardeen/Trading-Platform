package com.integral.request;

import com.integral.domain.VerificationType;

import lombok.Data;

@Data
public class ForgotPasswordTokenRequest {
	private String sendTo;
	private VerificationType verificationType;

}
