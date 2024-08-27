package com.integral.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.integral.domain.OrderStatus;
import com.integral.domain.Ordertype;
import com.integral.model.Asset;
import com.integral.model.Coin;
import com.integral.model.Order;
import com.integral.model.OrderItem;
import com.integral.model.User;
import com.integral.repository.OrderItemRepository;
import com.integral.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private WalletService walletService;
	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private AssetService assetService;

	@Override
	public Order createOrder(User user, OrderItem orderItem, Ordertype orderType) {
		double price = orderItem.getCoin().getCurrentPrice() * orderItem.getQuality();
		Order order = new Order();
		order.setUser(user);
		order.setOrderItem(orderItem);
		order.setOrderType(orderType);
		order.setPrice(BigDecimal.valueOf(price));
		order.setTimestapm(LocalDateTime.now());
		order.setStatus(OrderStatus.PENDING);
		return orderRepository.save(order);

	}

	@Override
	public Order getOrderById(Long orderId) throws Exception {

		return orderRepository.findById(orderId).orElseThrow(() -> new Exception("order Not Found"));
	}

	@Override
	public List<Order> getAllOrderOfUser(Long userId, Ordertype orderType, String asset_symbol) {
		return orderRepository.findByuserId(userId);
	}

	@Override
	public OrderItem createOrderItem(Coin coin, double quality, double buyPrice, double sellPrice) {
		OrderItem orderItem = new OrderItem();
		orderItem.setCoin(coin);
		orderItem.setQuality(quality);
		orderItem.setBuyPrice(buyPrice);
		orderItem.setSellPrice(sellPrice);
		return orderItemRepository.save(orderItem);
	}

	@Transactional // if all the Stage reach then only it will process Further.....mean
	public Order buyAsset(Coin coin, double quality, User user) throws Exception {
		if (quality <= 0) {
			throw new Exception("quality should be >(0)");
		}
		double buyPrice = coin.getCurrentPrice();
		OrderItem orderItem = createOrderItem(coin, quality, buyPrice, 0);
		Order order = createOrder(user, orderItem, Ordertype.BUY);
		orderItem.setOrder(order);
		// if 78 number line execute then only it will create order in our
		// database other
		// wise all above go to intiall Stage mean no order will place
		walletService.payOrderPayment(order, user);
		order.setStatus(OrderStatus.SUCCESS);
		;
		order.setOrderType(Ordertype.BUY);
		Order savedOrder = orderRepository.save(order);

		// create asset
		Asset oldAsset = assetService.findAssetByUserIdAndCoinId(order.getUser().getId(),
				order.getOrderItem().getCoin().getId());
		if (oldAsset == null) {
			assetService.create(user, orderItem.getCoin(), orderItem.getQuality());
		} else {
			assetService.updateAsset(oldAsset.getId(), quality);
		}
		return savedOrder;

	}

	@Transactional // if all the Stage reach then only it will process Further.....mean
	public Order sellAsset(Coin coin, double quality, User user) throws Exception {
		if (quality <= 0) {
			throw new Exception("quality should be >(0)");
		}
		double sellPrice = coin.getCurrentPrice();
		Asset assetToSell = assetService.findAssetByUserIdAndCoinId(user.getId(), coin.getId());
		double buyPrice = assetToSell.getBuyPrice();
		if (assetToSell != null) {  
			OrderItem orderItem = createOrderItem(coin, quality, buyPrice, sellPrice);

			Order order = createOrder(user, orderItem, Ordertype.SELL);
			orderItem.setOrder(order);
			if (assetToSell.getQuantity() >= quality) {
				order.setStatus(OrderStatus.SUCCESS);
				order.setOrderType(Ordertype.SELL);
				Order savedOrder = orderRepository.save(order);

				walletService.payOrderPayment(order, user);
				Asset updatedAsset = assetService.updateAsset(assetToSell.getId(), -quality);
				if (updatedAsset.getQuantity() * coin.getCurrentPrice() <= 1) {
					assetService.deleteAsset(updatedAsset.getId());

				}
				return savedOrder;
			}

			throw new Exception("Insuffient quantity to sell");
		}
		throw new Exception("asset not found");
	}

	@Override
	@Transactional
	public Order processOrder(Coin coin, double quality, Ordertype ordertype, String assetSymbol, User user)
			throws Exception {

		if (ordertype.equals(Ordertype.BUY)) {
			return buyAsset(coin, quality, user);
		} else if (ordertype.equals(Ordertype.SELL)) {
			return sellAsset(coin, quality, user);
		}
		throw new Exception("Invalid order type");

	}
}
