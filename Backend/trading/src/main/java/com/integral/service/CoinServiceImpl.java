package com.integral.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integral.model.Coin;
import com.integral.repository.CoinRepository;

@Service // here all the method are same soo kindly pay attention try to understand this
public class CoinServiceImpl implements CoinService {

	@Autowired
	private CoinRepository coinRepository;
	@Autowired
	private ObjectMapper objectMapper;

	// imp method
	@Override // copy below url for better Understanding
	public List<Coin> getCoinList(int page) throws JsonMappingException, JsonProcessingException { // see for pagenation
		String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page=" + page;
		RestTemplate restTemplate = new RestTemplate();
		try {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			List<Coin> coinList = objectMapper.readValue(response.getBody(), new TypeReference<List<Coin>>() {
			});
			return coinList;
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	@Override // copy below url for better Understanding
	public String getMarketChart(String coindId, int days) throws JsonMappingException, JsonProcessingException {
		String url = "https://api.coingecko.com/api/v3/coins/" + coindId + "/market_chart?vs_currency=usd&days="
				+ "" + days;
		RestTemplate restTemplate = new RestTemplate();                   
		try {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			return response.getBody();
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override // copy below url for better Understanding
	public String getCoinDetails(String coinId) throws JsonMappingException, JsonProcessingException {
		String url = "https://api.coingecko.com/api/v3/coins/" + coinId;// here bitcoin,etherum....
		RestTemplate restTemplate = new RestTemplate();
		try {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			JsonNode jsonNode = objectMapper.readTree(response.getBody());
			Coin coin = new Coin();
			coin.setId(jsonNode.get("id").asText());
			coin.setName(jsonNode.get("name").asText());
			coin.setSymbol(jsonNode.get("symbol").asText());
			coin.setImage(jsonNode.get("image").asText());

			JsonNode marketData = jsonNode.get("market_data");

			coin.setCurrentPrice(marketData.get("current_price").get("usd").asLong());
			coin.setMarketCap(marketData.get("market_cap").get("usd").asLong());
			coin.setMarketCapRank(marketData.get("market_cap_rank").asInt());
			coin.setTotalVolume(marketData.get("total_volume").get("usd").asLong());
			coin.setHigh24h(marketData.get("high_24h").get("usd").asLong());
			coin.setLow24h(marketData.get("low_24h").get("usd").asLong());
			coin.setPriceChange24h(marketData.get("price_change_24h").asLong());// here is the I have Changed Made
			coin.setPriceChangePercentage24h(marketData.get("price_change_percentage_24h").asDouble());
			coin.setMarketCapChange24h(marketData.get("market_cap_change_24h").asLong());
			coin.setMarketCapChangePercentage24h(marketData.get("market_cap_change_percentage_24h").asLong());
			coin.setTotalSupply(marketData.get("total_supply").asLong());
			coinRepository.save(coin);
			return response.getBody();
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			System.out.println("error Inside the catch getCoinDetails" + e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override // copy below url for better Understanding
	public Coin findById(String coinId) throws Exception {
		Optional<Coin> optionalCoin = coinRepository.findById(coinId);
		if (optionalCoin.isEmpty()) {
			throw new Exception("coin not Found");
		}
		return optionalCoin.get();
	}

	@Override // copy below url for better Understanding
	public String searchCoin(String keyword) throws JsonMappingException, JsonProcessingException {
		String url = "https://api.coingecko.com/api/v3/search?query=" + keyword;
		RestTemplate restTemplate = new RestTemplate();
		try {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			return response.getBody();
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override // copy below url for better Understanding
	public String getTop50CoinsByMarketCapRank() throws JsonMappingException, JsonProcessingException {
		String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=50&page=1";
		RestTemplate restTemplate = new RestTemplate();
		try {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			return response.getBody();
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override // copy below url for better Understanding
	public String getTreadingCoins() throws JsonMappingException, JsonProcessingException {
		String url = "https://api.coingecko.com/api/v3/search/trending";
		RestTemplate restTemplate = new RestTemplate();
		try {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			return response.getBody();
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
