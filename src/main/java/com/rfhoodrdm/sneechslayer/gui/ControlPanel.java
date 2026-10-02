package com.rfhoodrdm.sneechslayer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import com.rfhoodrdm.sneechslayer.state.DiceRoll;
import com.rfhoodrdm.sneechslayer.state.Direction;
import com.rfhoodrdm.sneechslayer.state.GameState;
import com.rfhoodrdm.sneechslayer.state.MonsterType;
import com.rfhoodrdm.sneechslayer.state.PlayerColor;
import com.rfhoodrdm.sneechslayer.state.PlayerState;
import com.rfhoodrdm.sneechslayer.state.TurnPhase;

/** The bestiary and game controls occupying the right side of the window. */
final class ControlPanel extends BackgroundPanel {
	private static final long serialVersionUID = 1L;
	private final Map<String, BufferedImage> images;
	private final Map<MonsterType, MonsterCard> monsterCards = new EnumMap<>(MonsterType.class);
	private final ExperienceGauge experienceGauge = new ExperienceGauge();
	private final JLabel roundDisplay = new JLabel("Round: 1", SwingConstants.CENTER);
	private final ScaledImage firstDie;
	private final ScaledImage secondDie;
	private final JRadioButton clockwise;
	private final JRadioButton counterClockwise;
	private final ButtonGroup directionGroup = new ButtonGroup();
	private final JButton move = new JButton("MOVE");
	private final JButton fight = new JButton("FIGHT");
	private final JButton run = new JButton("RUN");
	private final JLabel currentPlayer = new JLabel("Current Player: ");
	private final JLabel instruction = new JLabel("Select New Game To Start", SwingConstants.CENTER);

	ControlPanel(Map<String, BufferedImage> images) {
		super(images.get("background/control-panel.png"));
		this.images = images;
		firstDie = new ScaledImage(images.get("sprite/die-1.png"), 78, 78);
		secondDie = new ScaledImage(images.get("sprite/die-1.png"), 78, 78);
		clockwise = directionButton("CLOCKWISE", "icon/clockwise.png");
		counterClockwise = directionButton("COUNTER CLOCKWISE", "icon/counter-clockwise.png");
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		add(createRoundDisplay()); add(Box.createVerticalStrut(5));
		add(createMonsterGallery()); add(Box.createVerticalStrut(5));
		add(createSection("EXP", experienceGauge)); add(Box.createVerticalStrut(5));
		add(createDicePit()); add(Box.createVerticalStrut(5));
		add(createDirectionControls()); add(Box.createVerticalStrut(5));
		add(createCombatControls()); add(Box.createVerticalStrut(5));
		add(createStatus());
		setPhase(TurnPhase.NO_GAME);
	}

	void onDirectionSelected(Consumer<Direction> listener) {
		clockwise.addActionListener(event -> listener.accept(Direction.CLOCKWISE));
		counterClockwise.addActionListener(event -> listener.accept(Direction.COUNTER_CLOCKWISE));
	}
	void onMove(Runnable listener) { move.addActionListener(event -> listener.run()); }
	void onFight(Runnable listener) { fight.addActionListener(event -> listener.run()); }
	void onRun(Runnable listener) { run.addActionListener(event -> listener.run()); }

	void showInitialPlayers() {
		List<PlayerState> players = List.of(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.GREEN,
				PlayerColor.YELLOW).stream().map(PlayerState::initial).toList();
		experienceGauge.setPlayers(players, null);
		currentPlayer.setIcon(null);
	}

	void showGame(GameState state) {
		PlayerState player = state.currentPlayer();
		roundDisplay.setText("Round: " + state.roundNumber());
		currentPlayer.setIcon(new PipIcon(player.color(), 24));
		experienceGauge.setPlayers(state.players(), player.color());
	}

