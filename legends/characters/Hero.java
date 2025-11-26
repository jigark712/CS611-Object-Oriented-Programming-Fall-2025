package legends.characters;

import java.util.ArrayList;
import java.util.List;

import legends.items.Potion;
import legends.items.Spell;
import legends.items.Weapon;

public class Hero {

    private String name;
    private int level;
    private int experience;
    private int gold;

    private int strength;
    private int agility;
    private int dexterity;

    private int maxHp;
    private int hp;

    private int maxMp;
    private int mp;

    private Weapon equippedWeapon;

    private final List<Weapon> weapons = new ArrayList<>();
    private final List<Potion> potions = new ArrayList<>();
    private final List<Spell> spells = new ArrayList<>();

    public Hero(String name, int mana, int str, int agi, int dex, int money, int exp) {
        this.name = name;
        this.level = 1;
        this.experience = exp;
        this.gold = money;

        this.strength = str;
        this.agility = agi;
        this.dexterity = dex;

        this.maxHp = 300;   // FIXED so gameplay is fair
        this.hp = maxHp;

        this.maxMp = mana;
        this.mp = mana;
    }

    public Hero(String name) {
        this(name, 200, 100, 100, 100, 1000, 0);
    }

    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getMp() { return mp; }
    public int getMaxMp() { return maxMp; }
    public int getGold() { return gold; }

    public Weapon getEquippedWeapon() { return equippedWeapon; }

    public boolean isAlive() { return hp > 0; }

    public void setHp(int val) { hp = Math.max(0, Math.min(val, maxHp)); }
    public void setMp(int val) { mp = Math.max(0, Math.min(val, maxMp)); }

    public void takeDamage(int dmg) { setHp(hp - dmg); }

    public void heal(int amount) { setHp(hp + amount); }

    public double getDodgeChance() {
        return Math.min(0.30, agility * 0.002);
    }

    public int computePhysicalDamage() {
    int weaponDmg = (equippedWeapon != null) ? equippedWeapon.getDamage() : 50;
    double dmg = weaponDmg * 0.3 + strength * 0.1;

    // Fair limits so hero never one-shots
    if (dmg < 20) dmg = 20;
    if (dmg > 80) dmg = 80;

    // jitter ±20%
    int jitter = (int) (dmg * 0.2);
    int finalDmg = (int) (dmg - jitter + Math.random() * (2 * jitter + 1));

    return Math.max(10, finalDmg);
}


    public int computeSpellDamage(Spell s) {
        int base = s.getDamage() + dexterity / 20;
        int jitter = (int)(base * 0.15);
        int finalDmg = base - jitter + (int)(Math.random() * (jitter * 2 + 1));
        return Math.max(10, finalDmg);
    }

    public List<Weapon> getWeapons() { return weapons; }
    public List<Potion> getPotions() { return potions; }
    public List<Spell> getSpells() { return spells; }

    public boolean hasPotions() { return !potions.isEmpty(); }
    public boolean hasSpells() { return !spells.isEmpty(); }

    public void addWeapon(Weapon w) { weapons.add(w); }
    public void addPotion(Potion p) { potions.add(p); }
    public void addSpell(Spell s) { spells.add(s); }

    public void equipWeapon(Weapon w) {
        equippedWeapon = w;
        if (!weapons.contains(w)) weapons.add(w);
        System.out.println(name + " equipped weapon " + w.getName());
    }

    public boolean usePotion(int index) {
        if (index < 0 || index >= potions.size()) return false;
        Potion p = potions.remove(index);

        if (p.getAttributeAffected().toLowerCase().contains("hp")) {
            heal(p.getAttributeIncrease());
            return true;
        }
        if (p.getAttributeAffected().toLowerCase().contains("mp")) {
            setMp(mp + p.getAttributeIncrease());
            return true;
        }
        return false;
    }

    public void gainExperience(int amount) {
        experience += amount;
        int threshold = 5 * level;  
        if (experience >= threshold) {
            experience -= threshold;
            level++;
            strength += 10;
            agility += 10;
            dexterity += 10;
            maxHp += 10;
            hp = maxHp;
            System.out.println(name + " leveled up to " + level + "!");
        }
    }
    public Hero copyWithName(String newName) {
    Hero h = new Hero(
        newName,
        this.maxMp,
        this.strength,
        this.agility,
        this.dexterity,
        this.gold,
        this.experience
    );

    h.level = this.level;
    h.maxHp = this.maxHp;
    h.hp = this.hp;

    // copy equipment + inventory
    h.equippedWeapon = this.equippedWeapon;

    h.getWeapons().addAll(this.weapons);
    h.getPotions().addAll(this.potions);
    h.getSpells().addAll(this.spells);

    return h;
}


    @Override
    public String toString() {
        return name + " (Lvl " + level +
               ", HP " + hp + "/" + maxHp +
               ", MP " + mp + "/" + maxMp +
               ", Gold " + gold + ")";
    }
}
