package legends.items;

import legends.characters.monsters.Monster;

public class LightningSpell extends Spell {

    public LightningSpell(String name, int cost, int requiredLevel,
                          int damage, int manaCost) {
        super(name, cost, requiredLevel, damage, manaCost, School.LIGHTNING);
    }

    @Override
    public void applyEffectOnMonster(Monster m) {
        // Reduce dodge chance percent by 20%
        int oldPct = m.getDodgeChancePercent();
        int reduce = (int) Math.round(oldPct * 0.2);
        m.setDodgeChancePercent(Math.max(0, oldPct - reduce));
        System.out.println("Lightning interrupts " + m.getName()
                + "'s movements, lowering dodge chance by " + reduce + "%!");
    }
}
