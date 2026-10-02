package com.rfhoodrdm.sneechslayer.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JPanel;

import com.rfhoodrdm.sneechslayer.state.PlayerColor;
import com.rfhoodrdm.sneechslayer.state.PlayerState;

/**
 * Displays the zero-to-five experience scale and player pips.
 */
final class ExperienceGauge extends JPanel {

	private static final long serialVersionUID = 1L;

	private List<PlayerState> players = List.of(PlayerColor.RED, PlayerColor.BLUE,
			PlayerColor.GREEN, PlayerColor.YELLOW).stream().map(PlayerState::initial).toList();
	private PlayerColor currentPlayer;

	ExperienceGauge() {
		setOpaque(false);
		setPreferredSize(new Dimension(420, 78));
	}

	void setPlayers(List<PlayerState> players, PlayerColor currentPlayer) {
		this.players = List.copyOf(players);
		this.currentPlayer = currentPlayer;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D graphics2d = (Graphics2D) graphics.create();
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2d.setFont(GuiTheme.boldText(17f));
		FontMetrics metrics = graphics2d.getFontMetrics();
		int margin = 25;
		int usableWidth = getWidth() - margin * 2;
		int baseline = getHeight() - 10;

		graphics2d.setColor(new Color(10, 10, 10, 180));
		graphics2d.fillRoundRect(4, 2, getWidth() - 8, getHeight() - 4, 12, 12);
		graphics2d.setColor(GuiTheme.GOLD);
		graphics2d.drawLine(margin, baseline - metrics.getAscent() - 5,
				getWidth() - margin, baseline - metrics.getAscent() - 5);

		for (int experience = 0; experience <= 5; experience++) {
			int x = margin + usableWidth * experience / 5;
			String label = Integer.toString(experience);
			graphics2d.setColor(GuiTheme.PARCHMENT);
			graphics2d.drawString(label, x - metrics.stringWidth(label) / 2, baseline);
		}

		List<PlayerState> ordered = new java.util.ArrayList<>(players);
		ordered.sort(java.util.Comparator.comparing(player -> player.color() == currentPlayer));
		int[] stacks = new int[6];
		for (PlayerState player : ordered) {
			int stack = stacks[player.experience()]++;
			int x = margin + usableWidth * player.experience() / 5;
			PipIcon pip = new PipIcon(player.color(), 22);
			pip.paintIcon(this, graphics2d, x - 11 + stack * 6, 8 + stack * 4);
		}
		graphics2d.dispose();
	}
}
