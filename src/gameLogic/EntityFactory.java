package gameLogic;

import gameLogic.entities.mobs.Enemy;
import gameLogic.inventory.Equipment;
import gameLogic.inventory.Food;
import gameLogic.inventory.InventoryItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;

public class EntityFactory {

    private Document enemiesDoc;
    private Document itemsDoc;

    public EntityFactory() throws IOException {
        try {
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();

            //creating a constructor of file class and parsing an XML file
            File enemiesFile = new File("resources/enemies.xml");
            File itemsFile = new File("resources/items.xml");
            this.enemiesDoc = db.parse(enemiesFile);
            this.itemsDoc = db.parse(itemsFile);
            this.enemiesDoc.getDocumentElement().normalize();
            this.itemsDoc.getDocumentElement().normalize();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Enemy getRandomEnemy() {
        NodeList enemiesNodeList = this.enemiesDoc.getElementsByTagName("enemy");
        Node enemyNode = enemiesNodeList.item(RandomFunctions.getRandomNumberInRange(0, enemiesNodeList.getLength()-1));
        Element enemyElement = (Element) enemyNode;
        NodeList droppedItemsNodeList = enemyElement.getElementsByTagName("item");
        ArrayList<InventoryItem> droppedItems = new ArrayList<>();

        for (int temp = 0; temp < droppedItemsNodeList.getLength(); temp++) {
            Node nNode = droppedItemsNodeList.item(temp);
            Element eElement = (Element) nNode;
            int itemID = Integer.parseInt(eElement.getElementsByTagName("itemId").item(0).getTextContent());
            droppedItems.add(getItemByID(itemID));
        }

        return new Enemy(enemyElement.getElementsByTagName("name").item(0).getTextContent(),
                enemyElement.getElementsByTagName("description").item(0).getTextContent(),
                Integer.parseInt(enemyElement.getElementsByTagName("maxHealth").item(0).getTextContent()),
                Integer.parseInt(enemyElement.getElementsByTagName("level").item(0).getTextContent()),
                Integer.parseInt(enemyElement.getElementsByTagName("strength").item(0).getTextContent()),
                Integer.parseInt(enemyElement.getElementsByTagName("dexterity").item(0).getTextContent()),
                Integer.parseInt(enemyElement.getElementsByTagName("defense").item(0).getTextContent()),
                droppedItems,
                Integer.parseInt(enemyElement.getElementsByTagName("yieldsExperience").item(0).getTextContent()));
    }

    public InventoryItem getItemByID(int id) {
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
        return null;
    }

}
