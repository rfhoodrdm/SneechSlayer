package com.rfhoodrdm.sneechslayer.gui;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

final class ImageUtilities {

	private ImageUtilities() {
	}

	static ImageIcon icon(BufferedImage source, int width, int height) {
		if (source == null) {
			return new ImageIcon(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
		}

		double scale = Math.min((double) width / source.getWidth(), (double) height / source.getHeight());
		int imageWidth = (int) Math.round(source.getWidth() * scale);
		int imageHeight = (int) Math.round(source.getHeight() * scale);
		BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = target.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		int x = (width - imageWidth) / 2;
		int y = (height - imageHeight) / 2;
		graphics.drawImage(source, x, y, imageWidth, imageHeight, null);
		graphics.dispose();
		return new ImageIcon(target);
	}
}
