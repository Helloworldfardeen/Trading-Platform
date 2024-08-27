package com.integral.service;

import com.integral.domain.PaymentMethod;
import com.integral.model.PaymentOrder;
import com.integral.model.User;
import com.integral.responce.PaymentResponse;
import com.razorpay.RazorpayException;

public interface PaymentService {

	PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod) throws Exception;

	PaymentOrder getPaymentOrderById(Long id) throws Exception;

	Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException;

	public PaymentResponse createRazorpayPaymentLink(User user, Long amount,Long orderId) throws RazorpayException;

//	PaymentResponse createStripePaymentLing(User user, Long amount, Long orderId)throws RazorpayException;
}
