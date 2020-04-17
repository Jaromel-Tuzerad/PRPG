package prpg.gameLogic;

import prpg.exceptions.XMLException;
import prpg.gameLogic.entities.Entity;
import prpg.gameLogic.entities.mobs.Enemy;
import prpg.gameLogic.entities.objects.Shop;
import prpg.gameLogic.items.Equipment;
import prpg.gameLogic.items.Food;
import prpg.gameLogic.items.InventoryItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import prpg.mapping.Tile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;

public class GameFactory {

    private Document enemiesDoc;
    private Document itemsDoc;
    private Document shopsDoc;
    private Document tilesDoc;

    public GameFactory() throws IOException {
        try {
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();

            //creating a constructor of file class and parsing an XML file
            File enemiesFile = new File("resources/enemies.xml");
            File itemsFile = new File("resources/items.xml");
            File shopsFile = new File("resources/shops.xml");
            File tilesFile = new File("resources/tiles.xml");

            this.enemiesDoc = db.parse(enemiesFile);
            this.itemsDoc = db.parse(itemsFile);
            this.shopsDoc = db.parse(shopsFile);
            this.tilesDoc = db.parse(tilesFile);

            this.enemiesDoc.getDocumentElement().normalize();
            this.itemsDoc.getDocumentElement().normalize();
            this.shopsDoc.getDocumentElement().normalize();
            this.tilesDoc.getDocumentElement().normalize();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Enemy getRandomEnemy() throws XMLException {
        NodeList enemiesNodeList = this.enemiesDoc.getElementsByTagName("enemy");
        return getEnemyWithID(RandomFunctions.getRandomNumberInRange(0, enemiesNodeList.getLength()-1));
    }

    public Enemy getEnemyWithID(int id) throws XMLException {
        NodeList enemiesNodeList = this.enemiesDoc.getElementsByTagName("enemy");
        Node enemyNode = enemiesNodeList.item(id);
        Element enemyElement = (Element) enemyNode;
        NodeList droppedItemsNodeList = enemyElement.getElementsByTagName("item");
        ArrayList<InventoryItem> droppedItems = new ArrayList<>();

        for (int temp = 0; temp < droppedItemsNodeList.getLength(); temp++) {
            Node nNode = droppedItemsNodeList.item(temp);
            Element eElement = (Element) nNode;
            if(RandomFunctions.randomChance(Double.parseDouble(eElement.getElementsByTagName("dropChance").item(0).getTextContent()))) {
                int itemID = Integer.parseInt(eElement.getElementsByTagName("itemId").item(0).getTextContent());
                droppedItems.add(getItemWithID(itemID));
            }
        }
        try {
            return new Enemy(
                    enemyElement.getElementsByTagName("name").item(0).getTextContent(),
                    enemyElement.getElementsByTagName("description").item(0).getTextContent(),
                    Integer.parseInt(enemyElement.getElementsByTagName("maxHealth").item(0).getTextContent()),
                    Integer.parseInt(enemyElement.getElementsByTagName("level").item(0).getTextContent()),
                    Integer.parseInt(enemyElement.getElementsByTagName("strength").item(0).getTextContent()),
                    Integer.parseInt(enemyElement.getElementsByTagName("dexterity").item(0).getTextContent()),
                    Integer.parseInt(enemyElement.getElementsByTagName("defense").item(0).getTextContent()),
                    droppedItems,
                    Integer.parseInt(enemyElement.getElementsByTagName("yieldsExperience").item(0).getTextContent()),
                    Integer.parseInt(enemyElement.getElementsByTagName("yieldsGold").item(0).getTextContent()));
        } catch(Exception e) {
            throw new XMLException("Error while parsing enemies.xml - enemy with the following attributes couldn't be created " +
                    "\nname: " + enemyElement.getElementsByTagName("name").item(0).getTextContent() +
                    "\ndescription: " + enemyElement.getElementsByTagName("description").item(0).getTextContent() +
                    "\nmaxHealth: " + enemyElement.getElementsByTagName("maxHealth").item(0).getTextContent() +
                    "\nlevel: " + enemyElement.getElementsByTagName("level").item(0).getTextContent() +
                    "\nstrength: " + enemyElement.getElementsByTagName("strength").item(0).getTextContent() +
                    "\ndexterity: " + enemyElement.getElementsByTagName("dexterity").item(0).getTextContent() +
                    "\ndefense: " + enemyElement.getElementsByTagName("defense").item(0).getTextContent() +
                    "\ndroppedItems: " + droppedItems.toString() +
                    "\nyieldsExperience: " + enemyElement.getElementsByTagName("yieldsExperience").item(0).getTextContent() +
                    "\nyieldsGold: " + enemyElement.getElementsByTagName("yieldsGold").item(0).getTextContent());

        }

    }

    public ArrayList<Enemy> getEnemiesOfLevel(int level) throws XMLException {
        NodeList enemiesNodeList = this.enemiesDoc.getElementsByTagName("enemy");
        ArrayList<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < enemiesNodeList.getLength(); i++) {
            Node enemyNode = enemiesNodeList.item(i);
            Element enemyElement = (Element) enemyNode;
            int enemyLevel = Integer.parseInt(enemyElement.getElementsByTagName("level").item(0).getTextContent());
            if(enemyLevel == level) {
                NodeList droppedItemsNodeList = enemyElement.getElementsByTagName("item");
                ArrayList<InventoryItem> droppedItems = new ArrayList<>();

                for (int j = 0; j < droppedItemsNodeList.getLength(); j++) {
                    Node nNode = droppedItemsNodeList.item(j);
                    Element eElement = (Element) nNode;
                    if(RandomFunctions.randomChance(Double.parseDouble(eElement.getElementsByTagName("dropChance").item(0).getTextContent()))) {
                        int itemID = Integer.parseInt(eElement.getElementsByTagName("itemId").item(0).getTextContent());
                        droppedItems.add(getItemWithID(itemID));
                    }
                }

                enemies.add(new Enemy(enemyElement.getElementsByTagName("name").item(0).getTextContent(),
                        enemyElement.getElementsByTagName("description").item(0).getTextContent(),
                        Integer.parseInt(enemyElement.getElementsByTagName("maxHealth").item(0).getTextContent()),
                        Integer.parseInt(enemyElement.getElementsByTagName("level").item(0).getTextContent()),
                        Integer.parseInt(enemyElement.getElementsByTagName("strength").item(0).getTextContent()),
                        Integer.parseInt(enemyElement.getElementsByTagName("dexterity").item(0).getTextContent()),
                        Integer.parseInt(enemyElement.getElementsByTagName("defense").item(0).getTextContent()),
                        droppedItems,
                        Integer.parseInt(enemyElement.getElementsByTagName("yieldsExperience").item(0).getTextContent()),
                        Integer.parseInt(enemyElement.getElementsByTagName("yieldsGold").item(0).getTextContent())));
            }
        }
        return enemies;
    }

    public Enemy getRandomEnemyOfLevel(int level) throws XMLException {
        ArrayList<Enemy> enemies = getEnemiesOfLevel(level);
        if(enemies.size() == 1) {
            return enemies.get(0);
        } else {
            return enemies.get(RandomFunctions.getRandomNumberInRange(0, enemies.size()-1));
        }
    }

    public InventoryItem getItemWithID(int id) throws XMLException {
        // nodeList is not iterable, so we are using for loop
        NodeList itemsNodeList = this.itemsDoc.getElementsByTagName("item");
        for (int i = 0; i < itemsNodeList.getLength(); i++) {
            Node itemNode = itemsNodeList.item(i);
            Element itemElement = (Element) itemNode;
            int parsedItemId = Integer.parseInt(itemElement.getElementsByTagName("id").item(0).getTextContent());
            if(parsedItemId == id) {
                InventoryItem.ItemType type = InventoryItem.ItemType.valueOf(itemElement.getAttribute("type"));
                if(type == InventoryItem.ItemType.HEADWEAR || type == InventoryItem.ItemType.BODYWEAR || type == InventoryItem.ItemType.LEGWEAR || type == InventoryItem.ItemType.WEAPON) {
                    return new Equipment(
                            itemElement.getElementsByTagName("displayName").item(0).getTextContent(),
                            Integer.parseInt(itemElement.getElementsByTagName("addedStrength").item(0).getTextContent()),
                            Integer.parseInt(itemElement.getElementsByTagName("addedDexterity").item(0).getTextContent()),
                            Integer.parseInt(itemElement.getElementsByTagName("addedDefense").item(0).getTextContent()),
                            type
                    );
                } else if(type == InventoryItem.ItemType.FOOD) {
                    return new Food(
                            itemElement.getElementsByTagName("displayName").item(0).getTextContent(),
                            Integer.parseInt(itemElement.getElementsByTagName("saturationValue").item(0).getTextContent())
                    );
                }
            }
        }
        throw new XMLException("Error while parsing items.xml - item with id " + id + " not found");
    }

    public Shop getShopWithID(int id) throws XMLException {
        // nodeList is not iterable, so we are using for loop
        NodeList shopsNodeList = this.shopsDoc.getElementsByTagName("shop");
        for (int i = 0; i < shopsNodeList.getLength(); i++) {
            Node shopNode = shopsNodeList.item(i);
            Element shopElement = (Element) shopNode;
            int parsedShopId = Integer.parseInt(shopElement.getElementsByTagName("id").item(0).getTextContent());
            if(parsedShopId == id) {

                NodeList stockItemsNodeList = shopElement.getElementsByTagName("item");
                ArrayList<InventoryItem> stockItems = new ArrayList<>();

                for (int j = 0; j < stockItemsNodeList.getLength(); j++) {
                    Node nNode = stockItemsNodeList.item(j);
                    Element eElement = (Element) nNode;
                    if(RandomFunctions.randomChance(Double.parseDouble(eElement.getElementsByTagName("availableChance").item(0).getTextContent()))) {
                        int itemID = Integer.parseInt(eElement.getElementsByTagName("itemId").item(0).getTextContent());
                        stockItems.add(getItemWithID(itemID));
                    }
                }

                Shop outputShop = new Shop(
                        id,
                        shopElement.getElementsByTagName("name").item(0).getTextContent(),
                        shopElement.getElementsByTagName("description").item(0).getTextContent(),
                        Integer.parseInt(shopElement.getElementsByTagName("restockCost").item(0).getTextContent())
                );

                outputShop.addItems(stockItems);

                return outputShop;
            }
        }
        throw new XMLException("Error while parsing shops.xml - shop with id " + id + " not found");
    }

    public ArrayList<InventoryItem> getNewStockFromShopWithId(int id) throws XMLException {
        // nodeList is not iterable, so we are using for loop
        NodeList shopsNodeList = this.shopsDoc.getElementsByTagName("shop");
        for (int i = 0; i < shopsNodeList.getLength(); i++) {
            Node shopNode = shopsNodeList.item(i);
            Element shopElement = (Element) shopNode;
            int parsedShopId = Integer.parseInt(shopElement.getElementsByTagName("id").item(0).getTextContent());
            if (parsedShopId == id) {

                NodeList stockItemsNodeList = shopElement.getElementsByTagName("item");
                ArrayList<InventoryItem> stockItems = new ArrayList<>();

                for (int j = 0; j < stockItemsNodeList.getLength(); j++) {
                    Node nNode = stockItemsNodeList.item(j);
                    Element eElement = (Element) nNode;
                    if (RandomFunctions.randomChance(Double.parseDouble(eElement.getElementsByTagName("availableChance").item(0).getTextContent()))) {
                        int itemID = Integer.parseInt(eElement.getElementsByTagName("itemId").item(0).getTextContent());
                        stockItems.add(getItemWithID(itemID));
                    }
                }

                return stockItems;

            }
        }
        throw new XMLException("Error while parsing shops.xml - shop with id " + id + " not found");
    }

    public Tile getTileWithID(int id) throws XMLException {
        // nodeList is not iterable, so we are using for loop
        NodeList tileNodeList = this.tilesDoc.getElementsByTagName("tile");
        for (int i = 0; i < tileNodeList.getLength(); i++) {
            Node tileNode = tileNodeList.item(i);
            Element tileElement = (Element) tileNode;
            int parsedTileId = Integer.parseInt(tileElement.getElementsByTagName("id").item(0).getTextContent());
            if(parsedTileId == id) {

                NodeList entitiesNodeList = tileElement.getElementsByTagName("entity");
                ArrayList<Entity> tileEntities = new ArrayList<>();

                for (int j = 0; j < entitiesNodeList.getLength(); j++) {
                    Node nNode = entitiesNodeList.item(j);
                    Element eElement = (Element) nNode;
                    if(RandomFunctions.randomChance(Double.parseDouble(eElement.getElementsByTagName("chanceToAppear").item(0).getTextContent()))) {
                        switch(eElement.getAttribute("type")) {
                            case "SHOP":
                                tileEntities.add(getShopWithID(Integer.parseInt(eElement.getElementsByTagName("entityId").item(0).getTextContent())));
                                break;
                            case "ENEMY":
                                tileEntities.add(getEnemyWithID(Integer.parseInt(eElement.getElementsByTagName("entityId").item(0).getTextContent())));
                                break;
                        }
                    }
                }

                Tile outputTile = new Tile(
                        tileElement.getElementsByTagName("icon").item(0).getTextContent().charAt(0),
                        tileElement.getElementsByTagName("name").item(0).getTextContent(),
                        tileElement.getElementsByTagName("description").item(0).getTextContent()
                );

                // Adding entities to the outputTile
                for(Entity e : tileEntities) {
                    outputTile.addEntity(e);
                }

                return outputTile;
            }
        }
        throw new XMLException("Error while parsing tiles.xml - tile with id " + id + " not found");
    }

}
