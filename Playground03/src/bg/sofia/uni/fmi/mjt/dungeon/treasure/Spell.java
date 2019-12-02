package bg.sofia.uni.fmi.mjt.dungeon.treasure;

import bg.sofia.uni.fmi.mjt.dungeon.actor.Hero;

public class Spell extends Equipment {
    private int manaCost;

    public Spell(String name, int damage, int manaCost) {
        super(name, damage);
        this.manaCost = manaCost;
    }

    public int getManaCost() {
        return manaCost;
    }

    @Override
    public String collect(Hero hero) {
        final String message = "Spell found! Damage points: " + getDamage() + ", Mana cost: "
                + manaCost;

        hero.learn(this);
        
        return message;
    }
}
