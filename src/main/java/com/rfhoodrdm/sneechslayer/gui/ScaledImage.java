package com.rfhoodrdm.sneechslayer.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * Paints an image at the largest size that preserves its aspect ratio.
 */
final class ScaledImage extends JComponent {

	private static final long serialVersionUID = 1L;

	private BufferedImage image;

	ScaledImage(BufferedImage image, int preferredWidth, int preferredHeight) {
		this.image = image;
		setOpaque(false);
		setPreferredSize(new Dimension(preferredWidth, preferredHeight));
	}

	void setImage(BufferedImage image) {
		this.image = image;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		if (image == null) {
			return;
		}

		double scale = Math.min((double) getWidth() / image.getWidth(), (double) getHeight() / image.getHeight());
		int width = (int) Math.round(image.getWidth() * scale);
		int height = (int) Math.round(image.getHeight() * scale);
		int x = (getWidth() - width) / 2;
		int y = (getHeight() - height) / 2;

		Graphics2D graphics2d = (Graphics2D) graphics.create();
		graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2d.drawImage(image, x, y, width, height, null);
		graphics2d.dispose();
	}
}