	void showDice(DiceRoll roll) {
		firstDie.setImage(images.get("sprite/die-" + roll.firstDie() + ".png"));
		secondDie.setImage(images.get("sprite/die-" + roll.secondDie() + ".png"));
	}
	void setInstruction(String text) { instruction.setText(text); }
	void highlightMonster(MonsterType monster) {
		monsterCards.forEach((type, card) -> card.setEncountered(type == monster));
	}
	void clearTurnSelection() { directionGroup.clearSelection(); highlightMonster(null); }
	void setPhase(TurnPhase phase) {
		boolean directions = phase == TurnPhase.AWAITING_DIRECTION;
		clockwise.setEnabled(directions); counterClockwise.setEnabled(directions);
		move.setEnabled(directions && directionGroup.getSelection() != null);
		boolean combat = phase == TurnPhase.AWAITING_COMBAT_CHOICE;
		fight.setEnabled(combat); run.setEnabled(combat);
	}
	void directionWasSelected() { move.setEnabled(true); }

	private JPanel createRoundDisplay() {
		roundDisplay.setFont(GuiTheme.heading(25f)); roundDisplay.setForeground(GuiTheme.PARCHMENT);
		return createSection(null, roundDisplay);
	}
	private JPanel createMonsterGallery() {
		JPanel gallery = transparentPanel(new GridLayout(2, 3, 4, 4));
		for (MonsterType type : MonsterType.values()) {
			String imageName = "sprite/" + type.name().toLowerCase().replace('_', '-') + ".png";
			MonsterCard card = new MonsterCard(type, images.get(imageName));
			monsterCards.put(type, card); gallery.add(card);
		}
		return createSection("Monster Gallery", gallery);
	}
	private JPanel createDicePit() {
		JPanel dice = transparentPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
		dice.add(firstDie); dice.add(secondDie); return createSection("Dice Pit", dice);
	}
	private JPanel createDirectionControls() {
		JPanel panel = transparentPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
		directionGroup.add(clockwise); directionGroup.add(counterClockwise);
		configureButton(move); move.setIcon(ImageUtilities.icon(images.get("icon/right.png"), 28, 28));
		panel.add(clockwise); panel.add(counterClockwise); panel.add(move);
		return createSection("Move Direction", panel);
	}
	private JRadioButton directionButton(String text, String image) {
		JRadioButton button = new JRadioButton(text, ImageUtilities.icon(images.get(image), 54, 54));
		button.setOpaque(false); button.setForeground(GuiTheme.PARCHMENT); button.setFont(GuiTheme.boldText(15f));
		button.setHorizontalTextPosition(SwingConstants.CENTER); button.setVerticalTextPosition(SwingConstants.BOTTOM);
		return button;
	}
	private JPanel createCombatControls() {
		JPanel combat = transparentPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		configureButton(fight); configureButton(run); combat.add(fight); combat.add(run);
		return createSection("Combat", combat);
	}
	private JPanel createStatus() {
		JPanel status = transparentPanel(new BorderLayout(8, 2));
		currentPlayer.setIconTextGap(8); currentPlayer.setFont(GuiTheme.boldText(17f));
		currentPlayer.setForeground(GuiTheme.PARCHMENT);
		instruction.setFont(GuiTheme.boldText(18f)); instruction.setForeground(GuiTheme.GOLD);
		status.add(currentPlayer, BorderLayout.WEST); status.add(instruction, BorderLayout.CENTER);
		return createSection("Status", status);
	}
	private JPanel createSection(String title, java.awt.Component content) {
		JPanel section = transparentPanel(new BorderLayout()); section.setBackground(GuiTheme.DARK_PANEL);
		section.setOpaque(true);
		section.setBorder(title == null ? BorderFactory.createLineBorder(new Color(150, 125, 75), 1)
				: BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(150, 125, 75), 1),
						title, 0, 0, GuiTheme.boldText(14f), GuiTheme.PARCHMENT));
		section.add(content, BorderLayout.CENTER); return section;
	}
	private JPanel transparentPanel(java.awt.LayoutManager layout) {
		JPanel panel = new JPanel(layout); panel.setOpaque(false); return panel;
	}
	private void configureButton(JButton button) {
		button.setFont(GuiTheme.heading(16f)); button.setPreferredSize(new Dimension(120, 38));
	}
}
