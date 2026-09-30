package com.rfhoodrdm.sneechslayer.state;

/**
 * The fixed attributes shared by every encounter with a monster type.
 */
public enum MonsterType {
	SLIME('A', "Slime", 4, 1, 0),
	SKELETON('B', "Skeleton", 5, 1, 0),
	GOBLIN('C', "Goblin", 6, 1, 1),
	ZOMBIE('D', "Zombie", 7, 1, 1),
	OGRE('E', "Ogre", 8, 2, 2),
	DARK_KNIGHT('F', "Dark Knight", 9, 2, 1);

	private final char index;
	private final String displayName;
	private final int basePower;
	private final int experienceReward;
	private final int experiencePenalty;

	MonsterType(char index, String displayName, int basePower, int experienceReward, int experiencePenalty) {
		this.index = index;
		this.displayName = displayName;
		this.basePower = basePower;
		this.experienceReward = experienceReward;
		this.experiencePenalty = experiencePenalty;
	}

	public char getIndex() {
		return index;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getBasePower() {
		return basePower;
	}

	public int getExperienceReward() {
		return experienceReward;
	}

	public int getExperiencePenalty() {
		return experiencePenalty;
	}

	/**
	 * Calculates this monster's power on a normal dungeon level.
	 */
	public int powerOn(DungeonLevel dungeonLevel) {
		if (!dungeonLevel.isNormal()) {
			throw new IllegalArgumentException("Ordinary monsters cannot appear in the Sneech Lair");
		}
		return basePower + dungeonLevel.getNumber();
	}
}
