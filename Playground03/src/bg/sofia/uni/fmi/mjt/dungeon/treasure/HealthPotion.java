package bg.sofia.uni.fmi.mjt.dungeon.treasure;

import bg.sofia.uni.fmi.mjt.dungeon.actor.Hero;

public class HealthPotion extends Potion {

    public HealthPotion(int healingPoints) {
        super(healingPoints);
    }

    @Override
    public String collect(Hero hero) {
        final int healingPoints = heal();
        final String message = "Health potion found! " + healingPoints
                + " health points added to your hero!";
        
        hero.takeHealing(healingPoints);

        return message;
    }

}
