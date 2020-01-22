package gameLogic.entities;

import gui.GameController;

public class NPC extends Mob {
    public NPC(int x, int y, String name, String description, char icon, int maxHealth, int level, int strength, int dexterity, int intelligence) {
        super(x, y, name, description, maxHealth, level, strength, dexterity, intelligence);
    }

    public void talk() {
        GameController.callAlert("Conversation", this.displayName + " tells you:", "Hi, it is a lovely day, isn't it?");
    }

}
