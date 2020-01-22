package gameLogic.inventory;

public class Food extends InventoryItem {

    int saturationValue;

    public Food(String displayName, int saturationValue) {
        super(displayName);
        this.saturationValue = saturationValue;
    }

    public int getSaturationValue() {
        return saturationValue;
    }

}
