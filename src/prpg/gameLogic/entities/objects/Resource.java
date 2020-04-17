package prpg.gameLogic.entities.objects;

import prpg.exceptions.ExceptionAlert;
import prpg.gui.gamePanel.GamePanelController;

public class Resource extends GameObject {
    public Resource(String displayName, String description) {
        super(displayName, description);
    }

    public void gather() {
        GamePanelController.callAlert(new ExceptionAlert("Resource", "You are gathering resources", "Gathering from " + this.displayName));
    }

}
