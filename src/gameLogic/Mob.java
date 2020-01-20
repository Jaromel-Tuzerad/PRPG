package gameLogic;

import exceptions.EntityDiedException;

public abstract class Mob extends Entity {

    protected int health;
    protected int maxHealth;
    protected int armor;
    protected int level;

    //Skills
    protected int strength;
    protected int dexterity;
    protected int intelligence;

    public Mob(int x, int y, String name, String description,  char icon, int maxHealth, int armor, int level, int strength, int dexterity, int intelligence) {
        super(x, y, name, description, icon);
        this.armor = armor;
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

    public int getArmor() {
        return armor;
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

    public void die() throws EntityDiedException {
        throw new EntityDiedException(this.displayName + " has died.");
    }

    public void addHealth(int points) throws EntityDiedException {
        this.health += points;
        if(this.health <= 0) {
            this.die();
        }
    }
}
