package legends.items;

import legends.characters.monsters.Monster;

public class FireSpell extends Spell {

    public FireSpell(String name, int cost, int requiredLevel,
                     int damage, int manaCost) {
        super(name, cost, requiredLevel, damage, manaCost, School.FIRE);
    }

    @Override
    public void applyEffectOnMonster(Monster m) {
        // Reduce monster defense by 20%
        int oldDef = m.getDefense();
        int reduce = (int) Math.round(oldDef * 0.2);
        m.setDefense(Math.max(0, oldDef - reduce));
        System.out.println("Fire spell scorches " + m.getName()
                + ", reducing its defense by " + reduce + "!");
    }
}
