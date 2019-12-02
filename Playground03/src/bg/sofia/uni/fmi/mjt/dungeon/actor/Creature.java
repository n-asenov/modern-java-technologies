package bg.sofia.uni.fmi.mjt.dungeon.actor;

import bg.sofia.uni.fmi.mjt.dungeon.treasure.Spell;
import bg.sofia.uni.fmi.mjt.dungeon.treasure.Weapon;

public abstract class Creature implements Actor {
    private final int startingHealth;
    private final int startingMana;

    private String name;
    private int health;
    private int mana;
    private Weapon weapon;
    private Spell spell;

    public Creature(String name, int health, int mana) {
        this(name, health, mana, null, null);
    }

    public Creature(String name, int health, int mana, Weapon weapon, Spell spell) {
        this.name = name;
        this.health = health;
        this.mana = mana;
        this.weapon = weapon;
        this.spell = spell;
        startingHealth = health;
        startingMana = mana;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getMana() {
        return mana;
    }

    @Override
    public Weapon getWeapon() {
        return weapon;
    }

    @Override
    public Spell getSpell() {
        return spell;
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void takeDamage(int damagePoints) {
        if (damagePoints > health) {
            health = 0;
        } else {
            health -= damagePoints;
        }
    }

    @Override
    public int attack() {
        if (spellIsBetterThanWeapon()) {
            mana -= spell.getManaCost();
            return spell.getDamage();
        }

        if (weapon == null) {
            return 0;
        }

        return weapon.getDamage();
    }

    public void equip(Weapon newWeapon) {
        if (weapon == null || weapon.getDamage() < newWeapon.getDamage()) {
            weapon = newWeapon;
        }
    }

    public void learn(Spell newSpell) {
        if (spell == null || spell.getDamage() < newSpell.getDamage()
                || (spell.getDamage() == newSpell.getDamage()
                        && spell.getManaCost() > newSpell.getManaCost())) {
            spell = newSpell;
        }
    }

    public void takeHealing(int healingPoints) {
        if (isAlive()) {
            if (health + healingPoints > startingHealth) {
                health = startingHealth;
            } else {
                health += healingPoints;
            }
        }
    }

    public void takeMana(int manaPoints) {
        if (mana + manaPoints > startingMana) {
            mana = startingMana;
        } else {
            mana += manaPoints;
        }
    }

    private boolean spellIsBetterThanWeapon() {
        return (spell != null && mana >= spell.getManaCost())
                && (weapon == null || weapon.getDamage() < spell.getDamage());
    }

}
