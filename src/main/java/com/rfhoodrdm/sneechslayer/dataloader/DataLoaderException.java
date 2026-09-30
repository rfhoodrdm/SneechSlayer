package com.rfhoodrdm.sneechslayer.dataloader;

import com.rfhoodrdm.sneechslayer.dataloader.asset.AssetRequest;

/**
 * Indicates that a requested asset was missing or could not be decoded.
 */
public class DataLoaderException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final AssetRequest assetRequest;

	public DataLoaderException(AssetRequest assetRequest, String message) {
		super(message);
		this.assetRequest = assetRequest;
	}

	public DataLoaderException(AssetRequest assetRequest, String message, Throwable cause) {
		super(message, cause);
		this.assetRequest = assetRequest;
	}

	public AssetRequest getAssetRequest() {
		return assetRequest;
	}
}
