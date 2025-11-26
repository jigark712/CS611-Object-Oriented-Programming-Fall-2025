package legends;

import legends.characters.Hero;
import legends.characters.monsters.*;
import legends.items.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    // Adjust this path to where your txt files live inside the project
    private static final String ROOT = "legends/txt_data/";

    // --------------------------------------------------
    // generic file reader
    // --------------------------------------------------
    private static List<String[]> readFile(String filename) throws IOException {
        List<String[]> rows = new ArrayList<>();
        Path path = Paths.get(ROOT + filename);

        if (!Files.exists(path)) {
            System.out.println("[WARN] Missing file: " + filename);
            return rows;
        }

        for (String line : Files.readAllLines(path)) {
            line = line.trim();
            if (line.isEmpty()) continue;
            if (line.startsWith("#")) continue;
            if (line.toLowerCase().contains("name") || line.contains("/")) continue;

            String[] parts = line.split("\\s+");
            rows.add(parts);
        }
        return rows;
    }

    // --------------------------------------------------
    // WEAPONS
    // --------------------------------------------------
    public static List<Weapon> loadWeapons() {
        List<Weapon> list = new ArrayList<>();
        try {
            for (String[] p : readFile("Weaponry.txt")) {
                String name   = p[0];
                int cost      = Integer.parseInt(p[1]);
                int reqLevel  = Integer.parseInt(p[2]);
                int damage    = Integer.parseInt(p[3]);
                int hands     = Integer.parseInt(p[4]);
                list.add(new Weapon(name, cost, reqLevel, damage, hands));
            }
        } catch (Exception e) {
            System.out.println("[ERROR] loadWeapons(): " + e);
        }
        return list;
    }

    // --------------------------------------------------
    // ARMORS
    // --------------------------------------------------
    public static List<Armor> loadArmors() {
        List<Armor> list = new ArrayList<>();
        try {
            for (String[] p : readFile("Armory.txt")) {
                String name  = p[0];
                int cost     = Integer.parseInt(p[1]);
                int reqLevel = Integer.parseInt(p[2]);
                int reduce   = Integer.parseInt(p[3]);
                list.add(new Armor(name, cost, reqLevel, reduce));
            }
        } catch (Exception e) {
            System.out.println("[ERROR] loadArmors(): " + e);
        }
        return list;
    }

    // --------------------------------------------------
    // POTIONS
    // --------------------------------------------------
    public static List<Potion> loadPotions() {
        List<Potion> list = new ArrayList<>();
        try {
            for (String[] p : readFile("Potions.txt")) {
                String name    = p[0];
                int cost       = Integer.parseInt(p[1]);
                int reqLevel   = Integer.parseInt(p[2]);
                int increase   = Integer.parseInt(p[3]);
                String affected = p[4];
                list.add(new Potion(name, cost, reqLevel, increase, affected));
            }
        } catch (Exception e) {
            System.out.println("[ERROR] loadPotions(): " + e);
        }
        return list;
    }

    // --------------------------------------------------
    // SPELLS
    // --------------------------------------------------
    private static Spell makeSpell(String[] p, Spell.School school) {
        String name  = p[0];
        int cost     = Integer.parseInt(p[1]);
        int reqLevel = Integer.parseInt(p[2]);
        int damage   = Integer.parseInt(p[3]);
        int mana     = Integer.parseInt(p[4]);
        return switch (school) {
            case FIRE      -> new FireSpell(name, cost, reqLevel, damage, mana);
            case ICE       -> new IceSpell(name, cost, reqLevel, damage, mana);
            case LIGHTNING -> new LightningSpell(name, cost, reqLevel, damage, mana);
        };
    }

    public static List<Spell> loadFireSpells() {
        List<Spell> list = new ArrayList<>();
        try {
            for (String[] p : readFile("FireSpells.txt")) {
                list.add(makeSpell(p, Spell.School.FIRE));
            }
        } catch (Exception e) {
            System.out.println("[ERROR] loadFireSpells(): " + e);
        }
        return list;
    }

    public static List<Spell> loadIceSpells() {
        List<Spell> list = new ArrayList<>();
        try {
            for (String[] p : readFile("IceSpells.txt")) {
                list.add(makeSpell(p, Spell.School.ICE));
            }
        } catch (Exception e) {
            System.out.println("[ERROR] loadIceSpells(): " + e);
        }
        return list;
    }

    public static List<Spell> loadLightningSpells() {
        List<Spell> list = new ArrayList<>();
        try {
            for (String[] p : readFile("LightningSpells.txt")) {
                list.add(makeSpell(p, Spell.School.LIGHTNING));
            }
        } catch (Exception e) {
            System.out.println("[ERROR] loadLightningSpells(): " + e);
        }
        return list;
    }

    // --------------------------------------------------
    // HEROES
    // --------------------------------------------------
    private static Hero makeHero(String[] p) {
        String name = p[0];
        int mana   = Integer.parseInt(p[1]);
        int str    = Integer.parseInt(p[2]);
        int agi    = Integer.parseInt(p[3]);
        int dex    = Integer.parseInt(p[4]);
        int money  = Integer.parseInt(p[5]);
        int exp    = Integer.parseInt(p[6]);
        return new Hero(name, mana, str, agi, dex, money, exp);
    }

    private static List<Hero> loadHeroFile(String file) {
        List<Hero> list = new ArrayList<>();
        try {
            for (String[] p : readFile(file)) {
                list.add(makeHero(p));
            }
        } catch (Exception e) {
            System.out.println("[ERROR] loading " + file + ": " + e);
        }
        return list;
    }

    public static List<Hero> loadWarriors()  { return loadHeroFile("Warriors.txt"); }
    public static List<Hero> loadPaladins()  { return loadHeroFile("Paladins.txt"); }
    public static List<Hero> loadSorcerers() { return loadHeroFile("Sorcerers.txt"); }

    // --------------------------------------------------
    // MONSTERS
    // --------------------------------------------------
    private static Monster makeMonster(String[] p, String type) {
        String name = p[0];
        int level   = Integer.parseInt(p[1]);
        int damage  = Integer.parseInt(p[2]);
        int defense = Integer.parseInt(p[3]);
        int dodge   = Integer.parseInt(p[4]);
        return switch (type) {
            case "dragon" -> new Dragon(name, level, damage, defense, dodge);
            case "spirit" -> new Spirit(name, level, damage, defense, dodge);
            case "exo"    -> new Exoskeleton(name, level, damage, defense, dodge);
            default -> null;
        };
    }

    private static List<Monster> loadMonsterFile(String file, String type) {
        List<Monster> list = new ArrayList<>();
        try {
            for (String[] p : readFile(file)) {
                Monster m = makeMonster(p, type);
                if (m != null) list.add(m);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] loading " + file + ": " + e);
        }
        return list;
    }

    public static List<Monster> loadDragons()      { return loadMonsterFile("Dragons.txt", "dragon"); }
    public static List<Monster> loadSpirits()      { return loadMonsterFile("Spirits.txt", "spirit"); }
    public static List<Monster> loadExoskeletons() { return loadMonsterFile("Exoskeletons.txt", "exo"); }
}
