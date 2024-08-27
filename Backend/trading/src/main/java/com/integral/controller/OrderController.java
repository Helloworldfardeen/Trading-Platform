package com.integral.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.integral.domain.Ordertype;
import com.integral.model.Coin;
import com.integral.model.Order;
import com.integral.model.User;
import com.integral.request.CreateOrderRequest;
import com.integral.service.CoinService;
import com.integral.service.OrderService;
import com.integral.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	@Autowired
	private UserService userService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CoinService coinService;

//	@Autowired
//	private WalletTransactionService walletTransactional;
	@PostMapping("/pay")
	public ResponseEntity<Order> payOrderPayment(@RequestHeader("Authorization") String jwt,
			@RequestBody CreateOrderRequest req) throws Exception {

		User user = userService.findUserProfileByJwt(jwt);
		Coin coin = coinService.findById(req.getCoinId());
		Order order = orderService.processOrder(coin, req.getQuality(), req.getOrderType(), jwt, user);
		return ResponseEntity.ok(order);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<Order> getOrderById(@RequestHeader("Authorization") String jwt, @PathVariable Long orderId)
			throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Order order = orderService.getOrderById(orderId);
		if (order.getUser().getId().equals(user.getId())) {
			return ResponseEntity.ok(order);
		} else {
			throw new Exception("You don't Have accesss");
		}
	}

	@GetMapping()
	public ResponseEntity<List<Order>> getAllOrderForUser(@RequestHeader("Authorization") String jwt,
			@RequestParam(required = false) Ordertype order_type, @RequestParam(required = false) String asset_symbol)
			throws Exception {

		Long userId = userService.findUserProfileByJwt(jwt).getId();
		List<Order> userOrders = orderService.getAllOrderOfUser(userId, order_type, asset_symbol);
		return ResponseEntity.ok(userOrders);
	}
//	@GetMapping

}
