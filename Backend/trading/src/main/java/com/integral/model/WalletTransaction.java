package com.integral.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.LocalDate;

import com.integral.domain.WalletTranctionType;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class WalletTransaction {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne
	private Wallet wallet;
	private WalletTranctionType type;
	private LocalDate date;
	private String tranferId;
	private String purpose;
	private Long amount;

}
