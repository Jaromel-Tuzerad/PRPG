package gameLogic.entities;

import exceptions.ExceptionAlert;
import gui.gamePanel.GamePanelController;

public class Resource extends GameObject {
    public Resource(int x, int y, String displayName, String description) {
        super(x, y, displayName, description);
    }

    public void gather() {
        GamePanelController.callAlert(new ExceptionAlert("Resource", "You are gathering resources", "Gathering from " + this.displayName));
    }

}
