package prpg.gameLogic;

import prpg.exceptions.XMLException;
import prpg.gameLogic.entities.mobs.Enemy;
import prpg.gameLogic.entities.mobs.Player;
import prpg.gameLogic.entities.objects.Shop;
import prpg.gameLogic.mapping.Map;

import java.io.Serializable;

public class Game implements Serializable {

    private Player currentPlayer;
    public GameFactory gameFactory;
    public Map currentMap;
    public Enemy currentEnemy;
    public Shop currentShop;

    public Game(String playerName) throws XMLException {
        this.currentPlayer = new Player(4, 4, playerName, 5, 5, 5);
        this.gameFactory = new GameFactory();
        this.currentMap = new Map();
        this.currentEnemy = null;
        this.currentShop = null;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setGameFactory(GameFactory gameFactory) {
        this.gameFactory = gameFactory;
    }

    public void setCurrentMap(Map currentMap) {
        this.currentMap = currentMap;
    }

    public void setCurrentEnemy(Enemy currentEnemy) {
        this.currentEnemy = currentEnemy;
    }

    public void setCurrentShop(Shop currentShop) {
        this.currentShop = currentShop;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Map getCurrentMap() {
        return currentMap;
    }

    public GameFactory getGameFactory() {
        return gameFactory;
    }

    public Enemy getCurrentEnemy() {
        return currentEnemy;
    }

    public Shop getCurrentShop() {
        return currentShop;
    }
}
