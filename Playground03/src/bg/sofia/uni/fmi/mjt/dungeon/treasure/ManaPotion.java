package bg.sofia.uni.fmi.mjt.dungeon.treasure;

import bg.sofia.uni.fmi.mjt.dungeon.actor.Hero;

public class ManaPotion extends Potion {

    public ManaPotion(int manaPoints) {
        super(manaPoints);
    }

    @Override
    public String collect(Hero hero) {
        final int manaPoints = heal();
        final String message = "Mana potion found! " + manaPoints
                + " mana points added to your hero!";

        hero.takeMana(manaPoints);
        
        return message;
    }

}
