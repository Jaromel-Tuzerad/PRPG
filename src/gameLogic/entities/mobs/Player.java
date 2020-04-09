package gameLogic.entities.mobs;

import exceptions.ExceptionAlert;
import exceptions.MobDiedException;
import gameLogic.inventory.Equipment;
import gameLogic.inventory.Food;
import gameLogic.inventory.InventoryItem;
import gameLogic.RandomFunctions;

import java.util.ArrayList;

public class Player extends Mob {

    private int hunger;
    private int experience;
    private int experienceToNextLevel;
    private int statPoints;
    private Equipment equippedHeadArmor;
    private Equipment equippedBodyArmor;
    private Equipment equippedLegArmor;
    private Equipment equippedWeapon;
    private ArrayList<InventoryItem> inventory;
    private char icon = '@';
    private int posX;
    private int posY;

    public Player(int x, int y, String username, int strength, int dexterity, int intelligence) {
        super(username, "" , 100, 1, strength, dexterity, intelligence);
        this.hunger = 100;
        this.experience = 0;
        this.experienceToNextLevel = 100;
        this.statPoints = 0;
        this.inventory = new ArrayList<>();
        this.posX = x;
        this.posY = y;
    }

    public void setPos(int x, int y) {
        this.posX = x;
        this.posY = y;
    }

    public void moveBy(int dX, int dY) {
        this.posX += dX;
        this.posY += dY;
    }

    public void addExperience(int addedExperience) {
        this.experience += addedExperience;
        if(this.experience >= this.experienceToNextLevel) {
            this.levelUp();
        }
    }

    public void levelUp() {
        this.maxHealth *= 1.2;
        this.health = this.maxHealth;
        this.experience = this.experience - this.experienceToNextLevel;
        this.experienceToNextLevel *= 2;
        this.level+=1;
        this.statPoints += 5;
    }

    public char getIcon() {
        return this.icon;
    }

    public void pickUpItem(InventoryItem item) { this.inventory.add(item); }

    public void dropItem(InventoryItem item) { this.inventory.remove(item); }

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
        int output = this.dexterity;
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

    public int getTotalDefense() {
        int output = this.defense;
        if(this.equippedHeadArmor != null) {
            output += this.equippedHeadArmor.getAddedDefense();
        }
        if(this.equippedBodyArmor != null) {
            output += this.equippedBodyArmor.getAddedDefense();
        }
        if(this.equippedWeapon != null) {
            output += this.equippedWeapon.getAddedDefense();
        }
        if(this.equippedLegArmor != null) {
            output += this.equippedLegArmor.getAddedDefense();
        }
        return output;
    }

    public void decreaseHunger() {
        int points = RandomFunctions.getRandomNumberInRange(1, 5);
        if(this.hunger - points >= 0) {
            this.hunger -= points;
        } else {
            this.hunger = 0;
        }
    }

    public void starve() throws MobDiedException {
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
                    if(this.equippedHeadArmor != null) {
                        this.inventory.add(this.equippedHeadArmor);
                    }
                    this.equippedHeadArmor = item;
                    break;
                case BODYWEAR:
                    if(this.equippedBodyArmor != null) {
                        this.inventory.add(this.equippedBodyArmor);
                    }
                    this.equippedBodyArmor = item;
                    break;
                case LEGWEAR:
                    if(this.equippedLegArmor != null) {
                        this.inventory.add(this.equippedLegArmor);
                    }
                    this.equippedLegArmor = item;
                    break;
                case WEAPON:
                    if(this.equippedWeapon != null) {
                        this.inventory.add(this.equippedWeapon);
                    }
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

    public void UnequipItem(InventoryItem.ItemType type) throws ExceptionAlert {
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

    public void UnequipItem(InventoryItem item) throws ExceptionAlert {
        UnequipItem(item.getType());
    }

    public void increaseStrengthBy(int points) {
        this.strength += points;
    }

    public void increaseDexterityBy(int points) {
        this.dexterity += points;
    }

    public void increaseDefenseBy(int points) {
        this.defense += points;
    }

    public void decreaseStatPointsBy(int points) {
        this.statPoints -= points;
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }

    public int getHunger() {
        return this.hunger;
    }

    public int getExperience() {
        return this.experience;
    }

    public int getExperienceToNextLevel() {
        return experienceToNextLevel;
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

    public int getStatPoints() {
        return statPoints;
    }

}
