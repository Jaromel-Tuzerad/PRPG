package prpg.gameLogic.items;

public class QuestItem extends InventoryItem {

    public QuestItem(String name) {
        super(name, ItemType.QUEST);
    }

    @Override
    public String getDisplayName() {
        return this.name + " (Q)";
    }

    @Override
    public String getDescription() {
        return "This is the item you were looking for!";
    }

}
