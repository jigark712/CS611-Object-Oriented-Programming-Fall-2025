package legends.items;

import legends.characters.monsters.Monster;

public abstract class Spell {

    public enum School {
        FIRE,
        ICE,
        LIGHTNING
    }

    protected final String name;
    protected final int cost;
    protected final int requiredLevel;
    protected final int damage;
    protected final int manaCost;
    protected final School school;

    public Spell(String name, int cost, int requiredLevel,
                 int damage, int manaCost, School school) {
        this.name = name;
        this.cost = cost;
        this.requiredLevel = requiredLevel;
        this.damage = damage;
        this.manaCost = manaCost;
        this.school = school;
    }

    public String getName() { return name; }
    public int getCost() { return cost; }
    public int getRequiredLevel() { return requiredLevel; }
    public int getDamage() { return damage; }
    public int getManaCost() { return manaCost; }
    public School getSchool() { return school; }

    /**
     * All spells apply a debuff of some kind to a monster.
     */
    public abstract void applyEffectOnMonster(Monster m);

    @Override
    public String toString() {
        return String.format(
            "%s [%s] (Cost=%d, Lvl=%d, Dmg=%d, MP=%d)",
            name, school, cost, requiredLevel, damage, manaCost
        );
    }
}
