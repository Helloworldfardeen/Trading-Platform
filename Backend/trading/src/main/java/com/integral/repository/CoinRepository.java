package com.integral.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.integral.model.Coin;

public interface CoinRepository extends JpaRepository<Coin, String> {

}
