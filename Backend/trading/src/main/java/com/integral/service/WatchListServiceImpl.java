package com.integral.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integral.model.Coin;
import com.integral.model.User;
import com.integral.model.WatchList;
import com.integral.repository.WatchListRepository;

@Service
public class WatchListServiceImpl implements WatchListService {

	@Autowired
	private WatchListRepository watchlistRepo;

	@Override
	public WatchList findUserWatchList(Long userId) throws Exception {

		WatchList watchlist = watchlistRepo.findByUserId(userId);
		if (watchlist == null) {
			throw new Exception("watchlist not found");
		}
		return watchlist;
	}

	@Override
	public WatchList createWatchList(User user) {
		WatchList watchlist = new WatchList();
		watchlist.setUser(user);

		return watchlistRepo.save(watchlist);
	}

	@Override
	public WatchList findById(Long id) throws Exception {
		Optional<WatchList> watchlistOptional =watchlistRepo.findById(id);
		if(watchlistOptional.isEmpty())
		{
			throw new Exception("watchlist not found");
		}
		return watchlistOptional.get();
	}

	@Override
	public Coin additemToWatchList(Coin coin, User user) throws Exception {
		WatchList watchlist = findUserWatchList(user.getId());
		if(watchlist.getCoins().contains(coin))
		{
			watchlist.getCoins().remove(coin);
		}
		else
		{
			watchlist.getCoins().add(coin);
		}
		 watchlistRepo.save(watchlist);
		 return coin;
	}

}
