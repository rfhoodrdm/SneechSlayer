package com.rfhoodrdm.sneechslayer.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.random.RandomGenerator;

import com.rfhoodrdm.sneechslayer.state.DiceRoll;
import com.rfhoodrdm.sneechslayer.state.Direction;
import com.rfhoodrdm.sneechslayer.state.DungeonLevel;
import com.rfhoodrdm.sneechslayer.state.DungeonSpace;
import com.rfhoodrdm.sneechslayer.state.DungeonState;
import com.rfhoodrdm.sneechslayer.state.GameState;
import com.rfhoodrdm.sneechslayer.state.GameStatus;
import com.rfhoodrdm.sneechslayer.state.MonsterSpace;
import com.rfhoodrdm.sneechslayer.state.MonsterType;
import com.rfhoodrdm.sneechslayer.state.PlayerColor;
import com.rfhoodrdm.sneechslayer.state.PlayerState;
import com.rfhoodrdm.sneechslayer.state.SneechSpace;

/** Applies the board-game rules independently of the user interface. */
public final class GameEngine {

	private final RandomGenerator random;
	private GameState state;

	public GameEngine() {
		this(RandomGenerator.getDefault());
	}

	public GameEngine(RandomGenerator random) {
		this.random = Objects.requireNonNull(random, "Random generator must not be null");
	}

	public GameState startNewGame(List<PlayerColor> selectedColors) {
		Objects.requireNonNull(selectedColors, "Selected colors must not be null");
		List<PlayerState> players = new ArrayList<>(selectedColors.stream().map(PlayerState::initial).toList());
		Collections.shuffle(players, new java.util.Random(random.nextLong()));
		state = new GameState(players, createDungeons(), 1, 0, GameStatus.IN_PROGRESS);
		return state;
	}

	public GameState state() {
		return requireGame();
	}

	public DiceRoll rollDice() {
		return new DiceRoll(random.nextInt(1, 7), random.nextInt(1, 7));
	}

	public PlayerState moveCurrentPlayerOneSpace(Direction direction) {
		Objects.requireNonNull(direction, "Direction must not be null");
		GameState game = requirePlayableGame();
		PlayerState player = game.currentPlayer();
		int nextPosition;
		if (player.position() == PlayerState.PIT_POSITION) {
			nextPosition = DungeonSpace.MIN_POSITION;
		} else if (direction == Direction.CLOCKWISE) {
			nextPosition = player.position() == DungeonSpace.MAX_POSITION ? 1 : player.position() + 1;
		} else {
			nextPosition = player.position() == DungeonSpace.MIN_POSITION ? 9 : player.position() - 1;
		}
		PlayerState moved = new PlayerState(player.color(), player.experience(), player.dungeonLevel(),
				nextPosition, player.defeatedSneech());
		replaceCurrentPlayer(moved, game.status());
		return moved;
	}

	public Encounter currentEncounter() {
		GameState game = requireGame();
		PlayerState player = game.currentPlayer();
		if (player.position() == PlayerState.PIT_POSITION) {
			throw new IllegalStateException("The Start space has no encounter");
		}
		DungeonSpace space = game.dungeons().get(player.dungeonLevel()).spaceAt(player.position());
		if (space instanceof MonsterSpace monsterSpace) {
			MonsterType monster = monsterSpace.monsterType();
			return new Encounter(player.dungeonLevel(), monster, monster.powerOn(player.dungeonLevel()));
		}
		return new Encounter(player.dungeonLevel(), null, ((SneechSpace) space).power());
	}

