package com.integral.service;

import java.util.List;

import com.integral.model.Asset;
import com.integral.model.Coin;
import com.integral.model.User;

public interface AssetService {

	Asset create(User user, Coin coin, double quantity);

	Asset getAssetById(Long assetId) throws Exception;

	Asset getAssetByUserId(Long usrId);

	List<Asset> getUserAssets(Long userId);

	Asset updateAsset(Long assetId, double quality) throws Exception;

	Asset findAssetByUserIdAndCoinId(Long userId, String coinId);

	void deleteAsset(Long assetId);
}
