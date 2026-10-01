package com.rfhoodrdm.sneechslayer.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

/**
 * Shared colors and bundled fonts for the Swing interface.
 */
final class GuiTheme {

	static final Color PARCHMENT = new Color(238, 219, 172);
	static final Color DARK_PANEL = new Color(20, 18, 16, 205);
	static final Color GOLD = new Color(204, 158, 64);
	static final Color LEVEL_ONE = new Color(74, 140, 220);
	static final Color LEVEL_TWO = new Color(77, 170, 89);
	static final Color LEVEL_THREE = new Color(218, 151, 49);
	static final Color SNEECH = new Color(205, 68, 48);

	private static final Font CINZEL = loadFont("/assets/font/Cinzel-Bold.ttf", Font.SERIF, Font.BOLD);
	private static final Font ALEGREYA = loadFont("/assets/font/Alegreya.ttf", Font.SERIF, Font.PLAIN);

	private GuiTheme() {
	}

	static Font heading(float size) {
		return CINZEL.deriveFont(Font.BOLD, size);
	}

	static Font text(float size) {
		return ALEGREYA.deriveFont(Font.PLAIN, size);
	}

	static Font boldText(float size) {
		return ALEGREYA.deriveFont(Font.BOLD, size);
	}

	private static Font loadFont(String resourcePath, String fallbackFamily, int fallbackStyle) {
		try (InputStream inputStream = GuiTheme.class.getResourceAsStream(resourcePath)) {
			if (inputStream == null) {
				return new Font(fallbackFamily, fallbackStyle, 12);
			}
			Font font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
			return font;
		} catch (FontFormatException | IOException exception) {
			return new Font(fallbackFamily, fallbackStyle, 12);
		}
	}
}
