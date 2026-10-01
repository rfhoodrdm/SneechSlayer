package com.rfhoodrdm.sneechslayer.entry;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.rfhoodrdm.sneechslayer.dataloader.DataLoader;
import com.rfhoodrdm.sneechslayer.dataloader.DataLoaderException;
import com.rfhoodrdm.sneechslayer.dataloader.RequiresLoadedData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is responsible for launching the GUI and performing any last-minute initialization.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class SneechSlayer implements ApplicationRunner {
	
	private final List<RequiresLoadedData> componentsThatRequireData;
	private final DataLoader dataLoader;
	
	@Override
	public void run(ApplicationArguments args) {
		try {
			for (RequiresLoadedData component : componentsThatRequireData) {
				component.receiveLoadedAssets(dataLoader.loadAssets(component.getAssetRequests()));
			}
		} catch (DataLoaderException exception) {
			log.error("The application could not load a required asset", exception);
			System.exit(1);
		}
		
		//show the gui.
	}

}
