package legends.items;

public class Weapon extends Item {

    private final int damage;
    private final int handsRequired;

    public Weapon(String name, int cost, int requiredLevel, int damage, int handsRequired) {
        super(name, cost, requiredLevel);
        this.damage = damage;
        this.handsRequired = handsRequired;
    }

    public int getDamage() { return damage; }

    public int getHandsRequired() { return handsRequired; }

    @Override
    public String toString() {
        return String.format(
            "%s  (Cost=%d, Lvl=%d, Dmg=%d, Hands=%d)",
            name, cost, requiredLevel, damage, handsRequired
        );
    }
}
