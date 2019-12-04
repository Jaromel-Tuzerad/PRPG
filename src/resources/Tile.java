package resources;

import java.util.ArrayList;

public class Tile {

    private char icon;
    private String description;
    private ArrayList<Entity> entities;

    public Tile(char icon, String description) {
        this.icon = icon;
        this.description = description;
        this.entities = new ArrayList<>();
    }

    public char getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
}
