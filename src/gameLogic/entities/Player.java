package gameLogic.entities;

import exceptions.ExceptionAlert;
import gameLogic.inventory.Equipment;
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
    private Equipment equippedHeadArmor;
    private Equipment equippedBodyArmor;
    private Equipment equippedLegArmor;
    private Equipment equippedWeapon;
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

    public int getTotalStrength() {
        int output = this.strength;
        if(this.equippedHeadArmor != null) {
            output += this.equippedHeadArmor.getAddedStrength();
        }
        if(this.equippedBodyArmor != null) {
            output += this.equippedBodyArmor.getAddedStrength();
        }
        if(this.equippedWeapon != null) {
            output += this.equippedWeapon.getAddedStrength();
        }
        if(this.equippedLegArmor != null) {
            output += this.equippedLegArmor.getAddedStrength();
        }
        return output;
    }

    public int getTotalDexterity() {
        int output = this.strength;
        if(this.equippedHeadArmor != null) {
            output += this.equippedHeadArmor.getAddedDexterity();
        }
        if(this.equippedBodyArmor != null) {
            output += this.equippedBodyArmor.getAddedDexterity();
        }
        if(this.equippedWeapon != null) {
            output += this.equippedWeapon.getAddedDexterity();
        }
        if(this.equippedLegArmor != null) {
            output += this.equippedLegArmor.getAddedDexterity();
        }
        return output;
    }

    public int getTotalIntelligence() {
        int output = this.strength;
        if(this.equippedHeadArmor != null) {
            output += this.equippedHeadArmor.getAddedIntelligence();
        }
        if(this.equippedBodyArmor != null) {
            output += this.equippedBodyArmor.getAddedIntelligence();
        }
        if(this.equippedWeapon != null) {
            output += this.equippedWeapon.getAddedIntelligence();
        }
        if(this.equippedLegArmor != null) {
            output += this.equippedLegArmor.getAddedIntelligence();
        }
        return output;
    }

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

    public void equipItem(Equipment item) throws ExceptionAlert {
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

    public void deequipItem(InventoryItem.ItemType type) throws ExceptionAlert {
        switch(type) {
            case HEADWEAR:
                if(this.equippedHeadArmor != null) {
                    this.inventory.add(this.equippedHeadArmor);
                    this.equippedHeadArmor = null;
                } else {
                    throw new ExceptionAlert("Equipment error", "An error occurred while trying to deequip item in slot " + type, "There is no item equipped in that slot");
                }
                break;
            case BODYWEAR:
                if(this.equippedBodyArmor != null) {
                    this.inventory.add(this.equippedBodyArmor);
                    this.equippedBodyArmor = null;
                } else {
                    throw new ExceptionAlert("Equipment error", "An error occurred while trying to deequip item in slot " + type, "There is no item equipped in that slot");
                }
                break;
            case WEAPON:
                if(this.equippedWeapon != null) {
                    this.inventory.add(this.equippedWeapon);
                    this.equippedWeapon = null;
                } else {
                    throw new ExceptionAlert("Equipment error", "An error occurred while trying to deequip item in slot " + type, "There is no item equipped in that slot");
                }
                break;
            case LEGWEAR:
                if(this.equippedLegArmor != null) {
                    this.inventory.add(this.equippedLegArmor);
                    this.equippedLegArmor = null;
                } else {
                    throw new ExceptionAlert("Equipment error", "An error occurred while trying to deequip item in slot " + type, "There is no item equipped in that slot");
                }
                break;
            default:
                throw new ExceptionAlert("Equipment error", "An error occurred while trying to deequip item in slot " + type, "Selected slot type is not equipment");
        }
    }

    public void deequipItem(InventoryItem item) throws ExceptionAlert {
        deequipItem(item.getType());
    }

    public ArrayList<InventoryItem> getInventory() {
        return inventory;
    }

    public InventoryItem getEquippedHeadArmor() {
        return equippedHeadArmor;
    }

    public InventoryItem getEquippedBodyArmor() {
        return equippedBodyArmor;
    }

    public InventoryItem getEquippedLegArmor() {
        return equippedLegArmor;
    }

    public InventoryItem getEquippedWeapon() {
        return equippedWeapon;
    }

}
