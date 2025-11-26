package legends.items;

public class Potion extends Item {

    private final int attributeIncrease;
    private final String attributeAffected;

    public Potion(String name, int cost, int requiredLevel,
                  int attributeIncrease, String attributeAffected) {
        super(name, cost, requiredLevel);
        this.attributeIncrease = attributeIncrease;
        this.attributeAffected = attributeAffected;
    }

    public int getAttributeIncrease() {
        return attributeIncrease;
    }

    public String getAttributeAffected() {
        return attributeAffected;
    }

    @Override
    public String toString() {
        return String.format(
            "%s  (Cost=%d, Lvl=%d, +%d %s)",
            name, cost, requiredLevel, attributeIncrease, attributeAffected
        );
    }
}
