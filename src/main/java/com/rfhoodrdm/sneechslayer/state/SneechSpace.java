package com.rfhoodrdm.sneechslayer.state;

/**
 * A Sneech Lair space containing the Sneech's power at that position.
 */
public record SneechSpace(int position, int power) implements DungeonSpace {

	public SneechSpace {
		DungeonSpace.validatePosition(position);
		if (power < 1) {
			throw new IllegalArgumentException("Sneech power must be positive: " + power);
		}
	}
}
