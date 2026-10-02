# Description
This document contains definitions for various objects and terms in the game.

## Definitions
- A round consists of one turn for every player in the fixed turn order. The first round is round 1.
- Player: Is Controlled by a human.
    - Has several stats. Exp level, dungeon level, position on level.
    - Represented by a color: Red, blue, yellow, or green.
- Sneech: The end boss monster of the game. The goal is to defeat the Sneech in the Sneech Lair.
    - The Sneech lives only in level 4, the Sneech Lair and is not randomly encountered.
- Dice Roll: 2d6; i.e. a random integer generated from 1 to 6, twice, then adding the totals.
- EXP level: A player's 'score' on a given dungeon level. 
    - EXP ranges from 0 to 5.
    - When a player reaches 5 EXP in a normal dungeon level, they are promoted exactly one Dungeon Level.
    - EXP resets to 0 when promoted to Dungeon Level 2 or 3 and remains 5 when promoted to the Sneech Lair. Any excess EXP is discarded.
- Dungeon level: Ranges from 1 to 3 for normal dungeons. 
    - Dungeon Levels each have a graphical theme.
        - Dungeon level 1's theme is Castle Ruins.
        - Dungeon level 2's theme is bramble patch.
        - Dungeon level 3's theme is Swamp bog.
    - Dungeon levels have positions for the player. (See Position on Level)
    - Dungeon Levels 1 to 3 are normal dungeon levels.
    - The 4th, the Sneech Lair, is a special dungeon level.
- Position on level: Which space the player's character occupies on that dungeon floor.
    - Multiple players are allowed to occupy the same position at the same time with no consequence.
    - Players do not fight each other.
    - Each level starts at position 0, the center.
        - The Start space always leads to space 1, no matter if the player goes clockwise or counter-clockwise.
        - Moving out of the Start space takes a movement point.
    - The Path for that level consists of 9 spaces, arranged around the perimeter of the level.
        - The Path spaces are indexed from 1 to 9.
        - If a player would move clockwise from 9, then they loop around to 1.
        - If a player would move counter-clockwise from 1, then they loop around to 9.
        - A player may not move onto the Start space (space 0).
    - For normal dungeon levels, the spaces are marked with a monster index. The index determines what monster the player encounters when landing on that space.
        - Dungeon level 1 has monster indexes: A, A, B, B, C, C, D, E, F
        - Dungeon level 2 has monster indexes: A, B, B, C, C, D, D, E, F
        - Dungeon level 3 has monster indexes: A, B, C, C, D, D, E, E, F
    - For the Sneech Lair, the spaces are marked with a numeral representing the Sneech Power on that position.
        - The positions of the Sneech Lair are marked with Sneech powers: 12, 12, 13, 13, 14, 14, 15, 15, 15
- Monster: Opponents that the players meet during the adventure.
    - Have a monster index, and a power level determined in part by the dungeon and in part by the monster type.
        - Monster power is equal to their base type power + dungeon level. 
            - E.g. a Slime (index A) encountered on level 2 would be 4 (base) + 2 (dungeon level) = 6
    - Random Monsters encountered:
        - A: Slime: Power = 4, EXP: Win: +1, Loss: -0
        - B: Skeleton: Power = 5, EXP: Win: +1, Loss: -0
        - C: Goblin: Power = 6, EXP: Win: +1, Loss: -1
        - D: Zombie: Power = 7, EXP: Win: +1, Loss: -1
        - E: Ogre: Power = 8, EXP: Win: +2, Loss: -2
        - F: Dark Knight: Power = 9, EXP: Win: +2, Loss: -1


