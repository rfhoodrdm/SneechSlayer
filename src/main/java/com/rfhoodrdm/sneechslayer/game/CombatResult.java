package com.rfhoodrdm.sneechslayer.game;

import com.rfhoodrdm.sneechslayer.state.DiceRoll;
import com.rfhoodrdm.sneechslayer.state.DungeonLevel;
import com.rfhoodrdm.sneechslayer.state.PlayerState;

/** The complete outcome of one combat choice. */
public record CombatResult(DiceRoll roll, int playerScore, int enemyScore, boolean won,
		boolean promoted, DungeonLevel previousLevel, PlayerState player) {
}
