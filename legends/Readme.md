Legends: Monsters and Heroes
CS611 

Name: Jigar Kanakhara
Email: jigar@bu.edu
Student ID: U21762912

1. File Structure

The project is organized into packages according to the specification.

app/
    Main.java
    GameHub.java

legends/
    LegendsGame.java
    DataLoader.java

    battle/
        BattleSystem.java

    characters/
        Hero.java

        monsters/
            Monster.java
            Dragon.java
            Spirit.java
            Exoskeleton.java

    items/
        Item.java
        Weapon.java
        Spell.java
        FireSpell.java
        IceSpell.java
        LightningSpell.java
        Potion.java
        Armor.java

    market/
        Market.java
        MarketSystem.java

    world/
        Tile.java
        TileType.java
        CommonTile.java
        MarketTile.java
        InaccessibleTile.java
        World.java

txt_data/
    Warriors.txt
    Paladins.txt
    Sorcerers.txt
    Dragons.txt
    Spirits.txt
    Exoskeletons.txt
    Armory.txt
    Weapons.txt
    IceSpells.txt
    FireSpells.txt
    LightningSpells.txt
    Potions.txt

2. How to Compile and Run (Terminal Instructions)

All commands are executed from the project root folder (the folder containing app/ and legends/).

Step 1 — Compile all Java files
javac app/Main.java


If your system requires preview mode:

javac --enable-preview app/Main.java


If you prefer compiling the whole directory manually:

javac $(find . -name "*.java")

Step 2 — Run the program
java app.Main


Or, if using preview mode:

java --enable-preview app.Main


This launches the Game Hub menu.

3. Overview

This project implements a complete version of Legends: Monsters and Heroes, a turn-based role-playing game with exploration, battle, markets, and leveling.

The Game Hub lets you select between multiple games, including Sliding Puzzle, Dots & Boxes, Quoridor, and this RPG.

The RPG contains:

A dynamically generated 8×8 world map

Market tiles allowing purchase of weapons, spells, and potions

Turn-based combat against random monsters

Balanced hero and monster stats

Weapon selection each turn

Fair, stable combat without regeneration

Monster and hero both using 300 HP

Consistent damage ranging from approximately 60–90 for heroes and 30–45 for monsters

Victory condition: win 3 battles

4. Input Text Files

All hero, monster, item, and spell stats are loaded from the .txt files in txt_data/.
These files follow the standard format provided in the course assignment.

Examples:

Warriors.txt, Paladins.txt, Sorcerers.txt

Dragons.txt, Spirits.txt, Exoskeletons.txt

Weapons.txt, Armory.txt

FireSpells.txt, IceSpells.txt, LightningSpells.txt

Potions.txt

The loader (DataLoader.java) parses each file and creates class objects automatically.

5. Game Controls

During exploration:

W, A, S, D – Move

I – Show party information

M – Enter market (only if standing on a $ tile)

Q – Quit the game

During battle:

1 – Attack using weapon

2 – Cast spell

3 – Use potion

Each turn, the hero may choose a weapon from their inventory before attacking.

6. Sample Gameplay Output

Below is a shortened sample of normal expected behavior.

=================================
   A wild Andrealphus appears!
=================================

--- STATUS ---
Monster: Andrealphus  HP: 300/300
Party:
  es  Lvl 1  HP 300/300  MP 900/900  Gold 2500
--------------

--- es's turn ---
1) Attack
2) Cast spell
3) Use potion
> 1
es equipped weapon Dagger
es hits Andrealphus for 67! (233/300)
Andrealphus hits es for 15! (285/300)


Battle continues until victory or defeat.

7. Notes

Monster HP is fixed at 300.

Hero HP is fixed at 300.

Monster damage is carefully balanced to around 40 per hit.

Hero damage depends on weapon stats and hero strength.

All systems have been simplified and stabilized for consistent gameplay.

No regeneration occurs after each round.

Weapons and potions can be acquired from markets using gold.

Gold is rewarded after each victory.