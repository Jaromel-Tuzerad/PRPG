package prpg.gameLogic.entities.mobs;

import prpg.exceptions.MobDiedException;
import prpg.gameLogic.entities.Entity;

public abstract class Mob extends Entity {

    // Stats
    protected int health;
    protected int maxHealth;
    protected int level;

    // Attributes
    protected int strength;
    protected int dexterity;
    protected int defense;

    public Mob(String name, String description, int maxHealth, int level, int strength, int dexterity, int defense) {
        super(name, description);
        this.level = level;
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.strength = strength;
        this.dexterity = dexterity;
        this.defense = defense;
    }

    public int getLevel() {
        return this.level;
    }

    public int getHealth() {
        return this.health;
    }

    public int getMaxHealth() {
        return this.maxHealth;
    }

    public int getStrength() {
        return this.strength;
    }

    public int getDexterity() {
        return this.dexterity;
    }

    public int getDefense() {
        return this.defense;
    }

    public void addHealth(int points) throws MobDiedException {
        this.health += points;
        if(this.health > this.maxHealth) {
            this.health = this.maxHealth;
        } else if(this.health <= 0) {
            throw new MobDiedException(this);
        }
    }

}
