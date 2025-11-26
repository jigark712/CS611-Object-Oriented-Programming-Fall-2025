package legends.market;

import java.util.List;

import legends.items.Armor;
import legends.items.Potion;
import legends.items.Spell;
import legends.items.Weapon;

public class Market {

    private final List<Weapon> weapons;
    private final List<Armor> armors;
    private final List<Potion> potions;
    private final List<Spell> spells;

    public Market(List<Weapon> weapons,
                  List<Armor> armors,
                  List<Potion> potions,
                  List<Spell> spells) {
        this.weapons = weapons;
        this.armors = armors;
        this.potions = potions;
        this.spells = spells;
    }

    public List<Weapon> getWeapons() { return weapons; }
    public List<Armor>  getArmors()  { return armors; }
    public List<Potion> getPotions() { return potions; }
    public List<Spell>  getSpells()  { return spells; }
}
