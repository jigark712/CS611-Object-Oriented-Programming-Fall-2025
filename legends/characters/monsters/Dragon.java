package legends.characters.monsters;

public class Dragon extends Monster {

    public Dragon(String name, int level, int baseDamage, int defense, int dodgeChance) {
        super(name, level, baseDamage, defense, dodgeChance);
    }

    @Override
    public String toString() {
        return "Dragon " + name + " (Lvl " + level + ", HP " + getHp() + "/" + getMaxHp() + ")";
    }
}
