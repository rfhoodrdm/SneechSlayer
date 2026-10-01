package com.rfhoodrdm.sneechslayer.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * A panel whose image stretches to fill all available space.
 */
class BackgroundPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final BufferedImage background;

	BackgroundPanel(BufferedImage background) {
		this.background = background;
		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		if (background != null) {
			Graphics2D graphics2d = (Graphics2D) graphics.create();
			graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			graphics2d.drawImage(background, 0, 0, getWidth(), getHeight(), null);
			graphics2d.dispose();
		}
		super.paintComponent(graphics);
	}
}
