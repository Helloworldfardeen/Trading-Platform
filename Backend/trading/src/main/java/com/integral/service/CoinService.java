package com.integral.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.integral.model.Coin;

public interface CoinService {
	List<Coin> getCoinList(int page) throws JsonMappingException, JsonProcessingException;

	String getMarketChart(String coindId, int days) throws JsonMappingException, JsonProcessingException;

	String getCoinDetails(String coinId) throws JsonMappingException, JsonProcessingException;

	Coin findById(String coinId) throws Exception;// this method is inbuild

	String searchCoin(String keyword) throws JsonMappingException, JsonProcessingException;

	String getTop50CoinsByMarketCapRank() throws JsonMappingException, JsonProcessingException;

	String getTreadingCoins() throws JsonMappingException, JsonProcessingException;

}
