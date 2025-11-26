package legends.characters.monsters;

/**
 * Base Monster class used by Dragon, Spirit, Exoskeleton and BattleSystem.
 *
 * All monsters use:
 *   name, level, baseDamage, defense, dodgeChance (%)
 *   HP is FIXED at 300 for fairness (same as heroes).
 */
public abstract class Monster {

    protected String name;
    protected int level;
    protected int baseDamage;
    protected int defense;
    protected int dodgeChance; // as percentage, e.g., 25 => 25%

    protected static final int FIXED_MAX_HP = 300;
    protected int maxHp;
    protected int hp;

    public Monster(String name, int level, int baseDamage, int defense, int dodgeChance) {
        this.name = name;
        this.level = level;
        this.baseDamage = baseDamage;
        this.defense = defense;
        this.dodgeChance = dodgeChance;

        this.maxHp = FIXED_MAX_HP;
        this.hp = this.maxHp;
    }

    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getBaseDamage() { return baseDamage; }
    public int getDefense() { return defense; }
    public int getDodgePercent() { return dodgeChance; }

    public int getMaxHp() { return maxHp; }
    public int getHp() { return hp; }

    public void setHp(int newHp) {
        this.hp = Math.max(0, Math.min(newHp, maxHp));
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void takeDamage(int rawDamage) {
    // SIMPLE: ignore defense to keep game fair
    int effective = rawDamage;
    if (effective < 1) effective = 1;
    setHp(hp - effective);
}


    /**
     * Convert dodgeChance percentage to probability.
     */
    public double getDodgeChance() {
        double chance = dodgeChance / 100.0;
        if (chance < 0) chance = 0;
        if (chance > 0.5) chance = 0.5; // cap at 50%
        return chance;
    }

    /**
     * Called by LegendsGame before a battle.
     * For your final spec: monsters DO NOT scale with hero level.
     * We just reset HP to full to start a fresh fight.
     */
    public void scaleToLevel(int heroLevel) {
        // No stat scaling. HP stays fixed, just reset.
        this.hp = this.maxHp;
    }

}
