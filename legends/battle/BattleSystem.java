package legends.battle;

import legends.characters.Hero;
import legends.characters.monsters.Monster;
import legends.items.Weapon;
import legends.items.Spell;
import legends.items.Potion;

import java.util.List;
import java.util.Scanner;

/**
 * Simple, fair BattleSystem:
 * - No regen
 * - Hero & monster both do reasonable damage
 * - HP is shown every round
 */
public class BattleSystem {

    private final List<Hero> party;
    private final Monster monster;
    private final Scanner scanner;

    public BattleSystem(List<Hero> party, Monster monster, Scanner scanner) {
        this.party = party;
        this.monster = monster;
        this.scanner = scanner;
    }

    // ============================================================
    // MAIN LOOP
    // ============================================================
    public boolean runBattle() {
        System.out.println("\n------ BATTLE START ------\n");
        printStatus();

        while (monster.isAlive() && anyHeroAlive()) {

            // HERO TURNS
            for (Hero h : party) {
                if (!monster.isAlive()) break;
                if (h.isAlive()) {
                    heroTurn(h);
                }
            }

            if (!monster.isAlive()) {
                System.out.println("\n=== VICTORY ===");
                System.out.println("You defeated " + monster.getName() + "!");
                System.out.println("==============\n");
                return true;
            }

            // MONSTER TURN
            monsterTurn();

            if (!anyHeroAlive()) {
                System.out.println("\n=== DEFEAT ===");
                System.out.println("You have been defeated.");
                System.out.println("Returning to Game Hub...");
                System.out.println("==============\n");
                return false;
            }

            printStatus();
        }

        return monster.isAlive() ? false : true;
    }

