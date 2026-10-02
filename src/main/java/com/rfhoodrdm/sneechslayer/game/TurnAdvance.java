package com.rfhoodrdm.sneechslayer.game;

import java.util.List;

import com.rfhoodrdm.sneechslayer.state.PlayerState;

/** Result of ending a turn and selecting what happens next. */
public record TurnAdvance(boolean gameOver, boolean newRound, List<PlayerState> winners) {

	public TurnAdvance {
		winners = List.copyOf(winners);
	}
}