	public CombatResult fight() {
		GameState game = requirePlayableGame();
		PlayerState before = game.currentPlayer();
		Encounter encounter = currentEncounter();
		DiceRoll roll = rollDice();
		int playerScore = roll.total() + before.experience();
		boolean won = playerScore >= encounter.enemyPower();
		boolean promoted = false;
		PlayerState after = before;
		if (won && encounter.isSneech()) {
			after = new PlayerState(before.color(), before.experience(), before.dungeonLevel(),
					before.position(), true);
		} else if (won) {
			int experience = Math.min(PlayerState.MAX_EXPERIENCE,
					before.experience() + encounter.monster().getExperienceReward());
			if (experience == PlayerState.MAX_EXPERIENCE) {
				DungeonLevel nextLevel = before.dungeonLevel().nextLevel().orElseThrow();
				int promotedExperience = nextLevel == DungeonLevel.SNEECH_LAIR ? experience : 0;
				after = new PlayerState(before.color(), promotedExperience, nextLevel,
						PlayerState.PIT_POSITION, false);
				promoted = true;
			} else {
				after = new PlayerState(before.color(), experience, before.dungeonLevel(),
						before.position(), false);
			}
		} else if (!encounter.isSneech()) {
			int experience = Math.max(PlayerState.MIN_EXPERIENCE,
					before.experience() - encounter.monster().getExperiencePenalty());
			after = new PlayerState(before.color(), experience, before.dungeonLevel(),
					before.position(), false);
		}
		GameStatus status = after.defeatedSneech() ? GameStatus.FINAL_ROUND : game.status();
		replaceCurrentPlayer(after, status);
		return new CombatResult(roll, playerScore, encounter.enemyPower(), won, promoted,
				before.dungeonLevel(), after);
	}

	public TurnAdvance finishTurn() {
		GameState game = requireGame();
		boolean endOfRound = game.currentPlayerIndex() == game.players().size() - 1;
		if (endOfRound && !game.winners().isEmpty()) {
			state = new GameState(game.players(), game.dungeons(), game.roundNumber(),
					game.currentPlayerIndex(), GameStatus.COMPLETE);
			return new TurnAdvance(true, false, state.winners());
		}
		int nextIndex = endOfRound ? 0 : game.currentPlayerIndex() + 1;
		int nextRound = endOfRound ? game.roundNumber() + 1 : game.roundNumber();
		state = new GameState(game.players(), game.dungeons(), nextRound, nextIndex, game.status());
		return new TurnAdvance(false, endOfRound, List.of());
	}

	private Map<DungeonLevel, DungeonState> createDungeons() {
		Map<DungeonLevel, DungeonState> dungeons = new EnumMap<>(DungeonLevel.class);
		for (DungeonLevel level : DungeonLevel.values()) {
			List<DungeonSpace> spaces = new ArrayList<>();
			if (level.isNormal()) {
				List<MonsterType> monsters = shuffled(level.getMonsters());
				for (int index = 0; index < monsters.size(); index++) {
					spaces.add(new MonsterSpace(index + 1, monsters.get(index)));
				}
			} else {
				List<Integer> powers = shuffled(level.getSneechPowers());
				for (int index = 0; index < powers.size(); index++) {
					spaces.add(new SneechSpace(index + 1, powers.get(index)));
				}
			}
			dungeons.put(level, new DungeonState(level, spaces));
		}
		return dungeons;
	}

	private <T> List<T> shuffled(List<T> values) {
		List<T> result = new ArrayList<>(values);
		Collections.shuffle(result, new java.util.Random(random.nextLong()));
		return result;
	}

	private void replaceCurrentPlayer(PlayerState player, GameStatus status) {
		GameState game = requireGame();
		List<PlayerState> players = new ArrayList<>(game.players());
		players.set(game.currentPlayerIndex(), player);
		state = new GameState(players, game.dungeons(), game.roundNumber(), game.currentPlayerIndex(), status);
	}

	private GameState requireGame() {
		if (state == null) {
			throw new IllegalStateException("No game has been started");
		}
		return state;
	}

	private GameState requirePlayableGame() {
		GameState game = requireGame();
		if (game.status() == GameStatus.COMPLETE) {
			throw new IllegalStateException("The game is over");
		}
		return game;
	}
}
