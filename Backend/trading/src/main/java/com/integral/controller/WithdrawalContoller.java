package com.integral.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.integral.model.User;
import com.integral.model.Wallet;
import com.integral.model.Withdrawal;
import com.integral.service.UserService;
import com.integral.service.WalletService;
import com.integral.service.WithdrawalService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
//@RequestMapping("/api/withdrawal")
public class WithdrawalContoller {

	@Autowired
	private WalletService walletService;
	@Autowired
	private WithdrawalService withdrawalSerice;
	@Autowired
	private UserService userService;;

	@PostMapping("/api/withdrawal/{amount}")
	public ResponseEntity<?> withdrawalRequest(@RequestHeader("Authorization") String jwt, @PathVariable Long amount)
			throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Wallet userWallet = walletService.getUserWallet(user);
		Withdrawal withdrawal = withdrawalSerice.requestWithdrawal(amount, user);
		walletService.addBalance(userWallet, -withdrawal.getAmount());
		return new ResponseEntity<>(withdrawal, HttpStatus.OK);
	}

	@PatchMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
	public ResponseEntity<?> processWithdrawal(@PathVariable Long id, @PathVariable boolean accept,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Withdrawal withdrawl = withdrawalSerice.procedWithWithdrawal(id, accept);
		Wallet userwallet = walletService.getUserWallet(user);
		if (!accept) {
			walletService.addBalance(userwallet, withdrawl.getAmount());
		}
		return new ResponseEntity<>(withdrawl, HttpStatus.OK);

	}

	@GetMapping("/api/withdrawal")
	public ResponseEntity<List<Withdrawal>> getWithdrawalhistory(@RequestHeader("Authorization") String jwt)
			throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		List<Withdrawal> withdrawal = withdrawalSerice.getUsersWithdrawalHistory(user);
		return new ResponseEntity<>(withdrawal, HttpStatus.OK);

	}

	@GetMapping("/api/admin/withdrawal")
	public ResponseEntity<List<Withdrawal>> getAllWitdrawalRequet(@RequestHeader("Authorization") String jwt)
			throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		List<Withdrawal> withdrawal = withdrawalSerice.getAllWithdrawalRequest();
		return new ResponseEntity<>(withdrawal, HttpStatus.OK);

	}
}