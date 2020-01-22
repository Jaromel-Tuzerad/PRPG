package gameLogic.entities;

import gui.GameController;

public class Resource extends Object {
    public Resource(int x, int y, String displayName, String description) {
        super(x, y, displayName, description);
    }

    public void gather() {
        GameController.callAlert("Resource", "You are gathering resources", "Gathering from " + this.displayName);
    }

}
