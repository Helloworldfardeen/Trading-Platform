package com.integral.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.integral.domain.PaymentMethod;
import com.integral.model.PaymentOrder;
import com.integral.model.User;
import com.integral.responce.PaymentResponse;
import com.integral.service.PaymentService;
import com.integral.service.UserService;
import com.razorpay.RazorpayException;

@RestController
//@RequestMapping("/api")
public class PaymentController {
	@Autowired
	private UserService userService;
	@Autowired
	private PaymentService paymentService;

	@PostMapping("/api/payment/{paymentMethod}/amount/{amount}")//http://localhost:7575/api/payment/RAZORPAY/amount/1000
	public ResponseEntity<PaymentResponse> paymenthandler(@PathVariable PaymentMethod paymentMethod,
			@PathVariable Long amount, @RequestHeader("Authorization") String jwt) throws Exception, RazorpayException {
		User user = userService.findUserProfileByJwt(jwt);
		PaymentResponse paymentResponse = null;
		PaymentOrder order = paymentService.createOrder(user, amount, paymentMethod);
		if (paymentMethod.equals(paymentMethod.RAZORPAY)) {
			paymentResponse = paymentService.createRazorpayPaymentLink(user, amount,order.getId());
		}
		return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
	}

}
