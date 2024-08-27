package com.integral.service;

import com.integral.model.PaymentDetails;
import com.integral.model.User;

public interface PaymentDetailsService {

	public PaymentDetails addPaymentDetails(String accountNumber, Long id, String accountHolderName, String ifsc,
			String bankName, User user);

	public PaymentDetails getUsersPayDetails(User user);
}
