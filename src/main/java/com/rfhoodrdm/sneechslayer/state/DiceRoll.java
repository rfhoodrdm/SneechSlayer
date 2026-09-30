package com.rfhoodrdm.sneechslayer.state;

/**
 * The result of rolling two six-sided dice.
 */
public record DiceRoll(int firstDie, int secondDie) {

	public DiceRoll {
		validateDie(firstDie);
		validateDie(secondDie);
	}

	public int total() {
		return firstDie + secondDie;
	}

	private static void validateDie(int value) {
		if (value < 1 || value > 6) {
			throw new IllegalArgumentException("A die value must be between 1 and 6: " + value);
		}
	}
}
