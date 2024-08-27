package com.integral.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.integral.model.Asset;
import com.integral.model.Coin;
import com.integral.model.User;
import com.integral.repository.AssetRepository;

@Service
public class AssetServiceImpl implements AssetService {

	@Autowired
	private AssetRepository assetRepository;

	@Override
	public Asset create(User user, Coin coin, double quantity) {
		Asset asset = new Asset();
		asset.setUser(user);
		asset.setCoin(coin);
		asset.setQuantity(quantity);
		asset.setBuyPrice(coin.getCurrentPrice());

		return assetRepository.save(asset);

	}

	@Override
	public Asset getAssetById(Long assetId) throws Exception {

		return assetRepository.findById(assetId).orElseThrow(() -> new Exception("asset not found"));
	}

	@Override
	public Asset getAssetByUserId(Long usrId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Asset> getUserAssets(Long userId) {
		return assetRepository.findByUserId(userId);
	}

	@Override
	public Asset updateAsset(Long assetId, double quality) throws Exception {

		Asset oldAsset = getAssetById(assetId);
		oldAsset.setQuantity(quality + oldAsset.getQuantity());
		return assetRepository.save(oldAsset);
	}

	@Override
	public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) {
           return assetRepository.findByUserIdAndCoinId(userId, coinId);
	}

	@Override
	public void deleteAsset(Long assetId) {
		assetRepository.deleteById(assetId);
	}

}
