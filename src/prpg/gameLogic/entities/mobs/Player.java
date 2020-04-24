package prpg.gameLogic.entities.mobs;

import prpg.exceptions.AlertException;
import prpg.exceptions.MobDiedException;
import prpg.gameLogic.entities.Storing;
import prpg.gameLogic.items.Equipment;
import prpg.gameLogic.items.Food;
import prpg.gameLogic.items.InventoryItem;
import prpg.gameLogic.RandomFunctions;
import prpg.gameLogic.quests.FetchQuest;
import prpg.gameLogic.quests.Quest;

import java.util.ArrayList;

public class Player extends Mob implements Storing {

    private int hunger;
    private int experience;
    private int experienceToNextLevel;
    private int statPoints;
    private int gold;
    private Equipment equippedHeadArmor;
    private Equipment equippedBodyArmor;
    private Equipment equippedLegArmor;
    private Equipment equippedWeapon;
    private ArrayList<InventoryItem> inventory;
    private ArrayList<Quest> questJournal;
    private char icon = '@';
    private int posX;
    private int posY;

    public Player(int x, int y, String username, int strength, int dexterity, int intelligence) {
        super(username, "" , 100, 1, strength, dexterity, intelligence);
        this.hunger = 100;
        this.experience = 0;
        this.experienceToNextLevel = 100;
        this.statPoints = 0;
        this.gold = 0;
        this.inventory = new ArrayList<>();
        this.questJournal = new ArrayList<>();
        this.posX = x;
        this.posY = y;
    }

    @Override
    public String getDisplayName() {
        return "[P] " + this.name + " (" + this.level + ")";
    }

    @Override
    public String getActionName() {
        return "";
    }

    @Override
    public void removeFromInventory(InventoryItem item) throws AlertException {
        if(this.inventory.contains(item)) {
            this.inventory.remove(item);
        } else {
            throw new AlertException("Inventory error", "An error occured while trying to remove an item from player's inventory", "The player does not have the specified item in their inventory");
        }
    }

    @Override
    public void addToInventory(InventoryItem item) {
        this.inventory.add(item);
    }

    @Override
    public void addToInventory(ArrayList<InventoryItem> items) {
        this.inventory.addAll(items);
    }

    @Override
    public ArrayList<InventoryItem> getInventory() {
        return inventory;
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
        int points = RandomFunctions.getRandomNumberInRange(1, 3);
        if(this.hunger - points >= 0) {
            this.hunger -= points;
        } else {
            this.hunger = 0;
        }
    }

    public void starve() throws MobDiedException {
        this.addHealth(0- (int) Math.ceil(((double) this.maxHealth)/20));
    }

    public void eat(Food food) throws AlertException, MobDiedException {
        if(this.inventory.contains(food)) {
            int saturationValue = food.getSaturationValue();
            if(this.hunger + saturationValue <= 100) {
                this.hunger += saturationValue;
            } else {
                this.addHealth(this.maxHealth*(saturationValue+this.hunger-100)/100);
                this.hunger = 100;
            }
            this.inventory.remove(food);
        } else {
            throw new AlertException("Item error", "An error occurred while trying to eat food", "Selected food is not in player's inventory");
        }
    }

    public void equipItem(Equipment item) throws AlertException {
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
                    throw new AlertException("Item error", "An error occurred while trying to equip item " + item.getName(), "Item is not equipable");
            }
            this.inventory.remove(item);
        } else {
            throw new AlertException("Equipment error", "An error occurred while trying to equip item " + item.getName(), "Item is not in player's inventory");
        }
    }

    public void unequipItemInSlotType(InventoryItem.ItemType type) throws AlertException {
        switch(type) {
            case HEADWEAR:
                if(this.equippedHeadArmor != null) {
                    this.inventory.add(this.equippedHeadArmor);
                    this.equippedHeadArmor = null;
                } else {
                    throw new AlertException("Equipment error", "An error occurred while trying to deequip item in slot " + type, "There is no item equipped in that slot");
                }
                break;
            case BODYWEAR:
                if(this.equippedBodyArmor != null) {
                    this.inventory.add(this.equippedBodyArmor);
                    this.equippedBodyArmor = null;
                } else {
                    throw new AlertException("Equipment error", "An error occurred while trying to deequip item in slot " + type, "There is no item equipped in that slot");
                }
                break;
            case WEAPON:
                if(this.equippedWeapon != null) {
                    this.inventory.add(this.equippedWeapon);
                    this.equippedWeapon = null;
                } else {
                    throw new AlertException("Equipment error", "An error occurred while trying to deequip item in slot " + type, "There is no item equipped in that slot");
                }
                break;
            case LEGWEAR:
                if(this.equippedLegArmor != null) {
                    this.inventory.add(this.equippedLegArmor);
                    this.equippedLegArmor = null;
                } else {
                    throw new AlertException("Equipment error", "An error occurred while trying to deequip item in slot " + type, "There is no item equipped in that slot");
                }
                break;
            default:
                throw new AlertException("Equipment error", "An error occurred while trying to deequip item in slot " + type, "Selected slot type is not equipment");
        }
    }

    public void unequipItem(InventoryItem item) throws AlertException {
        unequipItemInSlotType(item.getType());
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

    public int getGold() {
        return gold;
    }

    public void addGold(int gold) throws AlertException {
        if(this.gold + gold >= 0) {
            this.gold += gold;
        } else {
            throw new AlertException("Purse", "Insufficient amount of gold", "You don't have enough gold for this transaction to take place.");
        }
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

    public ArrayList<Quest> getQuestJournal() {
        return this.questJournal;
    }

    public void startQuest(Quest quest) {
        this.questJournal.add(quest);
    }

    public void finishQuest(Quest quest) throws AlertException {
        if (this.questJournal.contains(quest)) {
            if(quest.getQuestType() == Quest.QuestType.FETCH) {
                this.removeFromInventory(((FetchQuest)quest).getRequiredItem());
            }
            this.addExperience(quest.getRewardXP());
            this.addGold(quest.getRewardGold());
            this.addToInventory(quest.getRewardItems());
            this.questJournal.remove(quest);
        } else {
            throw new AlertException("Quests error", "There was an error while trying to finish a quest", "The specified quest is not in the player's journal");
        }
    }

}
