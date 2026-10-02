package com.rfhoodrdm.sneechslayer.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sound.sampled.Clip;
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
import javax.swing.Timer;
import javax.swing.WindowConstants;

import com.rfhoodrdm.sneechslayer.game.CombatResult;
import com.rfhoodrdm.sneechslayer.game.Encounter;
import com.rfhoodrdm.sneechslayer.game.GameEngine;
import com.rfhoodrdm.sneechslayer.game.TurnAdvance;
import com.rfhoodrdm.sneechslayer.state.DiceRoll;
import com.rfhoodrdm.sneechslayer.state.Direction;
import com.rfhoodrdm.sneechslayer.state.DungeonLevel;
import com.rfhoodrdm.sneechslayer.state.GameState;
import com.rfhoodrdm.sneechslayer.state.PlayerColor;
import com.rfhoodrdm.sneechslayer.state.PlayerState;
import com.rfhoodrdm.sneechslayer.state.TurnPhase;

/** The top-level Sneech Slayer window and Swing turn-flow controller. */
final class GameWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private final DungeonPanel castleRuins;
	private final DungeonPanel bramblePatch;
	private final DungeonPanel swampBog;
	private final DungeonPanel sneechLair;
	private final ControlPanel controlPanel;
	private final Map<String, Clip> sounds;
	private final GameEngine engine = new GameEngine();
	private JCheckBoxMenuItem music;
	private JCheckBoxMenuItem soundEffects;
	private Direction selectedDirection;
	private int gameGeneration;

	GameWindow(String version, Map<String, BufferedImage> images, Map<String, Clip> sounds) {
		super("Sneech Slayer " + version);
		this.sounds = sounds;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
		wireControls();
		List<PlayerColor> initialPlayers = List.of(PlayerColor.RED, PlayerColor.BLUE,
				PlayerColor.GREEN, PlayerColor.YELLOW);
		castleRuins.setPitPlayers(initialPlayers);
		controlPanel.showInitialPlayers();
		setContentPane(createMainPanel());
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		addWindowListener(new WindowAdapter() {
			@Override public void windowClosed(WindowEvent event) { closeSounds(); }
		});
		playMusic();
	}

	private void wireControls() {
		controlPanel.onDirectionSelected(direction -> {
			selectedDirection = direction;
			controlPanel.directionWasSelected();
		});
		controlPanel.onMove(this::move);
		controlPanel.onFight(this::fight);
		controlPanel.onRun(this::runAway);
	}

	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		JMenuItem newGame = new JMenuItem("New Game");
		newGame.addActionListener(event -> promptForPlayers());
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(event -> dispose());
		gameMenu.add(newGame); gameMenu.add(exit);
		JMenu settings = new JMenu("Settings");
		music = new JCheckBoxMenuItem("Music", true);
		soundEffects = new JCheckBoxMenuItem("Sound Effects", true);
		music.addActionListener(event -> { if (music.isSelected()) playMusic(); else stopMusic(); });
		settings.add(music); settings.add(soundEffects);
		menuBar.add(gameMenu); menuBar.add(settings);
		return menuBar;
	}

	private void promptForPlayers() {
		JSpinner playerCount = new JSpinner(new SpinnerNumberModel(2, 2, 4, 1));
		int result = JOptionPane.showConfirmDialog(this, playerCount, "Number of Players",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (result != JOptionPane.OK_OPTION) return;
		int count = (Integer) playerCount.getValue();
		List<PlayerColor> colors = promptForColors(count);
		if (colors.size() != count) return;
		gameGeneration++;
		engine.startNewGame(colors);
		startTurn();
	}

	private List<PlayerColor> promptForColors(int count) {
		List<PlayerColor> selected = new ArrayList<>();
		EnumSet<PlayerColor> available = EnumSet.allOf(PlayerColor.class);
		for (int number = 1; number <= count; number++) {
			JComboBox<PlayerColor> choices = new JComboBox<>(available.toArray(PlayerColor[]::new));
			int result = JOptionPane.showConfirmDialog(this, choices, "Player " + number + " Color",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (result != JOptionPane.OK_OPTION) return List.of();
			PlayerColor color = (PlayerColor) choices.getSelectedItem();
			selected.add(color); available.remove(color);
		}
		return selected;
	}

	private void startTurn() {
		selectedDirection = null;
		controlPanel.clearTurnSelection();
		controlPanel.setInstruction("Choose movement direction to roll movement");
		controlPanel.setPhase(TurnPhase.AWAITING_DIRECTION);
		renderGame();
	}

	private void move() {
		if (selectedDirection == null) return;
		controlPanel.setPhase(TurnPhase.MOVING);
		DiceRoll roll = engine.rollDice();
		controlPanel.showDice(roll);
		playEffect("effect/dice_roll.aif");
		int generation = gameGeneration;
		final int[] remaining = { roll.total() };
		Timer timer = new Timer(250, null);
		timer.addActionListener(event -> {
			if (generation != gameGeneration) { timer.stop(); return; }
			engine.moveCurrentPlayerOneSpace(selectedDirection);
			renderGame();
			if (--remaining[0] == 0) { timer.stop(); showEncounter(); }
		});
		timer.setInitialDelay(0);
		timer.start();
	}

	private void showEncounter() {
		Encounter encounter = engine.currentEncounter();
		controlPanel.highlightMonster(encounter.monster());
		controlPanel.setInstruction("Select Fight or Run");
		controlPanel.setPhase(TurnPhase.AWAITING_COMBAT_CHOICE);
	}

	private void runAway() {
		controlPanel.setPhase(TurnPhase.RESOLVING_COMBAT);
		controlPanel.setInstruction("The player escapes");
		playEffect("effect/player_runs.aif");
		after(1000, this::finishTurn);
	}

	private void fight() {
		controlPanel.setPhase(TurnPhase.RESOLVING_COMBAT);
		CombatResult result = engine.fight();
		controlPanel.showDice(result.roll());
		controlPanel.setInstruction("Combat score: " + result.playerScore()
				+ " vs " + result.enemyScore());
		playEffect("effect/dice_roll.aif");
		after(1000, () -> {
			if (result.won()) playEffect("effect/player_attacks.aif");
			else playEffect("effect/monster_growl.aif");
			if (result.promoted()) playEffect("effect/victory_fanfare.aif");
			controlPanel.setInstruction(result.won() ? "Combat won!" : "Combat lost");
			renderGame();
			after(1000, this::finishTurn);
		});
	}

	private void finishTurn() {
		controlPanel.clearTurnSelection();
		TurnAdvance advance = engine.finishTurn();
		if (advance.gameOver()) {
			controlPanel.setPhase(TurnPhase.GAME_OVER);
			controlPanel.setInstruction("Game Over");
			renderGame();
			String winners = advance.winners().stream().map(PlayerState::color)
					.map(Enum::name).collect(Collectors.joining(", "));
			JOptionPane.showMessageDialog(this, "Winners: " + winners, "Sneech Defeated!",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			startTurn();
		}
	}

	private void renderGame() {
		GameState state = engine.state();
		PlayerState active = state.currentPlayer();
		castleRuins.setGameView(state.dungeons().get(DungeonLevel.CASTLE_RUINS), state.players(), active);
		bramblePatch.setGameView(state.dungeons().get(DungeonLevel.BRAMBLE_PATCH), state.players(), active);
		swampBog.setGameView(state.dungeons().get(DungeonLevel.SWAMP_BOG), state.players(), active);
		sneechLair.setGameView(state.dungeons().get(DungeonLevel.SNEECH_LAIR), state.players(), active);
		controlPanel.showGame(state);
	}

	private void after(int milliseconds, Runnable action) {
		int generation = gameGeneration;
		Timer timer = new Timer(milliseconds, event -> {
			((Timer) event.getSource()).stop();
			if (generation == gameGeneration) action.run();
		});
		timer.setRepeats(false); timer.start();
	}

	private void playEffect(String name) {
		if (soundEffects.isSelected()) playOnce(sounds.get(name));
	}
	private void playOnce(Clip clip) {
		if (clip == null) return;
		clip.stop(); clip.setFramePosition(0); clip.start();
	}
	private void playMusic() {
		Clip clip = sounds.get("music/background_music.aif");
		if (clip != null && !clip.isRunning()) { clip.setFramePosition(0); clip.loop(Clip.LOOP_CONTINUOUSLY); }
	}
	private void stopMusic() {
		Clip clip = sounds.get("music/background_music.aif"); if (clip != null) clip.stop();
	}
	private void closeSounds() { sounds.values().forEach(Clip::close); }

	private JPanel createMainPanel() {
		JPanel panel = new JPanel(new GridBagLayout()); panel.setBackground(java.awt.Color.BLACK);
		addPanel(panel, swampBog, 0, 0, 1); addPanel(panel, bramblePatch, 1, 0, 1);
		addPanel(panel, sneechLair, 0, 1, 1); addPanel(panel, castleRuins, 1, 1, 1);
		addPanel(panel, controlPanel, 2, 0, 2); return panel;
	}
	private void addPanel(JPanel parent, java.awt.Component component, int column, int row, int height) {
		GridBagConstraints constraints = new GridBagConstraints(); constraints.gridx = column;
		constraints.gridy = row; constraints.gridheight = height; constraints.weightx = 1;
		constraints.weighty = height; constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(3, 3, 3, 3); parent.add(component, constraints);
	}
}
