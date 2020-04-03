package gameLogic.entities.objects;

import exceptions.ExceptionAlert;
import gui.gamePanel.GamePanelController;

public class Resource extends GameObject {
    public Resource(String displayName, String description) {
        super(displayName, description);
    }

    public void gather() {
        GamePanelController.callAlert(new ExceptionAlert("Resource", "You are gathering resources", "Gathering from " + this.displayName));
    }

}
