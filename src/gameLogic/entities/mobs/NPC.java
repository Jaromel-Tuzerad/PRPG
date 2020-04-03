package gameLogic.entities.mobs;

import exceptions.ExceptionAlert;
import gui.gamePanel.GamePanelController;

public class NPC extends Mob {
    public NPC(String name, String description, char icon, int maxHealth) {
        super(name, description, maxHealth, 0, 0, 0, 0);
    }

    public void talk() {
        GamePanelController.callAlert(new ExceptionAlert("Conversation", this.displayName + " tells you:", "Hi, it is a lovely day, isn't it?"));
    }

}
