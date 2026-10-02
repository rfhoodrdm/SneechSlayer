# Description
This document describes the flow of the game from turn to turn, as well as setup concerns.

## To Reset Game Stats:
- Any visible players' positions start at Dungeon Level 1, In the Level 1 Start space.
- Any visible EXP level is set to 0.
- Set the round counter to 1.
- Randomize the turn order of the player colors. This turn order is retained for the rest of the game.
- Randomize positions of monster indexes for dungeon spaces for Dungeon Levels 1, 2, and 3.
- Randomize positions of Sneech Power levels in spaces for the Sneech Lair.

## Initial Game State on Application Startup
- Enter state NO_GAME
- Show "Select New Game To Start" in the Instruction widget in the control panel.
- You may display the four potentially active player pips for the initial state.
    -- assume all players are at Dungeon level 1, start space, and all players have EXP level of 0.
- Set Settings Menu -> Music menu option to checked.
- Set Settings Menu -> Sound Effects option to checked.

### On New Game:
- Show prompt to user to select number of players
    - If this dialog is cancelled, do not reset the game state.
- For each player, show prompt to select a player color for that player
    - If this dialog is cancelled, do not reset the game state.
- reset game stats. 
- Start the turn for the first player in turn order, and continue with game flow until the game is concluded or a New Game is started.
    - Enter state AWAITING_DIRECTION

## Game states:
You may use the following states for the game:
  1. NO_GAME
    - direction, Move, fight, and run controls disabled.
  2. AWAITING_DIRECTION
    - directions are enabled; Move control is disabled if no direction is selected, but is enabled after movement direction selected. Fight and Run disabled.
  3. MOVING
    - Move, fight, and run controls disabled. direction highlighted, but no action on click.
  4. AWAITING_COMBAT_CHOICE
    - direction and Move controls disabled; Fight and Run enabled.
  5. RESOLVING_COMBAT
    - direction, Move, fight, and run controls disabled.
  6. GAME_OVER
    - direction, Move, fight, and run controls disabled.

## One Player's Turn: 
- Highlight the dungeon level space which the player currently occupies at the start of the turn.
- Show the player's color pip in the turn player display widget in the control panel.
- Show "Choose movement direction to roll movement" in the instruction widget.
- The player selects either the clockwise or counter-clockwise movement option. 
- When the player clicks the Move button, movement dice are then rolled. Show the results corresponding to the roll as the dice faces with the correct number of pips for each face.
    - Play the dice roll sound effect.
- Move the character's pip either clockwise or counterclockwise along the dungeon level path for the level they're on. 
    - Show 1 space moved at a time.
    - Have a 0.25 second delay between movements, to allow the player to follow the movement.
    - After each movement, highlight the border for the space in which the player currently occupies and de-highlight the border for all other spaces.
- For dungeon levels 1 to 3, reference which monster index is assigned to that space presently.
    - Highlight the corresponding monster entry in the monster gallery widget.
- Show "Select Fight or Run" in the instruction widget.
    - If the player selects to run, play the player_runs.aif sound effect.
        - Then wait 1 second.
    - If the player selects to fight, roll the dice and add the player's exp total. That is the player's combat score for this turn.
        - Play the dice_roll.aif sound effect.
        - The dice faces are replaced with the results of the player's combat dice roll.
        - Wait 1 second.
        - Determine the enemy combat score. 
            - For levels 1 to 3, the enemy combat score is the encountered monster power for that given dungeon level.
            - For the Sneech Lair, the enemy combat score is the Sneech power corresponding to that space.
        - If the player's combat score is equal to or higher than the enemy combat score, then the player wins the combat.
            - Play the player_attacks.aif sound effect.
            - For dungeon levels 1, 2, or 3, Add the monster's exp reward to the player's EXP level, not exceeding 5. 
                - If the player is in dungeon level 1, 2, or 3 and the player's EXP total is 5, then perform a promotion to the next dungeon level.
            - For the Sneech Lair, set that player's victory flag to true.
        - If instead the player's combat score is lower than the enemy combat score, then the player loses that combat.
            - Play the monster_growl.aif sound effect.
            - For dungeon levels 1, 2, or 3, deduct the monster's exp penalty from the player's EXP level, not going below 0.
            - Nothing happens if a player fails to defeat the Sneech.
    - Then wait 1 second.
    - Perform turn cleanup:
        - De-highlight any highlighted monster gallery widgets.
        - De-select any movement directions that were chosen this turn.
    - If all players have had their turn this round: 
        - If at least one player has their victory flag set:
            - then those players win. Multiple players may win simultaneously
            - Display a dialog box announcing the winners.
            - Proceed to GAME_OVER state. 
        - if no player has won:
            - increment the round counter by 1, then allow each player a new turn in turn order.
    - If not all players have had their turn, proceed to the next player's turn.
              

### Promoting to the next dungeon level:
- Place the player's pip in the Start space of the next dungeon level.
- If promoting to dungeon level 2 or dungeon level 3, the player's EXP total resets to 0.
- If promoting to the Sneech Lair, EXP stays at 5.
- Play the victory_fanfare.aif sound effect.
- No monster or movement happens on promotion. Normally this is the last thing that happens on a player's turn.

### Example of movement:
- Clockwise path: 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 7 -> 8 -> 9 -> 1
- Counter-clockwise path: 1 -> 9 -> 8 -> 7 -> 6 -> 5 -> 4 -> 3 -> 2 -> 1 -> 9
- Movement from Start goes to space 1, regardless of clockwise or counter-clockwise movement.
- Example: Suppose player rolls 3 movement points and is at the Start space.
    - Clockwise movement: Player moves from Start -> 1 -> 2 -> 3.
    - Counter-clockwise movement: Player moves from Start -> 1 -> 9 -> 8


