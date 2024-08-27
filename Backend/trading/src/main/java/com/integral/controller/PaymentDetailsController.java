package com.integral.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.integral.model.PaymentDetails;
import com.integral.model.User;
import com.integral.service.PaymentDetailsService;
import com.integral.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class PaymentDetailsController {

	@Autowired
	private UserService userService;
	@Autowired
	private PaymentDetailsService paymentDetailservice;

	@PostMapping("/payment-details")
	public ResponseEntity<PaymentDetails> addPaymentDetails(@RequestHeader("Authorization") String jwt,
			@RequestBody PaymentDetails paymentsRequest) throws Exception {

		User user = userService.findUserProfileByJwt(jwt);
		PaymentDetails paymentDetails = paymentDetailservice.addPaymentDetails(paymentsRequest.getAccountNumber(),
				paymentsRequest.getId(), paymentsRequest.getAccountHolderName(), paymentsRequest.getIfsc(),
				paymentsRequest.getBankName(), user);
		return new ResponseEntity<>(paymentDetails, HttpStatus.CREATED);
	}

	@GetMapping("/payment-details")
	public ResponseEntity<PaymentDetails> getUserPaymentDetails(@RequestHeader("Authorization") String jwt)
			throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		PaymentDetails paymentDetail = paymentDetailservice.getUsersPayDetails(user);
		return new ResponseEntity<>(paymentDetail, HttpStatus.FOUND);
	}

}
