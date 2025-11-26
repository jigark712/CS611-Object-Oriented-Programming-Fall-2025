package legends.items;

public class Armor extends Item {

    private final int damageReduction;

    public Armor(String name, int cost, int requiredLevel, int damageReduction) {
        super(name, cost, requiredLevel);
        this.damageReduction = damageReduction;
    }

    public int getDamageReduction() {
        return damageReduction;
    }

    @Override
    public String toString() {
        return String.format(
            "%s  (Cost=%d, Lvl=%d, Reduction=%d)",
            name, cost, requiredLevel, damageReduction
        );
    }
}
