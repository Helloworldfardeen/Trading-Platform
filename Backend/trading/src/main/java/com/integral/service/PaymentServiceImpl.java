package com.integral.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.integral.domain.PaymentMethod;
import com.integral.domain.PaymentOrderStatus;
import com.integral.model.PaymentOrder;
import com.integral.model.User;
import com.integral.repository.PaymentRepository;
import com.integral.responce.PaymentResponse;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.param.billingportal.SessionCreateParams;

@Service
public class PaymentServiceImpl implements PaymentService {
	@Autowired
	private PaymentRepository paymentOrderRepository;

//	@Value("${stripe.api.key}")
//	private String stripeSecretKey;
	@Value("${razorpay.api.key}")
	private String apiKey;
	@Value("${razorpay.api.secret}")
	private String apiSecretKey;

	@Override
	public PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod) throws Exception {

		PaymentOrder payoder = new PaymentOrder();
		payoder.setUser(user);
		payoder.setAmount(amount);
		payoder.setPaymentMethod(paymentMethod);
		payoder.setStatus(PaymentOrderStatus.PENDING);
		return paymentOrderRepository.save(payoder);
	}

	@Override
	public PaymentOrder getPaymentOrderById(Long id) throws Exception {

		return paymentOrderRepository.findById(id).orElseThrow(() -> new Exception("payment order Not Found"));
	}

	// imp for using razor pay
	@Override
	public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {
	   if(paymentOrder.getStatus()==null)
	   {
		   paymentOrder.setStatus(PaymentOrderStatus.PENDING);
	   }
		if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
	        if (paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)) {
	            RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecretKey);
	            Payment payment = razorpay.payments.fetch(paymentId);
	            String status = payment.get("status");

	            if (status.equals("captured")) {  // Ensure the status is 'captured'
	                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
	                paymentOrderRepository.save(paymentOrder);  // Save the successful payment order
	                return true;
	            } else {
	                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
	                paymentOrderRepository.save(paymentOrder);  // Save the failed payment order
	                return false;
	            }
	        }
	        paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
	        paymentOrderRepository.save(paymentOrder);  // Save the payment order for other payment methods
	        return true;
	    }
	    return false;  // Return false if the payment order is not in PENDING status
	}


	
	@Override
	public PaymentResponse createRazorpayPaymentLink(User user, Long amount,Long orderId) throws RazorpayException {
	    Long Amount = amount * 100;  // Convert amount to paise (smallest unit of INR)
	    try {
	        RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecretKey);

	        JSONObject paymentLinkRequest = new JSONObject();
	        paymentLinkRequest.put("amount", Amount);
	        paymentLinkRequest.put("currency", "INR");

	        JSONObject customer = new JSONObject();
	        customer.put("name", user.getFullName());
	        customer.put("email", user.getEmail());
	        paymentLinkRequest.put("customer", customer);

	        JSONObject notify = new JSONObject();
	        notify.put("email", true);
	        paymentLinkRequest.put("notify", notify);

	        paymentLinkRequest.put("reminder_enable", true);
	        paymentLinkRequest.put("callback_url", "http://localhost:7575/wallet?order_id"+orderId);
	        paymentLinkRequest.put("callback_method", "get");

	        PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);

	        PaymentResponse response = new PaymentResponse();
	        response.setPayment_url(payment.get("short_url"));
	        return response;
	    } catch (RazorpayException e) {
	        System.out.println("Error Creating payment link: " + e.getMessage());
	        throw new RazorpayException(e.getMessage());
	    }
	}

//	@Override
//	public PaymentResponse createStripePaymentLing(User user, Long amount, Long orderId)throws RazorpayException {
//	Stripe.apiKey =stripeSecretKey;
//	SessionCreateParams params =SessionCreateParams.builder().addPaymentMethodTyoe(SessionCreateParams.PaymentMethodType.CARD).setMode(SessionCreateParams.Mode>PAYMENT).setSuccessUrl("http://localhost:5173/wallet?order_id="+orderId).setCancelUrl("http://localhost:5173/payment/cancel").assLineItem(SessionCreateParams.LineItem.builder().setQuality(1L).setPriceData(SessionCreateParams.LineItem.PriceData.builder().setCurrency))
//	
//	}

}
