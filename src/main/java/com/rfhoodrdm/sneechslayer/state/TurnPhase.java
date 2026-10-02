package com.rfhoodrdm.sneechslayer.state;

/** The interaction phase currently presented by the game. */
public enum TurnPhase {
	NO_GAME,
	AWAITING_DIRECTION,
	MOVING,
	AWAITING_COMBAT_CHOICE,
	RESOLVING_COMBAT,
	GAME_OVER
}