    // ============================================================
    // HERO TURN
    // ============================================================
    private void heroTurn(Hero hero) {
        while (true) {
            System.out.println("\n--- " + hero.getName() + "'s turn ---");
            System.out.println("1) Attack");
            System.out.println("2) Cast spell");
            System.out.println("3) Use potion");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    weaponAttack(hero);
                    return;
                }
                case "2" -> {
                    spellAttack(hero);
                    return;
                }
                case "3" -> {
                    usePotion(hero);
                    return;
                }
                default -> System.out.println("Enter 1, 2, or 3.");
            }
        }
    }

    // ============================================================
    // WEAPON ATTACK (with optional weapon choice)
    // ============================================================
    private void weaponAttack(Hero hero) {
        // If hero has multiple weapons, allow swap; if only one, just use it.
        Weapon newWeapon = chooseWeapon(hero);
        if (newWeapon != null) {
            hero.equipWeapon(newWeapon);
        }

        // Monster dodge
        if (Math.random() < monster.getDodgeChance()) {
            System.out.println(monster.getName() + " dodged!");
            return;
        }

        // Hero damage
        int raw = hero.computePhysicalDamage();

        int beforeHp = monster.getHp();
        monster.takeDamage(raw);
        int afterHp = monster.getHp();
        int effective = beforeHp - afterHp;
        if (effective < 0) effective = 0;

        System.out.println(hero.getName() + " hits " + monster.getName() +
                " for " + effective + "! (" + monster.getHp() + "/" + monster.getMaxHp() + ")");
    }

    // ============================================================
    // SPELL ATTACK
    // ============================================================
    private void spellAttack(Hero hero) {
        if (!hero.hasSpells()) {
            System.out.println("You have no spells.");
            return;
        }

        Spell spell = chooseSpell(hero);
        if (spell == null) return;

        if (hero.getMp() < spell.getManaCost()) {
            System.out.println("Not enough mana!");
            return;
        }

        // Monster dodge
        if (Math.random() < monster.getDodgeChance()) {
            System.out.println(monster.getName() + " dodged the spell!");
            hero.setMp(hero.getMp() - spell.getManaCost());
            return;
        }

        int dmg = hero.computeSpellDamage(spell);
        hero.setMp(hero.getMp() - spell.getManaCost());

        int beforeHp = monster.getHp();
        monster.takeDamage(dmg);
        int afterHp = monster.getHp();
        int effective = beforeHp - afterHp;
        if (effective < 0) effective = 0;

        System.out.println(hero.getName() + " casts " + spell.getName() +
                " for " + effective + " damage! (" +
                monster.getHp() + "/" + monster.getMaxHp() + ")");
    }

    // ============================================================
    // USE POTION
    // ============================================================
    private void usePotion(Hero hero) {
        if (!hero.hasPotions()) {
            System.out.println(hero.getName() + " has no potions.");
            return;
        }

        List<Potion> ps = hero.getPotions();
        System.out.println("\nChoose potion:");
        for (int i = 0; i < ps.size(); i++) {
            Potion p = ps.get(i);
            System.out.printf("%d) %s (+%d %s)%n",
                    i + 1, p.getName(), p.getAttributeIncrease(), p.getAttributeAffected());
        }
        System.out.println("0) Cancel");
        System.out.print("> ");

        int idx;
        try {
            idx = Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            return;
        }

        if (idx <= 0 || idx > ps.size()) return;

        hero.usePotion(idx - 1);
    }

    // ============================================================
    // MONSTER TURN (balanced damage)
    // ============================================================
    private void monsterTurn() {
        if (!monster.isAlive()) return;

        Hero target = firstAliveHero();
        if (target == null) return;

        // Hero dodge
        if (Math.random() < target.getDodgeChance()) {
            System.out.println(target.getName() + " dodged the attack!");
            return;
        }

        int dmg = computeMonsterDamage();
        target.takeDamage(dmg);

        System.out.println(monster.getName() + " hits " + target.getName() +
                " for " + dmg + "! (" + target.getHp() + "/" + target.getMaxHp() + ")");
    }

    // ------------------------------------------------------------
    // Balanced monster damage:
    // baseDamage is usually huge in the txt, so we scale it way down
    // ------------------------------------------------------------
    private int computeMonsterDamage() {

    // Monster damage centered around ~40
    int dmg = 40;

    // Small ±15% jitter
    int jitter = (int) (dmg * 0.15);  // → 6
    int finalDmg = dmg - jitter + (int)(Math.random() * (2 * jitter + 1));

    // clamp between 35–45
    if (finalDmg < 35) finalDmg = 35;
    if (finalDmg > 45) finalDmg = 45;

    return finalDmg;
}


    // ============================================================
    // HELPERS
    // ============================================================
    private boolean anyHeroAlive() {
        for (Hero h : party) {
            if (h.isAlive()) return true;
        }
        return false;
    }

    private Hero firstAliveHero() {
        for (Hero h : party) {
            if (h.isAlive()) return h;
        }
        return null;
    }

    private void printStatus() {
        System.out.println("\n--- STATUS ---");
        System.out.println("Monster: " + monster.getName() +
                "  HP: " + monster.getHp() + "/" + monster.getMaxHp());
        System.out.println("Party:");
        for (Hero h : party) {
            System.out.println("  " + h.getName() +
                    "  Lvl " + h.getLevel() +
                    "  HP " + h.getHp() + "/" + h.getMaxHp() +
                    "  MP " + h.getMp() + "/" + h.getMaxMp() +
                    "  Gold " + h.getGold());
        }
        System.out.println("--------------\n");
    }

    // Weapon picker: if only 1 weapon, we just use it.
    private Weapon chooseWeapon(Hero hero) {
        List<Weapon> ws = hero.getWeapons();
        if (ws.isEmpty()) return hero.getEquippedWeapon();

        if (ws.size() == 1) {
            // Only one available, no need to ask every time
            return ws.get(0);
        }

        System.out.println("\nChoose weapon:");
        for (int i = 0; i < ws.size(); i++) {
            Weapon w = ws.get(i);
            System.out.println((i + 1) + ") " + w.getName() +
                    " (DMG=" + w.getDamage() + ")");
        }
        System.out.println("0) Keep current");
        System.out.print("> ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            return null;
        }

        if (choice == 0) return null;
        if (choice < 1 || choice > ws.size()) return null;

        return ws.get(choice - 1);
    }

    private Spell chooseSpell(Hero hero) {
        List<Spell> ss = hero.getSpells();
        if (ss.isEmpty()) return null;

        System.out.println("\nChoose spell:");
        for (int i = 0; i < ss.size(); i++) {
            Spell s = ss.get(i);
            System.out.println((i + 1) + ") " + s.getName() +
                    " (DMG=" + s.getDamage() +
                    ", Mana=" + s.getManaCost() + ")");
        }
        System.out.println("0) Cancel");
        System.out.print("> ");

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            return null;
        }

        if (choice <= 0 || choice > ss.size()) return null;
        return ss.get(choice - 1);
    }
}
