package com.integral.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integral.model.PaymentDetails;
import com.integral.model.User;
import com.integral.repository.PaymentDetailsRepository;

@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService {

	@Autowired
	private PaymentDetailsRepository paymentDetailRepository;

	@Override
	public PaymentDetails addPaymentDetails(String accountNumber, Long id, String accountHolderName, String ifsc,
			String bankName, User user) {
		PaymentDetails paymentDetails = new PaymentDetails();
		paymentDetails.setAccountHolderName(accountHolderName);
		paymentDetails.setBankName(bankName);
		paymentDetails.setUser(user);
		paymentDetails.setIfsc(ifsc);
		paymentDetails.setAccountNumber(accountNumber);
		return paymentDetailRepository.save(paymentDetails);
	}

	@Override
	public PaymentDetails getUsersPayDetails(User user) {

		return paymentDetailRepository.findByUserId(user.getId());
	}

}
