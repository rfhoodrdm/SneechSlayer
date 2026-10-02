package com.rfhoodrdm.sneechslayer.game;

import com.rfhoodrdm.sneechslayer.state.DungeonLevel;
import com.rfhoodrdm.sneechslayer.state.MonsterType;

/** The opponent occupying the current player's space. */
public record Encounter(DungeonLevel level, MonsterType monster, int enemyPower) {

	public boolean isSneech() {
		return monster == null;
	}
}
