package com.rfhoodrdm.sneechslayer.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

import com.rfhoodrdm.sneechslayer.state.PlayerColor;

/**
 * A circular player marker.
 */
final class PipIcon implements Icon {

	private final Color color;
	private final int size;

	PipIcon(PlayerColor playerColor, int size) {
		this.color = switch (playerColor) {
			case RED -> new Color(211, 52, 52);
			case BLUE -> new Color(61, 113, 224);
			case GREEN -> new Color(63, 174, 82);
			case YELLOW -> new Color(242, 202, 54);
		};
		this.size = size;
	}

	@Override
	public int getIconWidth() {
		return size;
	}

	@Override
	public int getIconHeight() {
		return size;
	}

	@Override
	public void paintIcon(Component component, Graphics graphics, int x, int y) {
		Graphics2D graphics2d = (Graphics2D) graphics.create();
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2d.setColor(Color.BLACK);
		graphics2d.fillOval(x, y, size, size);
		graphics2d.setColor(color);
		graphics2d.fillOval(x + 2, y + 2, size - 4, size - 4);
		graphics2d.setColor(new Color(255, 255, 255, 150));
		graphics2d.fillOval(x + size / 4, y + size / 5, size / 4, size / 5);
		graphics2d.dispose();
	}
}
