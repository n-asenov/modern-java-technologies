package bg.sofia.uni.fmi.mjt.dungeon.actor;

import bg.sofia.uni.fmi.mjt.dungeon.treasure.Spell;
import bg.sofia.uni.fmi.mjt.dungeon.treasure.Weapon;

public class Enemy extends Creature {

    public Enemy(String name, int health, int mana, Weapon weapon, Spell spell) {
        super(name, health, mana, weapon, spell);
    }
    
}
