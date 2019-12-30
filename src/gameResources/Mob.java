package gameResources;

public class Mob extends Entity {

    private int health;
    private int maxHealth;
    private int armor;
    private int level;

    //Skills
    private int strength;
    private int dexterity;
    private int intelligence;

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
}
