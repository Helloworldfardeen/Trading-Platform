package com.integral.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.integral.model.Order;
import com.integral.model.PaymentOrder;
import com.integral.model.User;
import com.integral.model.Wallet;
import com.integral.model.WalletTransaction;
import com.integral.responce.PaymentResponse;
import com.integral.service.OrderService;
import com.integral.service.PaymentService;
import com.integral.service.UserService;
import com.integral.service.WalletService;

@RestController
//@RequestMapping("api/wallet")// SEEE
public class WalletController {

	@Autowired
	private WalletService walletService;
	@Autowired
	private UserService userService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private PaymentService paymentService;

	@GetMapping("/api/wallet")
	public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception {

		User user = userService.findUserProfileByJwt(jwt);
		Wallet wallet = walletService.getUserWallet(user);
		return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
	}

	@PutMapping("/api/wallet/{walletId}/tranfer")//http://localhost:7575/api/wallet/2/tranfer
	public ResponseEntity<Wallet> walletToWalletTranfer(@RequestHeader("Authorization") String jwt,
			@PathVariable Long walletId, @RequestBody WalletTransaction req) throws Exception {
		User sendUser = userService.findUserProfileByJwt(jwt);
		Wallet receiverWallet = walletService.findWalletById(walletId);
		Wallet wallet = walletService.walletToWalletTransfer(sendUser, receiverWallet, req.getAmount());

		return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
	}

	@PutMapping("/api/wallet/order/{orderId}/pay")
	public ResponseEntity<Wallet> payOrderPayment(@RequestHeader("Authorization") String jwt,
			@PathVariable Long orderId) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Order order = orderService.getOrderById(orderId);
		Wallet wallet = walletService.payOrderPayment(order, user);
		return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
	}

	@PutMapping("/api/wallet/deposit")
	public ResponseEntity<Wallet> addBalanceToWallet(@RequestHeader("Authorization") String jwt,
			@RequestParam(name = "order_id") Long Orderid, @RequestParam(name = "payment_id") String paymentId)
			throws Exception {
		User user = userService.findUserProfileByJwt(jwt);

		Wallet wallet = walletService.getUserWallet(user);
		PaymentOrder order = paymentService.getPaymentOrderById(Orderid);
		Boolean status = paymentService.proceedPaymentOrder(order, paymentId);
		if(wallet.getBalance()==null)
		{
			wallet.setBalance(BigDecimal.valueOf(0));
		}
		if (status) {
			wallet = walletService.addBalance(wallet, order.getAmount());
		}
		return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
	}
}
