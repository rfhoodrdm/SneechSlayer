package com.rfhoodrdm.sneechslayer.state;

/**
 * A numbered space on a dungeon's circular path.
 */
public sealed interface DungeonSpace permits MonsterSpace, SneechSpace {

	int MIN_POSITION = 1;
	int MAX_POSITION = 9;

	int position();

	static void validatePosition(int position) {
		if (position < MIN_POSITION || position > MAX_POSITION) {
			throw new IllegalArgumentException("Dungeon space position must be between 1 and 9: " + position);
		}
	}
}
