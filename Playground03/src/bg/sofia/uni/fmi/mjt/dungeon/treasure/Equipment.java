package bg.sofia.uni.fmi.mjt.dungeon.treasure;

import bg.sofia.uni.fmi.mjt.dungeon.actor.Hero;

public abstract class Equipment implements Treasure {
    private String name;
    private int damage;
    
    public Equipment(String name, int damage) {
        this.name = name;
        this.damage = damage;
    }
    
    public String getName() {
        return name;
    }
    
    public int getDamage() {
        return damage;
    }
    
    public abstract String collect(Hero hero);
}
