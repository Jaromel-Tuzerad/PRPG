package prpg.mapping;

import prpg.gameLogic.entities.Entity;

import java.util.ArrayList;

public class Tile {

    private char icon;
    private String name;
    private String description;
    private ArrayList<Entity> entities;
    private boolean explored;

    public Tile(char icon, String name, String description) {
        this.icon = icon;
        this.name = name;
        this.description = description;
        this.entities = new ArrayList<>();
        this.explored = false;
    }

    public char getIcon() {
        return icon;
    }

    public String getDescription() { return description; }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        this.entities.remove(entity);
    }

    public boolean isExplored() {
        return this.explored;
    }

    public void setExplored(boolean status) {
        this.explored = status;
    }

    public String getName() {
        return this.name;
    }

}
