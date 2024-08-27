package com.integral.model;

import com.integral.domain.PaymentMethod;
import com.integral.domain.PaymentOrderStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class PaymentOrder {
 
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long amount;
	private PaymentOrderStatus status;
	private PaymentMethod paymentMethod;
	@ManyToOne
	private User user;
	
}
