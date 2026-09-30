package com.rfhoodrdm.sneechslayer.state;

import java.util.Objects;

/**
 * The state of one player at a particular point in the game.
 */
public record PlayerState(PlayerColor color, int experience, DungeonLevel dungeonLevel, int position,
		boolean defeatedSneech) {

	public static final int MIN_EXPERIENCE = 0;
	public static final int MAX_EXPERIENCE = 5;
	public static final int PIT_POSITION = 0;

	public PlayerState {
		Objects.requireNonNull(color, "Player color must not be null");
		Objects.requireNonNull(dungeonLevel, "Dungeon level must not be null");
		if (experience < MIN_EXPERIENCE || experience > MAX_EXPERIENCE) {
			throw new IllegalArgumentException("Player experience must be between 0 and 5: " + experience);
		}
		if (position < PIT_POSITION || position > DungeonSpace.MAX_POSITION) {
			throw new IllegalArgumentException("Player position must be between 0 and 9: " + position);
		}
		if (defeatedSneech && dungeonLevel != DungeonLevel.SNEECH_LAIR) {
			throw new IllegalArgumentException("A player can defeat the Sneech only in the Sneech Lair");
		}
	}

	public static PlayerState initial(PlayerColor color) {
		return new PlayerState(color, MIN_EXPERIENCE, DungeonLevel.CASTLE_RUINS, PIT_POSITION, false);
	}
}
