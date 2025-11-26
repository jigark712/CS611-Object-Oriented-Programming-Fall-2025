package legends;

import java.util.*;

import legends.characters.Hero;
import legends.characters.monsters.Monster;
import legends.characters.monsters.Spirit;
import legends.items.Weapon;
import legends.items.Armor;
import legends.items.Potion;
import legends.items.Spell;

import legends.battle.BattleSystem;
import legends.market.MarketSystem;
import legends.world.World;
import legends.world.Tile;
import legends.world.CommonTile;
import legends.world.MarketTile;
import legends.world.InaccessibleTile;

public class LegendsGame {

    // DATA
    private List<Weapon> weapons;
    private List<Armor> armors;
    private List<Potion> potions;

    private List<Spell> fireSpells;
    private List<Spell> iceSpells;
    private List<Spell> lightningSpells;

    private List<Hero> warriors;
    private List<Hero> paladins;
    private List<Hero> sorcerers;

    private List<Monster> dragons;
    private List<Monster> spirits;
    private List<Monster> exoskeletons;

    // GAME STATE
    private static final int WORLD_SIZE = 8;
    private static final int BATTLES_TO_WIN = 3;

    private final Scanner scanner;
    private World world;
    private final List<Hero> party;
    private final Random random = new Random();

    private int battlesWon = 0;
    private boolean gameOver = false;

    public LegendsGame() {
        scanner = new Scanner(System.in);
        party = new ArrayList<>();
        loadTxtFiles();
    }

    private void loadTxtFiles() {
        try {
            weapons         = DataLoader.loadWeapons();
            armors          = DataLoader.loadArmors();
            potions         = DataLoader.loadPotions();

            fireSpells      = DataLoader.loadFireSpells();
            iceSpells       = DataLoader.loadIceSpells();
            lightningSpells = DataLoader.loadLightningSpells();

            warriors        = DataLoader.loadWarriors();
            paladins        = DataLoader.loadPaladins();
            sorcerers       = DataLoader.loadSorcerers();

            dragons         = DataLoader.loadDragons();
            spirits         = DataLoader.loadSpirits();
            exoskeletons    = DataLoader.loadExoskeletons();

        } catch (Exception e) {
            System.out.println("[ERROR] Could not load TXT data: " + e);
        }
    }

    public void start() {
        printIntro();
        createParty();
        initWorld();
        mainLoop();
        System.out.println("Exiting Legends... returning to Game Hub.");
    }

    private void printIntro() {
        System.out.println("=======================================");
        System.out.println("   LEGENDS: MONSTERS AND HEROES");
        System.out.println("=======================================");
        System.out.println("Controls:");
        System.out.println("  W/A/S/D - Move");
        System.out.println("  I       - Party info");
        System.out.println("  M       - Market (only on Market tile)");
        System.out.println("  Q       - Quit game");
        System.out.println();
        System.out.println("Goal: Survive and win " + BATTLES_TO_WIN + " battles!");
        System.out.println();
    }

    // ------------------------------------------------------
    // PARTY CREATION
    // ------------------------------------------------------

    private void createParty() {
        System.out.println("Create your party!");
        int count = readInt("How many heroes? (1–3): ", 1, 3);

        for (int i = 1; i <= count; i++) {
            System.out.println("\nChoose class for hero " + i + ":");
            System.out.println("1) Warrior");
            System.out.println("2) Sorcerer");
            System.out.println("3) Paladin");

            int choice = readInt("> ", 1, 3);

            System.out.print("Enter name for hero " + i + ": ");
            String name = scanner.nextLine().trim();

            Hero h = createHeroFromTxt(choice, name);
            giveStarterEquipment(h);
            party.add(h);

            System.out.println("Created hero: " + h.getName() + ", Lvl " + h.getLevel()
                    + ", HP " + h.getHp() + "/" + h.getMaxHp()
                    + ", MP " + h.getMp() + "/" + h.getMaxMp());
        }

        System.out.println("\nYour party is ready!");
        for (Hero h : party)
            System.out.println("  " + displayHeroShort(h));
    }

    private Hero createHeroFromTxt(int type, String name) {
        List<Hero> pool = switch (type) {
            case 1 -> warriors;
            case 2 -> sorcerers;
            case 3 -> paladins;
            default -> warriors;
        };

        if (pool == null || pool.isEmpty()) {
            System.out.println("[WARN] Hero file missing, using default stat hero.");
            return new Hero(name);
        }

        Hero template = pool.get(random.nextInt(pool.size()));
        try {
            return template.copyWithName(name);
        } catch (NoSuchMethodError e) {
            return new Hero(name);
        }
    }

    private String displayHeroShort(Hero h) {
        return h.getName() + " (Lvl " + h.getLevel() +
                ", HP " + h.getHp() + "/" + h.getMaxHp() +
                ", MP " + h.getMp() + "/" + h.getMaxMp() +
                ", Gold " + h.getGold() + ")";
    }

