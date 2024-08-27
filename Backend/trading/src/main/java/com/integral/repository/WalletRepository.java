package com.integral.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integral.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

	Wallet findByUserId(Long userId);
}
