package com.rfhoodrdm.sneechslayer.dataloader;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.springframework.stereotype.Service;

import com.rfhoodrdm.sneechslayer.dataloader.asset.Asset;
import com.rfhoodrdm.sneechslayer.dataloader.asset.AssetRequest;
import com.rfhoodrdm.sneechslayer.dataloader.asset.ImageAsset;
import com.rfhoodrdm.sneechslayer.dataloader.asset.SoundAsset;

/**
 * Loads image and sound assets from the conventional classpath directories.
 */
@Service
public class SimpleDataLoader implements DataLoader {

	private static final String IMAGE_DIRECTORY = "assets/image/";
	private static final String SOUND_DIRECTORY = "assets/sound/";

	@Override
	public List<Asset> loadAssets(List<AssetRequest> assetRequests) {
		Objects.requireNonNull(assetRequests, "Asset requests must not be null");

		List<Asset> loadedAssets = new ArrayList<>(assetRequests.size());
		try {
			for (AssetRequest assetRequest : assetRequests) {
				loadedAssets.add(loadAsset(assetRequest));
			}
			return List.copyOf(loadedAssets);
		} catch (RuntimeException exception) {
			closeLoadedSounds(loadedAssets);
			throw exception;
		}
	}

	private Asset loadAsset(AssetRequest assetRequest) {
		Objects.requireNonNull(assetRequest, "Asset request must not be null");
		validateAssetName(assetRequest.assetName());

		return switch (Objects.requireNonNull(assetRequest.assetType(), "Asset type must not be null")) {
			case IMAGE -> loadImage(assetRequest);
			case SOUND -> loadSound(assetRequest);
		};
	}

	private ImageAsset loadImage(AssetRequest assetRequest) {
		String resourcePath = IMAGE_DIRECTORY + assetRequest.assetName();
		try (InputStream inputStream = openResource(resourcePath, assetRequest)) {
			BufferedImage image = ImageIO.read(inputStream);
			if (image == null) {
				throw new DataLoaderException(assetRequest,
						"The resource is not a supported image: " + resourcePath);
			}
			return new ImageAsset(image, assetRequest.assetName());
		} catch (IOException exception) {
			throw new DataLoaderException(assetRequest,
					"Could not load image resource: " + resourcePath, exception);
		}
	}

	private SoundAsset loadSound(AssetRequest assetRequest) {
		String resourcePath = SOUND_DIRECTORY + assetRequest.assetName();
		Clip clip = null;
		try (InputStream resourceStream = openResource(resourcePath, assetRequest);
				BufferedInputStream bufferedStream = new BufferedInputStream(resourceStream);
				AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedStream)) {
			clip = AudioSystem.getClip();
			clip.open(audioStream);
			return new SoundAsset(clip, assetRequest.assetName());
		} catch (IOException | UnsupportedAudioFileException | LineUnavailableException exception) {
			if (clip != null) {
				clip.close();
			}
			throw new DataLoaderException(assetRequest,
					"Could not load sound resource: " + resourcePath, exception);
		}
	}

	private InputStream openResource(String resourcePath, AssetRequest assetRequest) {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
		if (inputStream == null) {
			throw new DataLoaderException(assetRequest,
					"No asset was found at classpath resource: " + resourcePath);
		}
		return inputStream;
	}

	private void validateAssetName(String assetName) {
		if (assetName == null || assetName.isBlank()) {
			throw new IllegalArgumentException("Asset name must not be blank");
		}
		if (assetName.startsWith("/") || assetName.contains("\\")
				|| List.of(assetName.split("/", -1)).contains("..")) {
			throw new IllegalArgumentException("Asset name must be a safe relative path: " + assetName);
		}
	}

	private void closeLoadedSounds(List<Asset> loadedAssets) {
		loadedAssets.stream()
				.filter(SoundAsset.class::isInstance)
				.map(SoundAsset.class::cast)
				.map(SoundAsset::sound)
				.forEach(Clip::close);
	}

}
