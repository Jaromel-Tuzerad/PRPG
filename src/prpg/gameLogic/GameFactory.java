package prpg.gameLogic;

import prpg.exceptions.XMLException;
import prpg.gameLogic.entities.Entity;
import prpg.gameLogic.entities.mobs.Enemy;
import prpg.gameLogic.entities.mobs.NPC;
import prpg.gameLogic.entities.objects.Shop;
import prpg.gameLogic.items.Equipment;
import prpg.gameLogic.items.Food;
import prpg.gameLogic.items.InventoryItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import prpg.gameLogic.items.QuestItem;
import prpg.gameLogic.quests.FetchQuest;
import prpg.gameLogic.quests.KillQuest;
import prpg.gameLogic.quests.Quest;
import prpg.gameLogic.mapping.Tile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;

public class GameFactory implements Serializable {

    private Document enemiesDoc;
    private Document npcsDoc;
    private Document shopsDoc;
    private Document equipmentDoc;
    private Document foodsDoc;
    private Document questItemsDoc;
    private Document tilesDoc;

    public GameFactory() {
        try {
            // an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();

            File enemiesFile = new File("resources/entities/enemies.xml");
            File npcsFile = new File("resources/entities/npcs.xml");
            File shopsFile = new File("resources/entities/shops.xml");
            File equipmentFile = new File("resources/items/equipment.xml");
            File foodsFile = new File("resources/items/foods.xml");
            File questItemsFile = new File("resources/items/questItems.xml");
            File tilesFile = new File("resources/tiles.xml");

            this.enemiesDoc = db.parse(enemiesFile);
            this.npcsDoc = db.parse(npcsFile);
            this.shopsDoc = db.parse(shopsFile);
            this.equipmentDoc = db.parse(equipmentFile);
            this.foodsDoc = db.parse(foodsFile);
            this.questItemsDoc = db.parse(questItemsFile);
            this.tilesDoc = db.parse(tilesFile);

            this.enemiesDoc.getDocumentElement().normalize();
            this.npcsDoc.getDocumentElement().normalize();
            this.shopsDoc.getDocumentElement().normalize();
            this.equipmentDoc.getDocumentElement().normalize();
            this.foodsDoc.getDocumentElement().normalize();
            this.questItemsDoc.getDocumentElement().normalize();
            this.tilesDoc.getDocumentElement().normalize();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

//  Enemies
    public Enemy getEnemyWithID(int id) throws XMLException {
        // nodeList is not iterable, so we are using for loop
        NodeList enemiesNodeList = this.enemiesDoc.getElementsByTagName("enemy");
        for (int i = 0; i < enemiesNodeList.getLength(); i++) {
            Element enemyElement = (Element) enemiesNodeList.item(i);
            int enemyElementId = Integer.parseInt(enemyElement.getElementsByTagName("id").item(0).getTextContent());
            if (enemyElementId == id) {

                // Rolling items that the enemy drops upon death
                NodeList droppedItemsNodeList = enemyElement.getElementsByTagName("item");
                ArrayList<InventoryItem> droppedItems = new ArrayList<>();

                for (int temp = 0; temp < droppedItemsNodeList.getLength(); temp++) {
                    Element itemElement = (Element) droppedItemsNodeList.item(temp);
                    if (RandomFunctions.randomChance(Double.parseDouble(itemElement.getElementsByTagName("dropChance").item(0).getTextContent()))) {
                        int itemElementID = Integer.parseInt(itemElement.getElementsByTagName("itemId").item(0).getTextContent());
                        InventoryItem item = null;
                        switch(itemElement.getAttribute("type")) {
                            case "EQUIPMENT":
                                item = getEquipmentWithId(itemElementID);
                                break;
                            case "FOOD":
                                item = getFoodWithId(itemElementID);
                                break;
                            case "QUESTITEM":
                                item = getQuestItemWithId(itemElementID);
                                break;
                        }
                        if(item != null) {
                            droppedItems.add(item);
                        } else {
                            throw new XMLException("Selected item type is invalid");
                        }
                    }
                }

                Enemy outputEnemy = new Enemy(
                        Integer.parseInt(enemyElement.getElementsByTagName("id").item(0).getTextContent()),
                        enemyElement.getElementsByTagName("name").item(0).getTextContent(),
                        enemyElement.getElementsByTagName("description").item(0).getTextContent(),
                        Integer.parseInt(enemyElement.getElementsByTagName("maxHealth").item(0).getTextContent()),
                        Integer.parseInt(enemyElement.getElementsByTagName("level").item(0).getTextContent()),
                        Integer.parseInt(enemyElement.getElementsByTagName("strength").item(0).getTextContent()),
                        Integer.parseInt(enemyElement.getElementsByTagName("dexterity").item(0).getTextContent()),
                        Integer.parseInt(enemyElement.getElementsByTagName("defense").item(0).getTextContent()),
                        Integer.parseInt(enemyElement.getElementsByTagName("yieldsExperience").item(0).getTextContent()),
                        Integer.parseInt(enemyElement.getElementsByTagName("yieldsGold").item(0).getTextContent()));

                outputEnemy.addToInventory(droppedItems);

                return outputEnemy;
            }
        }
        throw new XMLException("Enemy with id " + id + " not found in enemies.xml");
    }
    public Enemy getRandomEnemy() throws XMLException {
        NodeList enemiesNodeList = this.enemiesDoc.getElementsByTagName("enemy");
        return getEnemyWithID(RandomFunctions.getRandomNumberInRange(0, enemiesNodeList.getLength()-1));
    }
    public ArrayList<Enemy> getEnemiesOfLevel(int level) throws XMLException {
        NodeList enemiesNodeList = this.enemiesDoc.getElementsByTagName("enemy");
        ArrayList<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < enemiesNodeList.getLength(); i++) {
            Enemy enemy = getEnemyWithID(i);
            if(enemy.getLevel() == level) {
                enemies.add(enemy);
            }
        }
        if(!enemies.isEmpty()) {
            return enemies;
        } else {
            throw new XMLException("No enemies of level " + level + " found in enemies.xml");
        }
    }
    public Enemy getRandomEnemyOfLevel(int level) throws XMLException {
        ArrayList<Enemy> enemies = getEnemiesOfLevel(level);
        if(enemies.size() > 1) {
            return enemies.get(RandomFunctions.getRandomNumberInRange(0, enemies.size()-1));
        } else {
            return enemies.get(0);
        }
    }
//  NPCs
    public NPC getNPCWithID(int id) throws XMLException {
        // nodeList is not iterable, so we are using for loop
        NodeList npcNodeList = this.npcsDoc.getElementsByTagName("npc");
        for (int i = 0; i < npcNodeList.getLength(); i++) {
            Node npcNode = npcNodeList.item(i);
            Element npcElement = (Element) npcNode;
            int parsedEnemyId = Integer.parseInt(npcElement.getElementsByTagName("id").item(0).getTextContent());
            if (parsedEnemyId == id) {
                try {
                    return new NPC(
                            npcElement.getElementsByTagName("name").item(0).getTextContent(),
                            Integer.parseInt(npcElement.getElementsByTagName("level").item(0).getTextContent()));
                } catch (Exception e) {
                    throw new XMLException("Error while parsing npcs.xml - npc with the following attributes couldn't be created " +
                            "\nname: " + npcElement.getElementsByTagName("name").item(0).getTextContent());
                }
            }
        }
        throw new XMLException("Error while parsing enemies.xml - enemy with id " + id + " not found");
}
    public NPC getRandomNPC() throws XMLException {
        NodeList npcNodeList = this.npcsDoc.getElementsByTagName("npc");
        return getNPCWithID(RandomFunctions.getRandomNumberInRange(0, npcNodeList.getLength()-1));
    }
//  Shops
    public Shop getShopWithID(int id) throws XMLException {
        NodeList shopNodeList = this.shopsDoc.getElementsByTagName("shop");
        for (int i = 0; i < shopNodeList.getLength(); i++) {
            Element shopElement = (Element) shopNodeList.item(i);
            int shopElementId = Integer.parseInt(shopElement.getElementsByTagName("id").item(0).getTextContent());
            if(shopElementId == id) {

                Shop outputShop = new Shop(
                        id,
                        shopElement.getElementsByTagName("name").item(0).getTextContent(),
                        shopElement.getElementsByTagName("description").item(0).getTextContent(),
                        Integer.parseInt(shopElement.getElementsByTagName("restockCost").item(0).getTextContent()));

                outputShop.addToInventory(getNewShopStockWithId(id));
                return outputShop;
            }
        }
        throw new XMLException("Shop with id " + id + " not found in shops.xml");
    }
    public ArrayList<InventoryItem> getNewShopStockWithId(int id) throws XMLException {
        // nodeList is not iterable, so we are using for loop
        NodeList shopsNodeList = this.shopsDoc.getElementsByTagName("shop");
        for (int i = 0; i < shopsNodeList.getLength(); i++) {
            Element shopElement = (Element) shopsNodeList.item(i);
            int parsedShopId = Integer.parseInt(shopElement.getElementsByTagName("id").item(0).getTextContent());
            if (parsedShopId == id) {
                NodeList stockItemsNodeList = shopElement.getElementsByTagName("item");
                ArrayList<InventoryItem> stockItems = new ArrayList<>();
                for (int j = 0; j < stockItemsNodeList.getLength(); j++) {
                    Element itemElement = (Element) stockItemsNodeList.item(j);
                    // Roll if the item will be in stock
                    if (RandomFunctions.randomChance(Double.parseDouble(itemElement.getElementsByTagName("availableChance").item(0).getTextContent()))) {
                        int itemElementId = Integer.parseInt(itemElement.getElementsByTagName("itemId").item(0).getTextContent());
                        InventoryItem item = null;
                        switch(itemElement.getAttribute("type")) {
                            case "EQUIPMENT":
                                item = getEquipmentWithId(itemElementId);
                                break;
                            case "FOOD":
                                item = getFoodWithId(itemElementId);
                                break;
                            case "QUESTITEM":
                                item = getQuestItemWithId(itemElementId);
                                break;
                        }
                        if(item != null) {
                            stockItems.add(item);
                        } else {
                            throw new XMLException("Selected item type is invalid");
                        }
                    }
                }
                return stockItems;
            }
        }
        throw new XMLException("Error while parsing shops.xml - shop with id " + id + " not found");
    }
//  Equipment
    public Equipment getEquipmentWithId(int id) throws XMLException {
        NodeList equipmentNodeList = this.equipmentDoc.getElementsByTagName("equipmentPiece");
        for (int i = 0; i < equipmentNodeList.getLength(); i++) {
            Element equipmentPieceElement = (Element) equipmentNodeList.item(i);
            int equipmentPieceId = Integer.parseInt(equipmentPieceElement.getElementsByTagName("id").item(0).getTextContent());
            if(equipmentPieceId == id) {
                return new Equipment(
                        equipmentPieceElement.getElementsByTagName("displayName").item(0).getTextContent(),
                        Integer.parseInt(equipmentPieceElement.getElementsByTagName("addedStrength").item(0).getTextContent()),
                        Integer.parseInt(equipmentPieceElement.getElementsByTagName("addedDexterity").item(0).getTextContent()),
                        Integer.parseInt(equipmentPieceElement.getElementsByTagName("addedDefense").item(0).getTextContent()),
                        InventoryItem.ItemType.valueOf(equipmentPieceElement.getAttribute("type")),
                        Integer.parseInt(equipmentPieceElement.getElementsByTagName("equipmentLevel").item(0).getTextContent()));
            }
        }
        throw new XMLException("Equipment with id " + id + " not found in equipment.xml");
    }
    public InventoryItem getRandomEquipment() throws XMLException {
        NodeList equipmentNodeList = this.equipmentDoc.getElementsByTagName("equipmentPiece");
        return getEquipmentWithId(RandomFunctions.getRandomNumberInRange(0, equipmentNodeList.getLength()-1));
    }
    public ArrayList<Equipment> getEquipmentOfLevel(int level) throws XMLException {
        NodeList equipmentNodeList = this.equipmentDoc.getElementsByTagName("equipmentPiece");
        ArrayList<Equipment> equipment = new ArrayList<>();
        for (int i = 0; i < equipmentNodeList.getLength(); i++) {
            Equipment item = getEquipmentWithId(i);
            if(item.getEquipmentLevel() == level) {
                equipment.add(item);
            }
        }
        if(!equipment.isEmpty()) {
            return equipment;
        } else {
            throw new XMLException("No equipment of level " + level + " found in equipmment.xml");
        }
    }
    public Equipment getRandomEquipmentOfLevel(int level) throws XMLException {
        ArrayList<Equipment> equipment = getEquipmentOfLevel(level);
        if(equipment.size() > 1) {
            return equipment.get(RandomFunctions.getRandomNumberInRange(0, equipment.size()-1));
        } else {
            return equipment.get(0);
        }
    }
//  Food
    public Food getFoodWithId(int id) throws XMLException {
        NodeList foodNodeList = this.foodsDoc.getElementsByTagName("food");
        for (int i = 0; i < foodNodeList.getLength(); i++) {
            Element foodElement = (Element) foodNodeList.item(i);
            int foodElementId = Integer.parseInt(foodElement.getElementsByTagName("id").item(0).getTextContent());
            if(foodElementId == id) {
                return new Food(foodElement.getElementsByTagName("displayName").item(0).getTextContent(),
                        Integer.parseInt(foodElement.getElementsByTagName("saturationValue").item(0).getTextContent()));
            }
        }
        throw new XMLException("Food with id " + id + " not found in foods.xml");
    }
//  QuestItems
    public QuestItem getQuestItemWithId(int id) throws XMLException {
        NodeList questItemNodeList = this.questItemsDoc.getElementsByTagName("questItem");
        for (int i = 0; i < questItemNodeList.getLength(); i++) {
            Element questItemElement = (Element) questItemNodeList.item(i);
            int questItemId = Integer.parseInt(questItemElement.getElementsByTagName("id").item(0).getTextContent());
            if(questItemId == id) {
                return new QuestItem(questItemElement.getElementsByTagName("displayName").item(0).getTextContent());
            }
        }
        throw new XMLException("Error while parsing questItems.xml - questItem with id " + id + " not found");
    }
    public InventoryItem getRandomQuestItem() throws XMLException {
        NodeList questItemNodeList = this.questItemsDoc.getElementsByTagName("questItem");
        return getQuestItemWithId(RandomFunctions.getRandomNumberInRange(0, questItemNodeList.getLength()-1));
    }
//  Tiles
    public Tile getTileWithID(int id) throws XMLException {
        // nodeList is not iterable, so we are using for loop
        NodeList tileNodeList = this.tilesDoc.getElementsByTagName("tile");
        for (int i = 0; i < tileNodeList.getLength(); i++) {
            Element tileElement = (Element) tileNodeList.item(i);
            int parsedTileId = Integer.parseInt(tileElement.getElementsByTagName("id").item(0).getTextContent());
            if(parsedTileId == id) {

                NodeList entitiesNodeList = tileElement.getElementsByTagName("entity");
                ArrayList<Entity> tileEntities = new ArrayList<>();

                for (int j = 0; j < entitiesNodeList.getLength(); j++) {
                    Element entityElement = (Element) entitiesNodeList.item(j);
                    if(RandomFunctions.randomChance(Double.parseDouble(entityElement.getElementsByTagName("chanceToAppear").item(0).getTextContent()))) {
                        int entityId = Integer.parseInt(entityElement.getElementsByTagName("entityId").item(0).getTextContent());
                        switch(entityElement.getAttribute("type")) {
                            case "SHOP":
                                tileEntities.add(getShopWithID(entityId));
                                break;
                            case "ENEMY":
                                tileEntities.add(getEnemyWithID(entityId));
                                break;
                            case "NPC":
                                tileEntities.add(getNPCWithID(entityId));
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
//  Quests
    public Quest getNewQuestOfType(NPC questgiver, Quest.QuestType type) throws XMLException {
        switch (type) {
            case KILL:
                return new KillQuest("Kill enemies", questgiver, RandomFunctions.getRandomNumberInRange(1, 5), getRandomEnemyOfLevel(questgiver.getLevel()+1));
            case FETCH:
                ArrayList<InventoryItem> rewardItems = new ArrayList<>();
                rewardItems.add(getRandomEquipmentOfLevel(questgiver.getLevel()));
               return new FetchQuest("Collect item", questgiver, rewardItems, (QuestItem) getRandomQuestItem(), RandomFunctions.getRandomNumberInRange(0, 8), RandomFunctions.getRandomNumberInRange(0, 8));
        }
        throw new XMLException("Selected quest type is invalid");
    }
    public Quest getNewQuest(NPC questgiver) throws XMLException {
        return getNewQuestOfType(questgiver, Quest.QuestType.values()[RandomFunctions.getRandomNumberInRange(0, Quest.QuestType.values().length-1)]);
    }
}

