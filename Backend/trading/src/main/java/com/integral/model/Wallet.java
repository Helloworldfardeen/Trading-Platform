package com.integral.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Wallet {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@OneToOne
	private User user;
	private BigDecimal balance=BigDecimal.ZERO;
}
