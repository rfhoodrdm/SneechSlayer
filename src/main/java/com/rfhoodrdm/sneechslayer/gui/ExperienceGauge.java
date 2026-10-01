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

/**
 * Displays the zero-to-five experience scale and player pips.
 */
final class ExperienceGauge extends JPanel {

	private static final long serialVersionUID = 1L;

	private List<PlayerColor> players = List.of(PlayerColor.RED, PlayerColor.BLUE,
			PlayerColor.GREEN, PlayerColor.YELLOW);

	ExperienceGauge() {
		setOpaque(false);
		setPreferredSize(new Dimension(420, 78));
	}

	void setPlayers(List<PlayerColor> players) {
		this.players = List.copyOf(players);
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

		for (int index = players.size() - 1; index >= 0; index--) {
			PipIcon pip = new PipIcon(players.get(index), 22);
			pip.paintIcon(this, graphics2d, margin - 11 + index * 7, 8 + index * 4);
		}
		graphics2d.dispose();
	}
}
