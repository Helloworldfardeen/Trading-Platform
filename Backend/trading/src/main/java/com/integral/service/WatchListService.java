package com.integral.service;

import com.integral.model.Coin;
import com.integral.model.User;
import com.integral.model.WatchList;

public interface WatchListService {
	WatchList findUserWatchList(Long userId) throws Exception;
	WatchList createWatchList(User user);
	WatchList findById(Long id) throws Exception;
	Coin additemToWatchList(Coin coin,User user) throws Exception;

}
