package com.integral.responce;

import lombok.Data;

@Data
public class AuthResponce {
	private String jwt;
	private boolean status;
	private String message;
	private boolean isTwoFactorAuthEnable;
	private String session;

}
