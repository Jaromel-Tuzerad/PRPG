package gameLogic.entities;

import exceptions.ExceptionAlert;
import gameLogic.inventory.Food;
import gameLogic.inventory.InventoryItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Player extends Mob {

    private int maxMana;
    private int maxEnergy;
    private int energy;
    private int hunger;
    private int mana;
    private int experience;
    private InventoryItem equippedHeadArmor;
    private InventoryItem equippedBodyArmor;
    private InventoryItem equippedLegArmor;
    private InventoryItem equippedWeapon;
    private ArrayList<InventoryItem> inventory;
    private char icon = '@';

    public Player(int x, int y, String username) {
        super(x, y, username, "" , 10, 0, 5, 5, 5);
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

    public char getIcon() {
        return this.icon;
    }

    public void pickUpItem(InventoryItem item) { this.inventory.add(item); }

    public void decreaseHunger() {
        int points = miscResources.MiscFunctions.getRandomNumberInRange(1, 5);
        if(this.hunger - points >= 0) {
            this.hunger -= points;
        } else {
            this.hunger = 0;
        }
    }

    public void starve() throws ExceptionAlert {
        this.addHealth(0- (int) Math.ceil(((double) this.maxHealth)/20));
    }

    public void eat(Food food) throws ExceptionAlert {
        if(this.inventory.contains(food)) {
            int saturationValue = food.getSaturationValue();
            if(this.hunger + saturationValue <= 100) {
                this.hunger += saturationValue;
            } else {
                this.hunger = 100;
            }
            this.inventory.remove(food);
        } else {
            throw new ExceptionAlert("Item error", "An error occurred while trying to eat food", "Selected food is not in player's inventory");
        }
    }

    public void equipItem(InventoryItem item) throws ExceptionAlert {
        if(this.inventory.contains(item)) {
            switch(item.getType()) {
                case HEADWEAR:
                    this.equippedHeadArmor = item;
                    break;
                case BODYWEAR:
                    this.equippedBodyArmor = item;
                    break;
                case LEGWEAR:
                    this.equippedLegArmor = item;
                    break;
                case WEAPON:
                    this.equippedWeapon = item;
                    break;
                default:
                    throw new ExceptionAlert("Item error", "An error occurred while trying to equip item " + item.getDisplayName(), "Item is not equipable");
            }
            this.inventory.remove(item);
        } else {
            throw new ExceptionAlert("Equipment error", "An error occurred while trying to equip item " + item.getDisplayName(), "Item is not in player's inventory");
        }


    }

    public ArrayList<InventoryItem> getInventory() {
        return inventory;
    }

}
