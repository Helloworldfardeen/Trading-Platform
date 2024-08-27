package com.integral.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.integral.domain.OrderStatus;
import com.integral.domain.Ordertype;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="orders")
public class Order {//order is reserved keyword in mysql
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne
	private User user;
	@Column(nullable = false)
	private Ordertype orderType;
	@Column(nullable = false)
	private BigDecimal price;
	private LocalDateTime timestapm = LocalDateTime.now();
	@Column(nullable = false)
	private OrderStatus status;
	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
	private OrderItem orderItem;

}
