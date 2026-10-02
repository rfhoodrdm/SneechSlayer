package com.rfhoodrdm.sneechslayer.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.rfhoodrdm.sneechslayer.state.DungeonLevel;
import com.rfhoodrdm.sneechslayer.state.MonsterType;

/**
 * One entry in the two-by-three monster bestiary.
 */
final class MonsterCard extends JPanel {

	private static final long serialVersionUID = 1L;

	private final MonsterType monsterType;

	MonsterCard(MonsterType monsterType, BufferedImage sprite) {
		this.monsterType = monsterType;
		setLayout(new GridBagLayout());
		setOpaque(true);
		setBackground(new Color(15, 14, 13, 215));
		TitledBorder title = BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.GRAY, 2), monsterType.getDisplayName());
		title.setTitleColor(GuiTheme.PARCHMENT);
		title.setTitleFont(GuiTheme.boldText(15f));
		setBorder(title);

		addSprite(sprite);
		addValue("+" + monsterType.getExperienceReward() + " EXP", 2, 0, GuiTheme.PARCHMENT);
		addValue("-" + monsterType.getExperiencePenalty() + " EXP", 2, 1, GuiTheme.PARCHMENT);
		addValue(Integer.toString(monsterType.powerOn(DungeonLevel.CASTLE_RUINS)), 0, 2,
				GuiTheme.LEVEL_ONE);
		addValue(Integer.toString(monsterType.powerOn(DungeonLevel.BRAMBLE_PATCH)), 1, 2,
				GuiTheme.LEVEL_TWO);
		addValue(Integer.toString(monsterType.powerOn(DungeonLevel.SWAMP_BOG)), 2, 2,
				GuiTheme.LEVEL_THREE);
	}

	MonsterType getMonsterType() {
		return monsterType;
	}

	void setEncountered(boolean encountered) {
		TitledBorder title = BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(encountered ? new Color(230, 80, 35) : Color.GRAY,
						encountered ? 4 : 2), monsterType.getDisplayName());
		title.setTitleColor(GuiTheme.PARCHMENT);
		title.setTitleFont(GuiTheme.boldText(15f));
		setBorder(title);
	}

	private void addSprite(BufferedImage sprite) {
		GridBagConstraints constraints = baseConstraints(0, 0);
		constraints.gridwidth = 2;
		constraints.gridheight = 2;
		constraints.weightx = 2;
		constraints.weighty = 2;
		constraints.fill = GridBagConstraints.BOTH;
		add(new ScaledImage(sprite, 100, 76), constraints);
	}

	private void addValue(String text, int column, int row, Color color) {
		JLabel label = new JLabel(text, SwingConstants.CENTER);
		label.setForeground(color);
		label.setFont(GuiTheme.boldText(13f));
		label.setBorder(BorderFactory.createLineBorder(new Color(130, 125, 115, 120)));

		GridBagConstraints constraints = baseConstraints(column, row);
		constraints.fill = GridBagConstraints.BOTH;
		add(label, constraints);
	}

	private GridBagConstraints baseConstraints(int column, int row) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = column;
		constraints.gridy = row;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.insets = new Insets(1, 1, 1, 1);
		return constraints;
	}
}
