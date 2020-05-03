package prpg.gameLogic.entities;

import prpg.exceptions.AlertException;
import prpg.gameLogic.items.InventoryItem;

import java.util.ArrayList;

public interface Storing {

    void addToInventory(InventoryItem item);

    void addToInventory(ArrayList<InventoryItem> items);

    void removeFromInventory(InventoryItem item) throws AlertException;

    ArrayList<InventoryItem> getInventory();

}
