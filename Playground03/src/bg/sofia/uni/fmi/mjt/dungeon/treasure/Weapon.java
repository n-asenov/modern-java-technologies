package bg.sofia.uni.fmi.mjt.dungeon.treasure;

import bg.sofia.uni.fmi.mjt.dungeon.actor.Hero;

public class Weapon extends Equipment {
    
    public Weapon(String name, int damage) {
        super(name, damage);
    }
    
    @Override
    public String collect(Hero hero) {
        final String message = "Weapon found! Damage points: " + getDamage();
        
        hero.equip(this);
        
        return message;
    }
}
