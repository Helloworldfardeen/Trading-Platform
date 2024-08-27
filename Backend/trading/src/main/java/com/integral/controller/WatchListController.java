package com.integral.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.integral.model.Coin;
import com.integral.model.User;
import com.integral.model.WatchList;
import com.integral.service.CoinService;
import com.integral.service.UserService;
import com.integral.service.WatchListService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/watchlist")
public class WatchListController {
	@Autowired
	private WatchListService watchListService;
	@Autowired
	private UserService userService;
	@Autowired
	private CoinService coinService;

	@GetMapping("/user")
	public ResponseEntity<WatchList> getUserWatchlist(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		WatchList watchList = watchListService.findUserWatchList(user.getId());
		return ResponseEntity.ok(watchList);

	}

//	@PostMapping("/create")
//	public ResponseEntity<WatchList> createWatchList(@RequestHeader("Authorization") String jwt) throws Exception {
//		User user = userService.findUserProfileByJwt(jwt);
//		WatchList createWatchlist = watchListService.createWatchList(user);
//		return ResponseEntity.status(HttpStatus.CREATED).body(createWatchlist);
//
//	}
	@GetMapping("/{watchlistId}")
	public ResponseEntity<WatchList> getWatchbyId(@PathVariable Long watchListId) throws Exception {
		WatchList watchlist = watchListService.findById(watchListId);

		return ResponseEntity.ok(watchlist);
	}

	@PatchMapping("/add/coin/{coinId}")
	public ResponseEntity<Coin> addItemToWatchList(@RequestHeader("Authorization") String jwt,
			@PathVariable String coinId) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Coin coin = coinService.findById(coinId);
		Coin addedCoin = watchListService.additemToWatchList(coin, user);
		return ResponseEntity.ok(addedCoin);
	}

}
