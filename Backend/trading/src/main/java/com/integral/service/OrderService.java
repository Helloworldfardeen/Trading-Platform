package com.integral.service;

import java.util.List;

import com.integral.domain.Ordertype;
import com.integral.model.Coin;
import com.integral.model.Order;
import com.integral.model.OrderItem;
import com.integral.model.User;

public interface OrderService {
	Order createOrder(User user, OrderItem orderItem, Ordertype orderType);

	Order getOrderById(Long orderId) throws Exception;

	List<Order> getAllOrderOfUser(Long userId, Ordertype orderType, String asset_symbol);// i have made

	Order processOrder(Coin coin, double quality, Ordertype ordertype, String assetSymbol,User user) throws Exception;

	OrderItem createOrderItem(Coin coin,double quality,double buyPrice,double sellPrice);
}
