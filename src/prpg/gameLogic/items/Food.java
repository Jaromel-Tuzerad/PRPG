package prpg.gameLogic.items;

public class Food extends InventoryItem {

    int saturationValue;

    public Food(String displayName, int saturationValue) {
        super(displayName, ItemType.FOOD);
        this.saturationValue = saturationValue;
    }

    public int getSaturationValue() {
        return saturationValue;
    }

    @Override
    public String getDisplayName() {
        return this.displayName + " (" + this.saturationValue + ")";
    }

    @Override
    public String getDescription() {
        return (this.displayName + "\nEdible food\nSaturation value: " + this.saturationValue);
    }

    @Override
    public int getWorth() {
        return saturationValue;
    }

}
