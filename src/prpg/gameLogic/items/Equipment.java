package prpg.gameLogic.items;

public class Equipment extends InventoryItem implements Tradeable {

    private int addedStrength;
    private int addedDexterity;
    private int addedDefense;
    private int equipmentLevel;

    public Equipment(String displayName, int addedStrength, int addedDexterity, int addedDefense, ItemType type, int equipmentLevel) {
        super(displayName, type);
        this.addedStrength = addedStrength;
        this.addedDexterity = addedDexterity;
        this.addedDefense = addedDefense;
        this.equipmentLevel = equipmentLevel;
    }

    @Override
    public String getDisplayName() {
        return this.name + " (" + addedStrength + ", " + addedDexterity + ", " + addedDefense +")";
    }

    @Override
    public String getDescription() {
        return (this.name + "\nStrength: " + this.addedStrength + "\nDexterity: " + this.addedDexterity + "\nIntelligence: " + this.addedDefense);
    }

    @Override
    public int getWorth() {
        return (this.addedStrength+this.addedDexterity+this.addedDefense)*5;
    }

    public int getAddedStrength() {
        return addedStrength;
    }

    public int getAddedDexterity() {
        return addedDexterity;
    }

    public int getAddedDefense() {
        return addedDefense;
    }

    public int getEquipmentLevel() {
        return this.equipmentLevel;
    }

}
