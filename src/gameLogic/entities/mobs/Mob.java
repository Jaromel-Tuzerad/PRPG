package gameLogic.entities.mobs;

import exceptions.MobDiedException;
import gameLogic.entities.Entity;

public class Mob extends Entity {

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

    // TODO - what happens when an entity dies
    public void die() throws MobDiedException {
        throw new MobDiedException(this);
    }

    public void addHealth(int points) throws MobDiedException {
        this.health += points;
        if(this.health > this.maxHealth) {
            this.health = this.maxHealth;
        } else if(this.health <= 0) {
            this.die();
        }
    }

}
