# Description
This document goes over the game rules and how the game works from turn to turn.

## Definitions, Basic
- This game is for 2 to 4 players.
- This game is a digital version of a board game.
- The number of players is selected at the start of the game.
- Each player selects an unselected color from: Red, Blue, Green, Yellow.
- Turn order is determined randomly at the start of the game, and then is kept for each round afterward.
- Each player starts at the pit (space 0) of dungeon level 1, and EXP level of 0.
- The monster indexes and Sneech power levels are assigned to their proper spaces randomly at the start of the game, and do not change.
    - The quantity of each specific monster index by dungeon level -OR- Sneech Power level are given in the game terms file.
- Combat and movement both use the 2d6 dice roll. A separate number is generated for each movement or combat action.
    
## Turn order
- The turn player determines whether to proceed right or left along the dungeon path for the level they're in.
- The turn player rolls the dice and moves that many spaces around the path. Remember that the path of a dungeon level is circular.
    - Moving from the pit onto the path takes 1 movement point, and lands at position 1, regardless of if the player moves left or right that turn.
    - Example: From the pit, a roll of 2 lands on position 2 when moving right and position 9 when moving left.
- The turn player then checks the space where they landed
    - On a normal Dungeon Level, the player encounters the monster whose index was assigned to the landed space during game setup.
    - In the Sneech Lair level, this space will show a number that represents the Sneech power in that space.
- The encountered monster/Sneech is displayed in the GUI, along with the current power level being faced. The player determines whether to fight or to run.
    - If they run, their turn ends immediately.
    - If they fight, then they roll the dice and add their current EXP level.
    - If this total is equal to or greater than the monster's/Sneech's power, then the player wins
        - The player, upon winning, adds the EXP reward for that monster to their total, though never more than 5.
        - If the player's EXP is 5 or more, then they are immediately promoted to the next dungeon level.
            - Promotion means being placed at the "pit" (space 0) of the next dungeon level.
            - When a player is promoted to a normal dungeon level, their EXP is reset to 0.
            - When a player is promoted to the Sneech Lair, their EXP stays at 5.
    - If the player loses, then the EXP penalty for that monster is subtracted from their current EXP total, though never less than 0.
        - A player does NOT get demoted down dungeon levels for losing too much EXP.
        - A player does NOT lose EXP for losing a fight with the Sneech.
    - After winning or losing or running, the player's turn ends.
- If a player has defeated the Sneech, then they win the game. 
    - The game does not immediately end. Other players who have not finished their turn for that round get a chance to finish their turn.
    - Only at the end of the current numbered round does the game end. Any player who defeated the Sneech wins.
        - For example, suppose we're on round 15, and the turn order is Red -> Yellow -> Blue -> Green. If Yellow defeats the Sneech, then Blue and Green still get a turn to try to defeat the Sneech. Red does not, because they already had their turn 15. After all players have had their turn, round 15 ends. 
