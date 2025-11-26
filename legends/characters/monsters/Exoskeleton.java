package legends.characters.monsters;

public class Exoskeleton extends Monster {

    public Exoskeleton(String name, int level, int baseDamage, int defense, int dodgeChance) {
        super(name, level, baseDamage, defense, dodgeChance);
    }

    @Override
    public String toString() {
        return "Exoskeleton " + name + " (Lvl " + level + ", HP " + getHp() + "/" + getMaxHp() + ")";
    }
}
