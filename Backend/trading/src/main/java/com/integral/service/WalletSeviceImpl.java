package com.integral.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integral.domain.Ordertype;
import com.integral.model.Order;
import com.integral.model.User;
import com.integral.model.Wallet;
import com.integral.repository.WalletRepository;

@Service
public class WalletSeviceImpl implements WalletService {

	@Autowired
	private WalletRepository walletRepository;

	@Override
	public Wallet getUserWallet(User user) {
		Wallet wallet = walletRepository.findByUserId(user.getId());
		if (wallet == null) {
			wallet = new Wallet();
			wallet.setUser(user);
			walletRepository.save(wallet);
		}
		return wallet;
	}

	@Override
	public Wallet addBalance(Wallet wallet, Long money) {
		BigDecimal balance = wallet.getBalance();
		BigDecimal newBalance = balance.add(BigDecimal.valueOf(money));

		wallet.setBalance(newBalance);
		return walletRepository.save(wallet);
	}

	@Override
	public Wallet findWalletById(Long id) throws Exception {
		Optional<Wallet> wallet = walletRepository.findById(id);
		if (wallet.isPresent()) {
			return wallet.get();
		}

		throw new Exception("wallet not Found");
	}

	@Override
	public Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws Exception {
		Wallet senderWallter = getUserWallet(sender);
		if (senderWallter.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
			throw new Exception("Insuffient balance...");
		}
		BigDecimal receiveBalance = receiverWallet.getBalance().subtract(BigDecimal.valueOf(amount));
		walletRepository.save(senderWallter);

		BigDecimal receiverBalance = receiverWallet.getBalance().add(BigDecimal.valueOf(amount));
		receiverWallet.setBalance(receiverBalance);
		walletRepository.save(receiverWallet);
		return senderWallter;
	}

	@Override
	public Wallet payOrderPayment(Order order, User user) throws Exception {
		Wallet wallet = getUserWallet(user);
		if (order.getOrderType().equals(Ordertype.BUY)) {
			BigDecimal newBalnce = wallet.getBalance().subtract(order.getPrice());
			if (newBalnce.compareTo(order.getPrice()) < 0) {
				throw new Exception("Insufficient funds for this Transaction");
			}
			wallet.setBalance(newBalnce);
		} else {
			BigDecimal newBalance = wallet.getBalance().add(order.getPrice());
			wallet.setBalance(newBalance);
		}
		walletRepository.save(wallet);
		return wallet;
	}

}
