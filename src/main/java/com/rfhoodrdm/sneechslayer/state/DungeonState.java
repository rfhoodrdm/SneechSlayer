package com.rfhoodrdm.sneechslayer.state;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * The randomized, fixed-for-the-game layout of one dungeon level.
 */
public record DungeonState(DungeonLevel level, List<DungeonSpace> spaces) {

	public DungeonState {
		Objects.requireNonNull(level, "Dungeon level must not be null");
		Objects.requireNonNull(spaces, "Dungeon spaces must not be null");

		List<DungeonSpace> orderedSpaces = new ArrayList<>(spaces);
		orderedSpaces.forEach(space -> Objects.requireNonNull(space, "Dungeon space must not be null"));
		orderedSpaces.sort(Comparator.comparingInt(DungeonSpace::position));
		validatePositions(orderedSpaces);
		validateContents(level, orderedSpaces);
		spaces = List.copyOf(orderedSpaces);
	}

	public DungeonSpace spaceAt(int position) {
		DungeonSpace.validatePosition(position);
		return spaces.get(position - 1);
	}

	private static void validatePositions(List<DungeonSpace> spaces) {
		if (spaces.size() != DungeonSpace.MAX_POSITION) {
			throw new IllegalArgumentException("A dungeon layout must contain exactly 9 spaces");
		}

		for (int index = 0; index < spaces.size(); index++) {
			int expectedPosition = index + 1;
			if (spaces.get(index).position() != expectedPosition) {
				throw new IllegalArgumentException("A dungeon layout must contain each position from 1 through 9");
			}
		}
	}

	private static void validateContents(DungeonLevel level, List<DungeonSpace> spaces) {
		if (level.isNormal()) {
			validateMonsterContents(level, spaces);
			return;
		}
		validateSneechContents(level, spaces);
	}

	private static void validateMonsterContents(DungeonLevel level, List<DungeonSpace> spaces) {
		if (!spaces.stream().allMatch(MonsterSpace.class::isInstance)) {
			throw new IllegalArgumentException("Normal dungeon levels may contain only monster spaces");
		}

		List<MonsterType> actualMonsters = spaces.stream()
				.map(MonsterSpace.class::cast)
				.map(MonsterSpace::monsterType)
				.sorted()
				.toList();
		List<MonsterType> expectedMonsters = level.getMonsters().stream().sorted().toList();
		if (!actualMonsters.equals(expectedMonsters)) {
			throw new IllegalArgumentException("Monster spaces do not match the definition for " + level);
		}
	}

	private static void validateSneechContents(DungeonLevel level, List<DungeonSpace> spaces) {
		if (!spaces.stream().allMatch(SneechSpace.class::isInstance)) {
			throw new IllegalArgumentException("The Sneech Lair may contain only Sneech spaces");
		}

		List<Integer> actualPowers = spaces.stream()
				.map(SneechSpace.class::cast)
				.map(SneechSpace::power)
				.sorted()
				.toList();
		List<Integer> expectedPowers = level.getSneechPowers().stream().sorted().toList();
		if (!actualPowers.equals(expectedPowers)) {
			throw new IllegalArgumentException("Sneech spaces do not match the Sneech Lair definition");
		}
	}
}
