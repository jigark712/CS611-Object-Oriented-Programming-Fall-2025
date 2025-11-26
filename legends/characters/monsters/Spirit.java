package legends.characters.monsters;

public class Spirit extends Monster {

    public Spirit(String name, int level, int baseDamage, int defense, int dodgeChance) {
        super(name, level, baseDamage, defense, dodgeChance);
    }

    @Override
    public String toString() {
        return "Spirit " + name + " (Lvl " + level + ", HP " + getHp() + "/" + getMaxHp() + ")";
    }
}
