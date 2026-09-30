package com.rfhoodrdm.sneechslayer.dataloader.asset;

import javax.sound.sampled.Clip;

/**
 * A loaded sound. The receiving component owns the clip and must close it when
 * the clip is no longer needed.
 */
public record SoundAsset(Clip sound, String name) implements Asset {

}
