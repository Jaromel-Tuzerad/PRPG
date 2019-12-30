package gameResources;

public class Player extends Mob {

    private int maxMana;
    private int maxEnergy;
    private int energy;
    private int hunger;
    private int mana;
    private int experience;

    public Player(String username, int x, int y) {
        super(x, y, username,"", '@', 10, 0, 0, 5, 5, 5);
        this.maxEnergy = 5;
        this.maxMana = 5;
        this.hunger = 100;
        this.energy = maxEnergy;
        this.mana = maxMana;
        this.experience = 0;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public int getEnergy() {
        return this.energy;
    }

    public int getHunger() {
        return this.hunger;
    }

    public int getMana() {
        return this.mana;
    }

    public int getExperience() {
        return this.experience;
    }
}
