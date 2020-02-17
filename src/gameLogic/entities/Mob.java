package gameLogic.entities;

import exceptions.ExceptionAlert;
import gameLogic.entities.Entity;
import gui.GameController;

public class Mob extends Entity {

    protected int health;
    protected int maxHealth;
    protected int level;

    //Skills
    protected int strength;
    protected int dexterity;
    protected int intelligence;

    public Mob(int x, int y, String name, String description, int maxHealth, int level, int strength, int dexterity, int intelligence) {
        super(x, y, name, description);
        this.level = level;
        this.health = maxHealth;
        this.maxHealth = maxHealth;
        this.strength = strength;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
    }

    public int getLevel() {
        return level;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getStrength() {
        return strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void die() throws ExceptionAlert {
        throw new ExceptionAlert("An entity is dead", "An entity is dead", this.displayName + " has died.");
    }

    public void addHealth(int points) throws ExceptionAlert {
        this.health += points;
        if(this.health <= 0) {
            this.die();
        }
    }

    public void fight() {
        GameController.callAlert(new ExceptionAlert("Combat", "A fight starts", "You are now fighting " + this.displayName));
    }

}
