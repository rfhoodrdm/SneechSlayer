package com.rfhoodrdm.sneechslayer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import com.rfhoodrdm.sneechslayer.state.MonsterType;
import com.rfhoodrdm.sneechslayer.state.PlayerColor;

/**
 * The bestiary and game controls occupying the right side of the window.
 */
final class ControlPanel extends BackgroundPanel {

	private static final long serialVersionUID = 1L;

	private final ExperienceGauge experienceGauge = new ExperienceGauge();
	private final JLabel currentPlayer = new JLabel();
	private final JLabel instruction = new JLabel("Choose New Game to Start", SwingConstants.CENTER);

	ControlPanel(Map<String, BufferedImage> images) {
		super(images.get("background/control-panel.png"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

		add(createRoundDisplay());
		add(Box.createVerticalStrut(5));
		add(createMonsterGallery(images));
		add(Box.createVerticalStrut(5));
		add(createSection("EXP", experienceGauge));
		add(Box.createVerticalStrut(5));
		add(createDicePit(images));
		add(Box.createVerticalStrut(5));
		add(createDirectionControls(images));
		add(Box.createVerticalStrut(5));
		add(createCombatControls());
		add(Box.createVerticalStrut(5));
		add(createStatus());
	}

	void setPlayers(List<PlayerColor> players) {
		experienceGauge.setPlayers(players);
		if (players.isEmpty()) {
			currentPlayer.setIcon(null);
			return;
		}
		currentPlayer.setIcon(new PipIcon(players.getFirst(), 24));
		instruction.setText("Choose direction");
	}

	private JPanel createRoundDisplay() {
		JLabel label = new JLabel("Round: 1", SwingConstants.CENTER);
		label.setFont(GuiTheme.heading(25f));
		label.setForeground(GuiTheme.PARCHMENT);
		return createSection(null, label);
	}

	private JPanel createMonsterGallery(Map<String, BufferedImage> images) {
		JPanel gallery = transparentPanel(new GridLayout(2, 3, 4, 4));
		for (MonsterType monsterType : MonsterType.values()) {
			String imageName = "sprite/" + monsterType.name().toLowerCase().replace('_', '-') + ".png";
			gallery.add(new MonsterCard(monsterType, images.get(imageName)));
		}
		return createSection("Monster Gallery", gallery);
	}

	private JPanel createDicePit(Map<String, BufferedImage> images) {
		JPanel dice = transparentPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
		dice.add(new ScaledImage(images.get("sprite/die-1.png"), 78, 78));
		dice.add(new ScaledImage(images.get("sprite/die-1.png"), 78, 78));
		return createSection("Dice Pit", dice);
	}

	private JPanel createDirectionControls(Map<String, BufferedImage> images) {
		JPanel directions = transparentPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		JRadioButton clockwise = new JRadioButton("CLOCKWISE",
				ImageUtilities.icon(images.get("icon/clockwise.png"), 54, 54));
		JRadioButton counterClockwise = new JRadioButton("COUNTER CLOCKWISE",
				ImageUtilities.icon(images.get("icon/counter-clockwise.png"), 54, 54));
		configureRadioButton(clockwise);
		configureRadioButton(counterClockwise);
		clockwise.setEnabled(false);
		counterClockwise.setEnabled(false);
		ButtonGroup group = new ButtonGroup();
		group.add(clockwise);
		group.add(counterClockwise);
		directions.add(clockwise);
		directions.add(counterClockwise);
		return createSection("Move Direction", directions);
	}

	private JPanel createCombatControls() {
		JPanel combat = transparentPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		JButton fight = new JButton("FIGHT");
		JButton run = new JButton("RUN");
		configureButton(fight);
		configureButton(run);
		fight.setEnabled(false);
		run.setEnabled(false);
		combat.add(fight);
		combat.add(run);
		return createSection("Combat", combat);
	}

	private JPanel createStatus() {
		JPanel status = transparentPanel(new BorderLayout(8, 2));
		currentPlayer.setText("Current Player: ");
		currentPlayer.setIconTextGap(8);
		currentPlayer.setFont(GuiTheme.boldText(17f));
		currentPlayer.setForeground(GuiTheme.PARCHMENT);
		instruction.setFont(GuiTheme.boldText(18f));
		instruction.setForeground(GuiTheme.GOLD);
		status.add(currentPlayer, BorderLayout.WEST);
		status.add(instruction, BorderLayout.CENTER);
		return createSection("Status", status);
	}

	private JPanel createSection(String title, java.awt.Component content) {
		JPanel section = transparentPanel(new BorderLayout());
		section.setBackground(GuiTheme.DARK_PANEL);
		section.setOpaque(true);
		section.setBorder(title == null
				? BorderFactory.createLineBorder(new Color(150, 125, 75), 1)
				: BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(150, 125, 75), 1),
						title, 0, 0, GuiTheme.boldText(14f), GuiTheme.PARCHMENT));
		section.add(content, BorderLayout.CENTER);
		return section;
	}

	private JPanel transparentPanel(java.awt.LayoutManager layout) {
		JPanel panel = new JPanel(layout);
		panel.setOpaque(false);
		return panel;
	}

	private void configureRadioButton(JRadioButton button) {
		button.setOpaque(false);
		button.setForeground(GuiTheme.PARCHMENT);
		button.setFont(GuiTheme.boldText(15f));
		button.setHorizontalTextPosition(SwingConstants.CENTER);
		button.setVerticalTextPosition(SwingConstants.BOTTOM);
	}

	private void configureButton(JButton button) {
		button.setFont(GuiTheme.heading(16f));
		button.setPreferredSize(new Dimension(120, 38));
	}
}
