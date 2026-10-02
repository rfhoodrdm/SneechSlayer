package com.rfhoodrdm.sneechslayer.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.rfhoodrdm.sneechslayer.state.DungeonLevel;
import com.rfhoodrdm.sneechslayer.state.DungeonState;
import com.rfhoodrdm.sneechslayer.state.MonsterSpace;
import com.rfhoodrdm.sneechslayer.state.MonsterType;
import com.rfhoodrdm.sneechslayer.state.PlayerColor;
import com.rfhoodrdm.sneechslayer.state.PlayerState;
import com.rfhoodrdm.sneechslayer.state.SneechSpace;

/**
 * Paints a dungeon background, its path spaces, and player tokens.
 */
final class DungeonPanel extends BackgroundPanel {

	private static final long serialVersionUID = 1L;
	private static final double[][] SPACE_LOCATIONS = {
			{ 0.18, 0.24 }, { 0.5, 0.18 }, { 0.82, 0.24 },
			{ 0.89, 0.48 }, { 0.82, 0.75 }, { 0.62, 0.84 },
			{ 0.38, 0.84 }, { 0.18, 0.75 }, { 0.11, 0.48 }
	};

	private final DungeonLevel dungeonLevel;
	private final Color themeColor;
	private List<PlayerColor> pitPlayers = List.of();
	private List<PlayerState> players = List.of();
	private DungeonState dungeonState;
	private int highlightedPosition = -1;

	DungeonPanel(DungeonLevel dungeonLevel, BufferedImage background, Color themeColor) {
		super(background);
		this.dungeonLevel = dungeonLevel;
		this.themeColor = themeColor;
	}

	void setPitPlayers(List<PlayerColor> players) {
		pitPlayers = List.copyOf(players);
		repaint();
	}

	void setGameView(DungeonState dungeonState, List<PlayerState> allPlayers, PlayerState activePlayer) {
		this.dungeonState = dungeonState;
		players = allPlayers.stream().filter(player -> player.dungeonLevel() == dungeonLevel).toList();
		pitPlayers = players.stream().filter(player -> player.position() == PlayerState.PIT_POSITION)
				.map(PlayerState::color).toList();
		highlightedPosition = activePlayer != null && activePlayer.dungeonLevel() == dungeonLevel
				? activePlayer.position() : -1;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D graphics2d = (Graphics2D) graphics.create();
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		paintTitle(graphics2d);
		paintPath(graphics2d);
		paintPit(graphics2d);
		graphics2d.dispose();
	}

	private void paintTitle(Graphics2D graphics) {
		int fontSize = Math.max(17, getHeight() / 21);
		graphics.setFont(GuiTheme.heading(fontSize));
		FontMetrics metrics = graphics.getFontMetrics();
		String title = dungeonLevel.getTheme();
		int x = (getWidth() - metrics.stringWidth(title)) / 2;
		int y = metrics.getAscent() + 8;
		graphics.setColor(new Color(0, 0, 0, 185));
		graphics.drawString(title, x + 2, y + 2);
		graphics.setColor(GuiTheme.PARCHMENT);
		graphics.drawString(title, x, y);
	}

	private void paintPath(Graphics2D graphics) {
		int diameter = Math.max(42, Math.min(getWidth(), getHeight()) / 8);
		for (int index = 0; index < SPACE_LOCATIONS.length; index++) {
			int centerX = (int) (getWidth() * SPACE_LOCATIONS[index][0]);
			int centerY = (int) (getHeight() * SPACE_LOCATIONS[index][1]);
			paintSpace(graphics, centerX, centerY, diameter, index);
		}
	}

	private void paintSpace(Graphics2D graphics, int centerX, int centerY, int diameter, int index) {
		int x = centerX - diameter / 2;
		int y = centerY - diameter / 2;
		graphics.setColor(new Color(15, 15, 15, 190));
		graphics.fillOval(x, y, diameter, diameter);
		graphics.setStroke(new BasicStroke(Math.max(2, diameter / 18f)));
		graphics.setColor(highlightedPosition == index + 1 ? themeColor : Color.GRAY);
		graphics.drawOval(x, y, diameter, diameter);

		String marker = markerAt(index);
		graphics.setFont(GuiTheme.boldText(Math.max(15, diameter / 3f)));
		FontMetrics metrics = graphics.getFontMetrics();
		int markerX = centerX - metrics.stringWidth(marker) / 2;
		int markerY = centerY + metrics.getAscent() / 3;
		graphics.setColor(Color.WHITE);
		graphics.drawString(marker, markerX, markerY);

		graphics.setFont(GuiTheme.boldText(Math.max(10, diameter / 5f)));
		graphics.setColor(themeColor);
		graphics.drawString(Integer.toString(index + 1), x + 4, y + diameter - 5);

		List<PlayerState> spacePlayers = players.stream()
				.filter(player -> player.position() == index + 1).toList();
		for (int playerIndex = spacePlayers.size() - 1; playerIndex >= 0; playerIndex--) {
			PlayerState player = spacePlayers.get(playerIndex);
			int offset = (spacePlayers.size() - 1 - playerIndex) * 6;
			new PipIcon(player.color(), Math.max(18, diameter / 3)).paintIcon(this, graphics,
					centerX - diameter / 5 + offset, centerY - diameter / 5 + offset);
		}
	}

	private String markerAt(int index) {
		if (dungeonState != null) {
			if (dungeonState.spaceAt(index + 1) instanceof MonsterSpace monsterSpace) {
				return Character.toString(monsterSpace.monsterType().getIndex());
			}
			return Integer.toString(((SneechSpace) dungeonState.spaceAt(index + 1)).power());
		}
		if (dungeonLevel == DungeonLevel.SNEECH_LAIR) {
			return Integer.toString(dungeonLevel.getSneechPowers().get(index));
		}
		MonsterType monsterType = dungeonLevel.getMonsters().get(index);
		return Character.toString(monsterType.getIndex());
	}

	private void paintPit(Graphics2D graphics) {
		int diameter = Math.max(54, Math.min(getWidth(), getHeight()) / 7);
		int x = (getWidth() - diameter) / 2;
		int y = (getHeight() - diameter) / 2;
		graphics.setColor(new Color(0, 0, 0, 205));
		graphics.fillOval(x, y, diameter, diameter);
		graphics.setStroke(new BasicStroke(3f));
		graphics.setColor(highlightedPosition == PlayerState.PIT_POSITION ? themeColor : Color.GRAY);
		graphics.drawOval(x, y, diameter, diameter);
		graphics.setFont(GuiTheme.boldText(Math.max(11, diameter / 5f)));
		graphics.setColor(GuiTheme.PARCHMENT);
		String label = "START";
		FontMetrics metrics = graphics.getFontMetrics();
		int labelX = x + (diameter - metrics.stringWidth(label)) / 2;
		graphics.drawString(label, labelX, y + diameter / 2 + 4);

		List<PlayerColor> players = new ArrayList<>(pitPlayers);
		for (int index = players.size() - 1; index >= 0; index--) {
			int offset = (players.size() - 1 - index) * 7;
			PipIcon pip = new PipIcon(players.get(index), Math.max(18, diameter / 3));
			pip.paintIcon(this, graphics, x + diameter / 2 - offset, y + diameter / 2 + offset);
		}
	}
}
