package legends.market;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import legends.characters.Hero;
import legends.items.Armor;
import legends.items.Potion;
import legends.items.Spell;
import legends.items.Weapon;

/**
 * Text-based market system.
 *
 * Called from LegendsGame:
 *   MarketSystem.openMarket(scanner, party, weapons, armors, potions,
 *                           fireSpells, iceSpells, lightningSpells);
 *
 * Features:
 *   - View party
 *   - Buy weapons / potions / spells (armors included for completeness)
 *   - Equip weapons / armor
 */
public class MarketSystem {

    public static void openMarket(
            Scanner scanner,
            List<Hero> party,
            List<Weapon> weapons,
            List<Armor> armors,
            List<Potion> potions,
            List<Spell> fireSpells,
            List<Spell> iceSpells,
            List<Spell> lightningSpells
    ) {
        if (party == null || party.isEmpty()) {
            System.out.println("No heroes in party.");
            return;
        }

        List<Spell> allSpells = new ArrayList<>();
        if (fireSpells != null)      allSpells.addAll(fireSpells);
        if (iceSpells != null)       allSpells.addAll(iceSpells);
        if (lightningSpells != null) allSpells.addAll(lightningSpells);

        boolean done = false;
        while (!done) {
            System.out.println("\n=== MARKET ===");
            System.out.println("1) View party");
            System.out.println("2) Buy weapons");
            System.out.println("3) Buy armors");
            System.out.println("4) Buy potions");
            System.out.println("5) Buy spells");
            System.out.println("6) Equip weapon");
            System.out.println("7) Equip armor");
            System.out.println("0) Exit market");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> showParty(party);
                case "2" -> buyWeapon(scanner, party, weapons);
                case "3" -> buyArmor(scanner, party, armors);
                case "4" -> buyPotion(scanner, party, potions);
                case "5" -> buySpell(scanner, party, allSpells);
                case "6" -> equipWeaponMenu(scanner, party);
                case "7" -> equipArmorMenu(scanner, party);
                case "0" -> done = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // --------------------------------------------------
    // UTIL
    // --------------------------------------------------

    private static void showParty(List<Hero> party) {
        System.out.println("\n--- Party ---");
        for (int i = 0; i < party.size(); i++) {
            Hero h = party.get(i);
            System.out.println((i + 1) + ") " + h.toString());
        }
    }

    private static Hero chooseHero(Scanner scanner, List<Hero> party) {
        showParty(party);
        System.out.print("Choose hero by number: ");
        String input = scanner.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return null;
        }
        if (idx < 1 || idx > party.size()) {
            System.out.println("Invalid hero number.");
            return null;
        }
        return party.get(idx - 1);
    }

    // --------------------------------------------------
    // BUY WEAPON
    // --------------------------------------------------

    private static void buyWeapon(Scanner scanner, List<Hero> party, List<Weapon> weapons) {
        if (weapons == null || weapons.isEmpty()) {
            System.out.println("No weapons available in this market.");
            return;
        }

        Hero buyer = chooseHero(scanner, party);
        if (buyer == null) return;

        System.out.println("\n--- Weapons for sale ---");
        for (int i = 0; i < weapons.size(); i++) {
            Weapon w = weapons.get(i);
            System.out.printf("%d) %s  (Cost=%d, ReqLvl=%d, Dmg=%d, Hands=%d)%n",
                    i + 1, w.getName(), w.getCost(), w.getRequiredLevel(),
                    w.getDamage(), w.getHandsRequired());
        }
        System.out.println("0) Cancel");
        System.out.print("Choose weapon to buy: ");
        String input = scanner.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (idx == 0) return;
        if (idx < 1 || idx > weapons.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Weapon chosen = weapons.get(idx - 1);
        if (buyer.getGold() < chosen.getCost()) {
            System.out.println(buyer.getName() + " does not have enough gold.");
            return;
        }
        if (buyer.getLevel() < chosen.getRequiredLevel()) {
            System.out.println(buyer.getName() + " is not high enough level.");
            return;
        }

        buyer.spendGold(chosen.getCost());
        buyer.addWeapon(chosen);
        System.out.println(buyer.getName() + " bought weapon " + chosen.getName() + ".");
    }

    // --------------------------------------------------
    // BUY ARMOR
    // --------------------------------------------------

    private static void buyArmor(Scanner scanner, List<Hero> party, List<Armor> armors) {
        if (armors == null || armors.isEmpty()) {
            System.out.println("No armors available in this market.");
            return;
        }

        Hero buyer = chooseHero(scanner, party);
        if (buyer == null) return;

        System.out.println("\n--- Armors for sale ---");
        for (int i = 0; i < armors.size(); i++) {
            Armor a = armors.get(i);
            System.out.printf("%d) %s  (Cost=%d, ReqLvl=%d, Red=%d)%n",
                    i + 1, a.getName(), a.getCost(), a.getRequiredLevel(),
                    a.getDamageReduction());
        }
        System.out.println("0) Cancel");
        System.out.print("Choose armor to buy: ");
        String input = scanner.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (idx == 0) return;
        if (idx < 1 || idx > armors.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Armor chosen = armors.get(idx - 1);
        if (buyer.getGold() < chosen.getCost()) {
            System.out.println(buyer.getName() + " does not have enough gold.");
            return;
        }
        if (buyer.getLevel() < chosen.getRequiredLevel()) {
            System.out.println(buyer.getName() + " is not high enough level.");
            return;
        }

        buyer.spendGold(chosen.getCost());
        buyer.addArmor(chosen);
        System.out.println(buyer.getName() + " bought armor " + chosen.getName() + ".");
    }

    // --------------------------------------------------
    // BUY POTION
    // --------------------------------------------------

    private static void buyPotion(Scanner scanner, List<Hero> party, List<Potion> potions) {
        if (potions == null || potions.isEmpty()) {
            System.out.println("No potions available in this market.");
            return;
        }

        Hero buyer = chooseHero(scanner, party);
        if (buyer == null) return;

        System.out.println("\n--- Potions for sale ---");
        for (int i = 0; i < potions.size(); i++) {
            Potion p = potions.get(i);
            System.out.printf("%d) %s  (Cost=%d, ReqLvl=%d, +%d %s)%n",
                    i + 1, p.getName(), p.getCost(), p.getRequiredLevel(),
                    p.getAttributeIncrease(), p.getAttributeAffected());
        }
        System.out.println("0) Cancel");
        System.out.print("Choose potion to buy: ");
        String input = scanner.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (idx == 0) return;
        if (idx < 1 || idx > potions.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Potion chosen = potions.get(idx - 1);
        if (buyer.getGold() < chosen.getCost()) {
            System.out.println(buyer.getName() + " does not have enough gold.");
            return;
        }
        if (buyer.getLevel() < chosen.getRequiredLevel()) {
            System.out.println(buyer.getName() + " is not high enough level.");
            return;
        }

        buyer.spendGold(chosen.getCost());
        buyer.addPotion(chosen);
        System.out.println(buyer.getName() + " bought potion " + chosen.getName() + ".");
    }

    // --------------------------------------------------
    // BUY SPELL
    // --------------------------------------------------

    private static void buySpell(Scanner scanner, List<Hero> party, List<Spell> spells) {
        if (spells == null || spells.isEmpty()) {
            System.out.println("No spells available in this market.");
            return;
        }

        Hero buyer = chooseHero(scanner, party);
        if (buyer == null) return;

        System.out.println("\n--- Spells for sale ---");
        for (int i = 0; i < spells.size(); i++) {
            Spell s = spells.get(i);
            System.out.printf("%d) %s (Cost=%d, ReqLvl=%d, Dmg=%d, MP=%d, School=%s)%n",
                    i + 1, s.getName(), s.getCost(), s.getRequiredLevel(),
                    s.getDamage(), s.getManaCost(), s.getSchool());
        }
        System.out.println("0) Cancel");
        System.out.print("Choose spell to buy: ");
        String input = scanner.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (idx == 0) return;
        if (idx < 1 || idx > spells.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Spell chosen = spells.get(idx - 1);
        if (buyer.getGold() < chosen.getCost()) {
            System.out.println(buyer.getName() + " does not have enough gold.");
            return;
        }
        if (buyer.getLevel() < chosen.getRequiredLevel()) {
            System.out.println(buyer.getName() + " is not high enough level.");
            return;
        }

        buyer.spendGold(chosen.getCost());
        buyer.addSpell(chosen);
        System.out.println(buyer.getName() + " bought spell " + chosen.getName() + ".");
    }

    // --------------------------------------------------
    // EQUIP MENUS
    // --------------------------------------------------

    private static void equipWeaponMenu(Scanner scanner, List<Hero> party) {
        Hero h = chooseHero(scanner, party);
        if (h == null) return;

        List<Weapon> ws = h.getWeapons();
        if (ws.isEmpty()) {
            System.out.println(h.getName() + " has no weapons.");
            return;
        }

        System.out.println("\n" + h.getName() + "'s weapons:");
        for (int i = 0; i < ws.size(); i++) {
            Weapon w = ws.get(i);
            System.out.printf("%d) %s  (Dmg=%d, ReqLvl=%d)%n",
                    i + 1, w.getName(), w.getDamage(), w.getRequiredLevel());
        }
        System.out.println("0) Cancel");
        System.out.print("Choose weapon to equip: ");
        String input = scanner.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (idx == 0) return;
        if (idx < 1 || idx > ws.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Weapon chosen = ws.get(idx - 1);
        h.equipWeapon(chosen);
    }

    private static void equipArmorMenu(Scanner scanner, List<Hero> party) {
        Hero h = chooseHero(scanner, party);
        if (h == null) return;

        List<Armor> as = h.getArmors();
        if (as.isEmpty()) {
            System.out.println(h.getName() + " has no armors.");
            return;
        }

        System.out.println("\n" + h.getName() + "'s armors:");
        for (int i = 0; i < as.size(); i++) {
            Armor a = as.get(i);
            System.out.printf("%d) %s  (Red=%d, ReqLvl=%d)%n",
                    i + 1, a.getName(), a.getDamageReduction(), a.getRequiredLevel());
        }
        System.out.println("0) Cancel");
        System.out.print("Choose armor to equip: ");
        String input = scanner.nextLine().trim();
        int idx;
        try {
            idx = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (idx == 0) return;
        if (idx < 1 || idx > as.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Armor chosen = as.get(idx - 1);
        h.equipArmor(chosen);
    }
}
