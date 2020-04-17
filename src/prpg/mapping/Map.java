package prpg.mapping;

import prpg.exceptions.XMLException;
import prpg.gameLogic.GameFactory;
import prpg.gameLogic.RandomFunctions;
import prpg.gui.gamePanel.GamePanelController;

import java.io.IOException;

public class Map {
    private Tile[][] tileMap = new Tile[9][9];

    //Generates a new map based on the difficulty level
    public Map() throws IOException, XMLException {
        GameFactory gameFactory = new GameFactory();
        int[] tileIDs = {0, 2};
        this.tileMap[RandomFunctions.getRandomNumberInRange(0, 8)][RandomFunctions.getRandomNumberInRange(0, 8)] = gameFactory.getTileWithID(3);
        for(int x = 0; x<9; x++) {
            for(int y = 0; y<9; y+=1) {
                if(this.tileMap[x][y] == null) {
                    if(x == y) {
                        this.tileMap[x][y] = gameFactory.getTileWithID(1);
                    } else {
                        this.tileMap[x][y] = gameFactory.getTileWithID(tileIDs[RandomFunctions.getRandomNumberInRange(0, tileIDs.length-1)]);
                    }
                }
            }
        }
    }

    public Tile getTileByCoords(int x, int y) {
        return this.tileMap[x][y];
    }

}
