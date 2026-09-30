package com.rfhoodrdm.sneechslayer.state;

import java.util.Objects;

/**
 * A normal dungeon space containing a monster type.
 */
public record MonsterSpace(int position, MonsterType monsterType) implements DungeonSpace {

	public MonsterSpace {
		DungeonSpace.validatePosition(position);
		Objects.requireNonNull(monsterType, "Monster type must not be null");
	}
}
