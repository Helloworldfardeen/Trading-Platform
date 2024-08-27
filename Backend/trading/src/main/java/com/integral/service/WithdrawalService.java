package com.integral.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.integral.model.User;
import com.integral.model.Withdrawal;


public interface WithdrawalService {
	Withdrawal requestWithdrawal(Long amount, User user);
	Withdrawal procedWithWithdrawal(Long withdrawalId,boolean accept) throws Exception;
	List<Withdrawal> getUsersWithdrawalHistory(User user);
	List<Withdrawal> getAllWithdrawalRequest();
	
}
