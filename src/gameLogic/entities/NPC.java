package gameLogic.entities;

import exceptions.ExceptionAlert;
import gui.gamePanel.GamePanelController;

public class NPC extends Mob {
    public NPC(int x, int y, String name, String description, char icon, int maxHealth, int level, int strength, int dexterity, int intelligence) {
        super(x, y, name, description, maxHealth, level, strength, dexterity, intelligence);
    }

    public void talk() {
        GamePanelController.callAlert(new ExceptionAlert("Conversation", this.displayName + " tells you:", "Hi, it is a lovely day, isn't it?"));
    }

}
