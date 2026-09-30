package com.rfhoodrdm.sneechslayer.dataloader;

import java.util.List;

import com.rfhoodrdm.sneechslayer.dataloader.asset.Asset;
import com.rfhoodrdm.sneechslayer.dataloader.asset.AssetRequest;

/**
 * Marks a service or component that requires one or more loaded asset.
 */
public interface RequiresLoadedData {

	List<AssetRequest> getAssetRequests();

	void receiveLoadedAssets(List<Asset> assetList);
}
