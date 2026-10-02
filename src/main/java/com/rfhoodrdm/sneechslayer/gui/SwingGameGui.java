package com.rfhoodrdm.sneechslayer.gui;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.Clip;

import javax.swing.SwingUtilities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rfhoodrdm.sneechslayer.dataloader.RequiresLoadedData;
import com.rfhoodrdm.sneechslayer.dataloader.asset.Asset;
import com.rfhoodrdm.sneechslayer.dataloader.asset.AssetRequest;
import com.rfhoodrdm.sneechslayer.dataloader.asset.AssetType;
import com.rfhoodrdm.sneechslayer.dataloader.asset.ImageAsset;
import com.rfhoodrdm.sneechslayer.dataloader.asset.SoundAsset;

import lombok.extern.slf4j.Slf4j;

/**
 * Loads and displays the Swing implementation of the game GUI.
 */
@Component
@Slf4j
public class SwingGameGui implements GameGui, RequiresLoadedData {

	private static final List<String> IMAGE_NAMES = List.of(
			"background/castle-ruins.png",
			"background/bramble-patch.png",
			"background/swamp-bog.png",
			"background/sneech-lair.png",
			"background/control-panel.png",
			"sprite/slime.png",
			"sprite/skeleton.png",
			"sprite/goblin.png",
			"sprite/zombie.png",
			"sprite/ogre.png",
			"sprite/dark-knight.png",
			"sprite/die-1.png",
			"sprite/die-2.png",
			"sprite/die-3.png",
			"sprite/die-4.png",
			"sprite/die-5.png",
			"sprite/die-6.png",
			"icon/clockwise.png",
			"icon/counter-clockwise.png",
			"icon/right.png");
	private static final List<String> SOUND_NAMES = List.of(
			"effect/dice_roll.aif", "effect/monster_growl.aif", "effect/player_attacks.aif",
			"effect/player_runs.aif", "effect/victory_fanfare.aif", "music/background_music.aif");

	private final String version;
	private Map<String, BufferedImage> images = Map.of();
	private Map<String, Clip> sounds = Map.of();

	public SwingGameGui(@Value("${game.version}") String version) {
		this.version = version;
	}

	@Override
	public List<AssetRequest> getAssetRequests() {
		List<AssetRequest> imageRequests = IMAGE_NAMES.stream()
				.map(name -> new AssetRequest(AssetType.IMAGE, name))
				.toList();
		if (GraphicsEnvironment.isHeadless()) {
			return imageRequests;
		}
		List<AssetRequest> soundRequests = SOUND_NAMES.stream()
				.map(name -> new AssetRequest(AssetType.SOUND, name)).toList();
		return java.util.stream.Stream.concat(imageRequests.stream(), soundRequests.stream()).toList();
	}

	@Override
	public void receiveLoadedAssets(List<Asset> assetList) {
		Map<String, BufferedImage> loadedImages = new LinkedHashMap<>();
		Map<String, Clip> loadedSounds = new LinkedHashMap<>();
		for (Asset asset : assetList) {
			if (asset instanceof ImageAsset imageAsset) {
				loadedImages.put(imageAsset.name(), imageAsset.image());
			} else if (asset instanceof SoundAsset soundAsset) {
				loadedSounds.put(soundAsset.name(), soundAsset.sound());
			}
		}
		images = Map.copyOf(loadedImages);
		sounds = Map.copyOf(loadedSounds);
		show();
	}

	@Override
	public void show() {
		if (GraphicsEnvironment.isHeadless()) {
			log.info("Skipping the Swing GUI because the environment is headless");
			return;
		}
		SwingUtilities.invokeLater(() -> {
			GameWindow gameWindow = new GameWindow(version, images, sounds);
			gameWindow.setVisible(true);
		});
	}
}
