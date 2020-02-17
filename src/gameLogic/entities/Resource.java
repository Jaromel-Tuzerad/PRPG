package gameLogic.entities;

import exceptions.ExceptionAlert;
import gui.GameController;

public class Resource extends Object {
    public Resource(int x, int y, String displayName, String description) {
        super(x, y, displayName, description);
    }

    public void gather() {
        GameController.callAlert(new ExceptionAlert("Resource", "You are gathering resources", "Gathering from " + this.displayName));
    }

}
