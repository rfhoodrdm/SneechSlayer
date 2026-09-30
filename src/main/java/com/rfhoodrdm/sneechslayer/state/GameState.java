package com.rfhoodrdm.sneechslayer.state;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A complete snapshot of one running game.
 * <p>
 * Players are stored in their fixed turn order.
 */
public record GameState(List<PlayerState> players, Map<DungeonLevel, DungeonState> dungeons,
		int roundNumber, int currentPlayerIndex, GameStatus status) {

	public static final int MIN_PLAYERS = 2;
	public static final int MAX_PLAYERS = 4;

	public GameState {
		Objects.requireNonNull(players, "Players must not be null");
		Objects.requireNonNull(dungeons, "Dungeons must not be null");
		Objects.requireNonNull(status, "Game status must not be null");

		players = List.copyOf(players);
		validatePlayers(players);
		dungeons = copyAndValidateDungeons(dungeons);

		if (roundNumber < 1) {
			throw new IllegalArgumentException("Round number must be at least 1: " + roundNumber);
		}
		if (currentPlayerIndex < 0 || currentPlayerIndex >= players.size()) {
			throw new IllegalArgumentException("Current player index is outside the turn order: "
					+ currentPlayerIndex);
		}

		boolean hasWinner = players.stream().anyMatch(PlayerState::defeatedSneech);
		if (status == GameStatus.IN_PROGRESS && hasWinner) {
			throw new IllegalArgumentException("A game with a winner must be in its final round or complete");
		}
		if (status != GameStatus.IN_PROGRESS && !hasWinner) {
			throw new IllegalArgumentException("A final-round or complete game must have at least one winner");
		}
	}

	public PlayerState currentPlayer() {
		return players.get(currentPlayerIndex);
	}

	public List<PlayerState> winners() {
		return players.stream().filter(PlayerState::defeatedSneech).toList();
	}

	private static void validatePlayers(List<PlayerState> players) {
		if (players.size() < MIN_PLAYERS || players.size() > MAX_PLAYERS) {
			throw new IllegalArgumentException("A game must have between 2 and 4 players");
		}

		Set<PlayerColor> colors = EnumSet.noneOf(PlayerColor.class);
		for (PlayerState player : players) {
			Objects.requireNonNull(player, "Player must not be null");
			if (!colors.add(player.color())) {
				throw new IllegalArgumentException("Player colors must be unique: " + player.color());
			}
		}
	}

	private static Map<DungeonLevel, DungeonState> copyAndValidateDungeons(
			Map<DungeonLevel, DungeonState> dungeons) {
		if (!dungeons.keySet().equals(EnumSet.allOf(DungeonLevel.class))) {
			throw new IllegalArgumentException("Game state must contain every dungeon level");
		}

		Map<DungeonLevel, DungeonState> copy = new EnumMap<>(DungeonLevel.class);
		for (Map.Entry<DungeonLevel, DungeonState> entry : dungeons.entrySet()) {
			DungeonLevel level = Objects.requireNonNull(entry.getKey(), "Dungeon level key must not be null");
			DungeonState dungeon = Objects.requireNonNull(entry.getValue(), "Dungeon state must not be null");
			if (level != dungeon.level()) {
				throw new IllegalArgumentException("Dungeon map key does not match its state: " + level);
			}
			copy.put(level, dungeon);
		}
		return Collections.unmodifiableMap(copy);
	}
}
