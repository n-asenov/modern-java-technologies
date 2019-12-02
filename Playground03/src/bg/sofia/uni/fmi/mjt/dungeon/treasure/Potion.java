package bg.sofia.uni.fmi.mjt.dungeon.treasure;

import bg.sofia.uni.fmi.mjt.dungeon.actor.Hero;

public abstract class Potion implements Treasure {
    private int recoverPoints;
    
    public Potion(int recoverPoints) {
        this.recoverPoints = recoverPoints;
    }
    
    public int heal() {
        return recoverPoints;
    }
    
    public abstract String collect(Hero hero);
}
