package com.integral.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integral.domain.WithdrawalStatus;
import com.integral.model.User;
import com.integral.model.Withdrawal;
import com.integral.repository.WithdrawalRepository;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {

	@Autowired
	private WithdrawalRepository withdrawalRepository;

	@Override
	public Withdrawal requestWithdrawal(Long amount, User user) {
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setAmount(amount);
		withdrawal.setUser(user);
		withdrawal.setStatus(WithdrawalStatus.PENDING);
		return withdrawalRepository.save(withdrawal);
	}

	@Override
	public Withdrawal procedWithWithdrawal(Long withdrawalId, boolean accept) throws Exception {
		Optional<Withdrawal> withdrawal = withdrawalRepository.findById(withdrawalId);
		if (withdrawal.isEmpty()) {
			throw new Exception("withdrawal not found");
		}
		Withdrawal withdrawal2 = withdrawal.get();
		withdrawal2.setDate(LocalDateTime.now());
		if (accept) {
			withdrawal2.setStatus(WithdrawalStatus.SUCCESS);
		} else {
			withdrawal2.setStatus(WithdrawalStatus.PENDING);
		}
		return withdrawalRepository.save(withdrawal2);
	}

	@Override
	public List<Withdrawal> getUsersWithdrawalHistory(User user) {

		return withdrawalRepository.findByUserId(user.getId());
	}

	@Override
	public List<Withdrawal> getAllWithdrawalRequest() {

		return withdrawalRepository.findAll();
	}
}
