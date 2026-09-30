package com.rfhoodrdm.sneechslayer.state;

import java.util.List;
import java.util.Optional;

/**
 * The fixed definition of each dungeon level.
 */
public enum DungeonLevel {
	CASTLE_RUINS(1, "Castle Ruins",
			List.of(MonsterType.SLIME, MonsterType.SLIME,
					MonsterType.SKELETON, MonsterType.SKELETON,
					MonsterType.GOBLIN, MonsterType.GOBLIN,
					MonsterType.ZOMBIE, MonsterType.OGRE, MonsterType.DARK_KNIGHT),
			List.of()),
	BRAMBLE_PATCH(2, "Bramble Patch",
			List.of(MonsterType.SLIME,
					MonsterType.SKELETON, MonsterType.SKELETON,
					MonsterType.GOBLIN, MonsterType.GOBLIN,
					MonsterType.ZOMBIE, MonsterType.ZOMBIE,
					MonsterType.OGRE, MonsterType.DARK_KNIGHT),
			List.of()),
	SWAMP_BOG(3, "Swamp Bog",
			List.of(MonsterType.SLIME, MonsterType.SKELETON,
					MonsterType.GOBLIN, MonsterType.GOBLIN,
					MonsterType.ZOMBIE, MonsterType.ZOMBIE,
					MonsterType.OGRE, MonsterType.OGRE, MonsterType.DARK_KNIGHT),
			List.of()),
	SNEECH_LAIR(4, "Sneech Lair", List.of(),
			List.of(12, 12, 13, 13, 14, 14, 15, 15, 15));

	private final int number;
	private final String theme;
	private final List<MonsterType> monsters;
	private final List<Integer> sneechPowers;

	DungeonLevel(int number, String theme, List<MonsterType> monsters, List<Integer> sneechPowers) {
		this.number = number;
		this.theme = theme;
		this.monsters = monsters;
		this.sneechPowers = sneechPowers;
	}

	public int getNumber() {
		return number;
	}

	public String getTheme() {
		return theme;
	}

	public boolean isNormal() {
		return this != SNEECH_LAIR;
	}

	public List<MonsterType> getMonsters() {
		return monsters;
	}

	public List<Integer> getSneechPowers() {
		return sneechPowers;
	}

	public Optional<DungeonLevel> nextLevel() {
		int nextOrdinal = ordinal() + 1;
		if (nextOrdinal >= values().length) {
			return Optional.empty();
		}
		return Optional.of(values()[nextOrdinal]);
	}
}
