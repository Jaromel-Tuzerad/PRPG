package gameLogic;

import exceptions.EntityDiedException;

import java.util.ArrayList;

public class Player extends Mob {

    private int maxMana;
    private int maxEnergy;
    private int energy;
    private int hunger;
    private int mana;
    private int experience;
    private ArrayList<InventoryItem> inventory;

    public Player(String username, int x, int y) {
        super(x, y, username,"", '@', 10, 0, 0, 5, 5, 5);
        this.maxEnergy = 5;
        this.maxMana = 5;
        this.hunger = 100;
        this.energy = maxEnergy;
        this.mana = maxMana;
        this.experience = 0;
        this.inventory = new ArrayList<InventoryItem>();
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

    public void pickUp(Item item) {
        this.inventory.add(item.toInventoryItem());
    }

    public void decreaseHunger() {
        int points = miscResources.MiscFunctions.getRandomNumberInRange(1, 5);
        if(this.hunger - points >= 0) {
            this.hunger -= points;
        } else {
            this.hunger = 0;
        }
    }

    public void starve() throws EntityDiedException {
        this.addHealth(-this.getMaxHealth()/20);
    }
}
