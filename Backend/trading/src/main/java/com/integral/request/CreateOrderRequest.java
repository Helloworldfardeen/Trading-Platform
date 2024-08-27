package com.integral.request;

import com.integral.domain.Ordertype;

import lombok.Data;

@Data
public class CreateOrderRequest {
	private String coinId;
	private double quality;
	private Ordertype orderType;

}
