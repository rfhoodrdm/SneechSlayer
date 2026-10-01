package com.rfhoodrdm.sneechslayer.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;

import com.rfhoodrdm.sneechslayer.state.DungeonLevel;
import com.rfhoodrdm.sneechslayer.state.PlayerColor;

/**
 * The top-level Sneech Slayer window.
 */
final class GameWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private final DungeonPanel castleRuins;
	private final DungeonPanel bramblePatch;
	private final DungeonPanel swampBog;
	private final DungeonPanel sneechLair;
	private final ControlPanel controlPanel;

	GameWindow(String version, Map<String, BufferedImage> images) {
		super("Sneech Slayer " + version);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setJMenuBar(createMenuBar());

		castleRuins = new DungeonPanel(DungeonLevel.CASTLE_RUINS,
				images.get("background/castle-ruins.png"), GuiTheme.LEVEL_ONE);
		bramblePatch = new DungeonPanel(DungeonLevel.BRAMBLE_PATCH,
				images.get("background/bramble-patch.png"), GuiTheme.LEVEL_TWO);
		swampBog = new DungeonPanel(DungeonLevel.SWAMP_BOG,
				images.get("background/swamp-bog.png"), GuiTheme.LEVEL_THREE);
		sneechLair = new DungeonPanel(DungeonLevel.SNEECH_LAIR,
				images.get("background/sneech-lair.png"), GuiTheme.SNEECH);
		controlPanel = new ControlPanel(images);

		List<PlayerColor> defaultPlayers = List.of(PlayerColor.RED, PlayerColor.BLUE,
				PlayerColor.GREEN, PlayerColor.YELLOW);
		castleRuins.setPitPlayers(defaultPlayers);
		setContentPane(createMainPanel());
		setExtendedState(JFrame.MAXIMIZED_BOTH);
	}

	private JPanel createMainPanel() {
		JPanel mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBackground(java.awt.Color.BLACK);
		addPanel(mainPanel, swampBog, 0, 0, 1);
		addPanel(mainPanel, bramblePatch, 1, 0, 1);
		addPanel(mainPanel, sneechLair, 0, 1, 1);
		addPanel(mainPanel, castleRuins, 1, 1, 1);
		addPanel(mainPanel, controlPanel, 2, 0, 2);
		return mainPanel;
	}

	private void addPanel(JPanel parent, java.awt.Component component, int column, int row, int height) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = column;
		constraints.gridy = row;
		constraints.gridheight = height;
		constraints.weightx = 1;
		constraints.weighty = height;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(3, 3, 3, 3);
		parent.add(component, constraints);
	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(createGameMenu());
		menuBar.add(createSettingsMenu());
		return menuBar;
	}

	private JMenu createGameMenu() {
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setFont(GuiTheme.text(15f));

		JMenuItem newGame = new JMenuItem("New Game");
		newGame.setFont(GuiTheme.text(15f));
		newGame.addActionListener(event -> promptForPlayers());
		gameMenu.add(newGame);

		JMenuItem exit = new JMenuItem("Exit");
		exit.setFont(GuiTheme.text(15f));
		exit.addActionListener(event -> dispose());
		gameMenu.add(exit);
		return gameMenu;
	}

	private JMenu createSettingsMenu() {
		JMenu settingsMenu = new JMenu("Settings");
		settingsMenu.setFont(GuiTheme.text(15f));
		JCheckBoxMenuItem music = new JCheckBoxMenuItem("Music", true);
		JCheckBoxMenuItem soundEffects = new JCheckBoxMenuItem("Sound Effects", true);
		music.setFont(GuiTheme.text(15f));
		soundEffects.setFont(GuiTheme.text(15f));
		settingsMenu.add(music);
		settingsMenu.add(soundEffects);
		return settingsMenu;
	}

	private void promptForPlayers() {
		JSpinner playerCount = new JSpinner(new SpinnerNumberModel(2, 2, 4, 1));
		int result = JOptionPane.showConfirmDialog(this, playerCount, "Number of Players",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (result != JOptionPane.OK_OPTION) {
			return;
		}

		int count = (Integer) playerCount.getValue();
		List<PlayerColor> colors = promptForColors(count);
		if (colors.size() != count) {
			return;
		}
		showNewPlayers(colors);
	}

	private List<PlayerColor> promptForColors(int count) {
		List<PlayerColor> selectedColors = new ArrayList<>();
		EnumSet<PlayerColor> availableColors = EnumSet.allOf(PlayerColor.class);
		for (int playerNumber = 1; playerNumber <= count; playerNumber++) {
			JComboBox<PlayerColor> choices = new JComboBox<>(availableColors.toArray(PlayerColor[]::new));
			int result = JOptionPane.showConfirmDialog(this, choices, "Player " + playerNumber + " Color",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (result != JOptionPane.OK_OPTION) {
				return List.of();
			}
			PlayerColor selection = (PlayerColor) choices.getSelectedItem();
			selectedColors.add(selection);
			availableColors.remove(selection);
		}
		return selectedColors;
	}

	private void showNewPlayers(List<PlayerColor> players) {
		castleRuins.setPitPlayers(players);
		bramblePatch.setPitPlayers(List.of());
		swampBog.setPitPlayers(List.of());
		sneechLair.setPitPlayers(List.of());
		controlPanel.setPlayers(players);
	}
}