    private void giveStarterEquipment(Hero h) {
        // starter weapon: cheapest weapon hero can use
        if (weapons != null && !weapons.isEmpty()) {
            Weapon best = null;
            for (Weapon w : weapons) {
                if (w.getRequiredLevel() <= h.getLevel()) {
                    if (best == null || w.getCost() < best.getCost()) {
                        best = w;
                    }
                }
            }
            if (best != null) {
                h.addWeapon(best);
                h.equipWeapon(best);
                System.out.println("  -> Starter weapon given to " + h.getName() + ": " + best.getName());
            }
        }

        // starter potion: just one basic potion if exists
        if (potions != null && !potions.isEmpty()) {
            Potion p = potions.get(0);
            h.addPotion(p);
            System.out.println("  -> Starter potion given to " + h.getName() + ": " + p.getName());
        }
    }

    // ------------------------------------------------------
    // WORLD / MAIN LOOP
    // ------------------------------------------------------

    private void initWorld() {
        world = new World(WORLD_SIZE);
        world.placePartyAtStart();
        System.out.println("\nA new world has been created. Your party starts at the bottom-left corner.\n");
    }

    private void mainLoop() {
        while (!gameOver) {
            world.printWorld();
            printPartyStatus();

            String cmd = readCommand();

            switch (cmd) {
                case "W", "A", "S", "D" -> handleMove(cmd.charAt(0));
                case "I" -> printPartyStatus();
                case "M" -> tryMarket();
                case "Q" -> gameOver = true;
                default -> System.out.println("Invalid command.");
            }
        }
    }

    private String readCommand() {
        System.out.print("Enter command (W/A/S/D, I, M, Q): ");
        return scanner.nextLine().trim().toUpperCase();
    }

    // ------------------------------------------------------
    // MOVEMENT/BATTLES
    // ------------------------------------------------------

    private void handleMove(char dir) {
        Tile prev = world.getCurrentTile();

        if (!world.move(dir)) {
            System.out.println("You cannot move there.");
            return;
        }

        Tile tile = world.getCurrentTile();

        if (tile instanceof CommonTile) {
            System.out.println("You move onto a common tile. It's quiet here... for now.");

            if (random.nextDouble() < 0.28) {
                Monster m = generateTempMonster();
                boolean heroesWon = new BattleSystem(party, m, scanner).runBattle();

                if (!heroesWon) {
                    System.out.println("You have been defeated.");
                    System.out.println("Returning to Game Hub...");
                    gameOver = true;
                    return;
                } else {
                    battlesWon++;
                    if (battlesWon >= BATTLES_TO_WIN) {
                        System.out.println();
                        System.out.println("🎉 CONGRATULATIONS! 🎉");
                        System.out.println("Your party has survived " + battlesWon + " battles and completed the adventure!");
                        System.out.println("Returning to Game Hub...");
                        gameOver = true;
                        return;
                    } else {
                        System.out.println("You have won " + battlesWon + " battle(s) so far. Win "
                                + (BATTLES_TO_WIN - battlesWon) + " more to complete the adventure!");
                    }
                }
            }

        } else if (tile instanceof MarketTile) {
            System.out.println("You step onto a Market tile. Press 'M' to enter.");
        } else if (tile instanceof InaccessibleTile) {
            System.out.println("BUG: You moved inside an inaccessible tile.");
        }
    }

    private void tryMarket() {
        if (!(world.getCurrentTile() instanceof MarketTile)) {
            System.out.println("You are not standing on a market tile.");
            return;
        }

        if (weapons == null || potions == null) {
            System.out.println("Market data not loaded.");
            return;
        }

        MarketSystem.openMarket(scanner, party, weapons, armors, potions,
                fireSpells, iceSpells, lightningSpells);
    }

    private Monster generateTempMonster() {
        List<Monster> pool = new ArrayList<>();
        if (dragons != null) pool.addAll(dragons);
        if (spirits != null) pool.addAll(spirits);
        if (exoskeletons != null) pool.addAll(exoskeletons);

        Monster base;
        if (pool.isEmpty()) {
            base = new Spirit("TempSpirit", 1, 50, 20, 20);
        } else {
            base = pool.get(random.nextInt(pool.size()));
        }

        try {
            int maxHeroLevel = party.stream().mapToInt(Hero::getLevel).max().orElse(1);
            base.scaleToLevel(maxHeroLevel);
        } catch (NoSuchMethodError ignored) {}

        System.out.println();
        System.out.println("=================================");
        System.out.println("   A wild " + base.getName() + " appears!");
        System.out.println("=================================\n");

        return base;
    }

    private void printPartyStatus() {
        System.out.println("\n=== Party Status ===");
        for (Hero h : party) {
            System.out.println(h.getName() +
                    "  |  Lvl " + h.getLevel() +
                    "  |  HP " + h.getHp() + "/" + h.getMaxHp() +
                    "  |  MP " + h.getMp() + "/" + h.getMaxMp() +
                    "  |  Gold " + h.getGold());
        }
        System.out.println("====================\n");
    }

    private int readInt(String msg, int min, int max) {
        while (true) {
            System.out.print(msg);
            String s = scanner.nextLine().trim();

            try {
                int val = Integer.parseInt(s);
                if (val >= min && val <= max)
                    return val;
            } catch (Exception ignored) {}

            System.out.println("Enter a number between " + min + " and " + max + ".");
        }
    }
}
