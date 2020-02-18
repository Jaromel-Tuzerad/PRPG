package gameLogic.inventory;

public class Equipment extends InventoryItem {

    private int addedStrength;
    private int addedDexterity;
    private int addedIntelligence;

    public Equipment(String displayName, int addedStrength, int addedDexterity, int addedIntelligence, ItemType type) {
        super(displayName, type);
        this.addedStrength = addedStrength;
        this.addedDexterity = addedDexterity;
        this.addedIntelligence = addedIntelligence;
    }

    public int getAddedStrength() {
        return addedStrength;
    }

    public int getAddedDexterity() {
        return addedDexterity;
    }

    public int getAddedIntelligence() {
        return addedIntelligence;
    }

    public String getDisplayName() {
        return this.displayName + " (" + addedStrength + ", " + addedDexterity + ", " + addedIntelligence +")";
    }

    public String getDescription() {
        return (this.displayName + "\nStrength: " + this.addedStrength + "\nDexterity: " + this.addedDexterity + "\nIntelligence: " + this.addedIntelligence);
    }
}
