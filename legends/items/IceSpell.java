package legends.items;

import legends.characters.monsters.Monster;

public class IceSpell extends Spell {

    public IceSpell(String name, int cost, int requiredLevel,
                    int damage, int manaCost) {
        super(name, cost, requiredLevel, damage, manaCost, School.ICE);
    }

    @Override
    public void applyEffectOnMonster(Monster m) {
        // Reduce monster damage by 20%
        int old = m.getBaseDamage();
        int reduce = (int) Math.round(old * 0.2);
        m.setBaseDamage(Math.max(1, old - reduce));
        System.out.println("Ice spell chills " + m.getName()
                + ", weakening its attacks by " + reduce + "!");
    }
}
