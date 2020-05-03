package prpg.gameLogic.items;

public class Food extends InventoryItem implements Tradeable  {

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
        return this.name + " (" + this.saturationValue + ")";
    }

    @Override
    public String getDescription() {
        return (this.name + "\nEdible food\nSaturation value: " + this.saturationValue);
    }

    @Override
    public int getWorth() {
        return saturationValue;
    }

}
