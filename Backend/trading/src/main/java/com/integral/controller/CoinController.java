package com.integral.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integral.model.Coin;
import com.integral.service.CoinService;

@RestController
@RequestMapping("/coins")
public class CoinController {

	@Autowired
	private CoinService coinService;
	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping
	ResponseEntity<List<Coin>> getCoinList(@RequestParam(required = false, name = "page") int page) throws Exception {
		List<Coin> coins = coinService.getCoinList(page);
		return new ResponseEntity<>(coins, HttpStatus.ACCEPTED);

	}

	//http://localhost:7575/coins/bitcoin/chart?days=2
	@GetMapping("/{coinId}/chart")                                         //days=2 like
 	ResponseEntity<JsonNode> getMarketChart(@PathVariable String coinId, @RequestParam("days") int days)
			throws Exception {
		String res = coinService.getMarketChart(coinId, days);
		JsonNode jsonNode = objectMapper.readTree(res);
		return new ResponseEntity<>(jsonNode, HttpStatus.ACCEPTED);

	}

	@GetMapping("search")//http://localhost:7575/coins/search?q=bitcoin
	ResponseEntity<JsonNode> searchCoin(@RequestParam("q") String keyword)
			throws JsonMappingException, JsonProcessingException {
		String coin = coinService.searchCoin(keyword);
		JsonNode jsonNode = objectMapper.readTree(coin);
		return ResponseEntity.ok(jsonNode);

	}

	@GetMapping("/top50") //http://localhost:7575/coins/top50
	ResponseEntity<JsonNode> getTop50CoinByMarketCapRank() throws JsonMappingException, JsonProcessingException {
		String coin = coinService.getTop50CoinsByMarketCapRank();
		JsonNode jsonNode = objectMapper.readTree(coin);
		return ResponseEntity.ok(jsonNode);

	}

	@GetMapping("/treading")//http://localhost:7575/coins/treading
	ResponseEntity<JsonNode> getTreadingCoin() throws JsonMappingException, JsonProcessingException {
		String coin = coinService.getTreadingCoins();
		JsonNode jsonNode = objectMapper.readTree(coin);
		return ResponseEntity.ok(jsonNode);

	}

	@GetMapping("/details/{coinId}")//http://localhost:7575/coins/details/bitcoin
	public ResponseEntity<JsonNode> getCoinDetails(@PathVariable String coinId)
			throws JsonMappingException, JsonProcessingException {
		String coin = coinService.getCoinDetails(coinId);
		JsonNode jn = objectMapper.readTree(coin);
		return ResponseEntity.status(HttpStatus.OK).body(jn);
	}

}
